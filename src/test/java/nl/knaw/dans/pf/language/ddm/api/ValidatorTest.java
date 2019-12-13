/**
 * Copyright (C) 2014 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.pf.language.ddm.api;

import static nl.knaw.dans.pf.language.ddm.api.SpecialValidator.RECENT_SCHEMAS;
import static nl.knaw.dans.pf.language.ddm.handlermaps.NameSpace.DC;
import static nl.knaw.dans.pf.language.ddm.handlermaps.NameSpace.DDM;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import nl.knaw.dans.pf.language.ddm.handlermaps.NameSpace;
import nl.knaw.dans.pf.language.xml.validation.XMLErrorHandler;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidatorTest {

    private static final Logger logger = LoggerFactory.getLogger(ValidatorTest.class);

    @BeforeClass
    public static void externalSchemaCheck() {
        // ignore test case if not available
        assumeTrue("can access " + DC.xsd, canConnect(DC.xsd));
    }

    @Test
    public void testValidation() throws Exception {
        Source xmlSource = new StreamSource(new File("src/test/resources/input/spatial.xml"));
        XMLErrorHandler handler = new SpecialValidator().validate(xmlSource);
        logger.info(handler.getMessages());
        logger.info(handler.passed() + "");
    }

    @Test
    public void testDDMValidation() throws Exception {
        assumeTrue("last DDM is published", lastDdmIsPublished());
        XMLErrorHandler handler = new DDMValidator().validate(new File("src/test/resources/input/ddm.xml"));

        assertTrue(handler.getMessages(), handler.passed());
    }

    @Test
    public void testDDMValidationWithDOI() throws Exception {
        assumeTrue("last DDM is published", lastDdmIsPublished());
        XMLErrorHandler handler = new DDMValidator().validate(new File("src/test/resources/input/ddm-with-doi.xml"));
        assertTrue(handler.passed());
    }

    private static boolean canConnect(String url) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.connect();
            urlConnection.disconnect();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    private static boolean lastDdmIsPublished() {
        File other = RECENT_SCHEMAS.get(new File(DDM.xsd).getName());
        String targetSchema = other.toString().replace("target/easy-schema/", "");

        String ddmSchema = DDM.xsd.replace("https://easy.dans.knaw.nl/schemas/", "");

        return targetSchema.equals(ddmSchema);
    }
}
