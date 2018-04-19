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

import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.emd.binding.EmdMarshaller;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkException;
import nl.knaw.dans.pf.language.xml.exc.XMLSerializationException;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/** Without validation, so pure crosswalk tests that execute without web access. */
@RunWith(Parameterized.class)
public class Ddm2EmdCrosswalkTest {

    private static File testFiles = null;

    @BeforeClass
    public static void beforeAll() throws URISyntaxException {
        testFiles = new File(Ddm2EmdCrosswalkTest.class.getResource("/ddm2emdCrosswalk").toURI());
    }

    @Parameters
    public static Collection<Object[]> data() {
        // @formatter:off
        return Arrays.asList(new Object[][] {
            { "alternativeTitle" },
            { "dcxDaiAuthor" },
            { "dcxIsniAuthor" },
            { "ddmAccessRight" },
            { "ddmDescriptionWithRequiredDescriptionType" },
            { "ddmSubject" },
            { "ddmTemporal" },
            { "formatWithSchemeAndId" },
            { "identifierWithIdTypeEDNA" },
            { "identifierWithIdTypeISBN" },
            { "identifierWithIdTypeISSN" },
            { "identifierWithIdTypeNewArchis" },
            { "identifierWithIdTypeNwoProjectNummer" },
            { "identifierWithIdTypeOldArchis" },
            { "identifierWithoutIdType" },
            { "languageWithSchemeAndIdButNoEmdCode" },
            { "languageWithSchemeAndIdToEmdCode" },
            { "license" },
            { "normalRelation" },
            { "spatialBox" },
            { "spatialGmlEnvelope" },
            { "spatialGmlPoints" },
            { "spatialISO3166" },
            { "spatialPoint" },
            { "spatialPolygonRD" },
            { "spatialPolygonWGS84" },
            { "spatialPolygonWithOnlyDescriptionInExternal" },
            { "spatialPolygonWithoutDescriptions" },
            { "spatialPolygonWithoutDescriptionsOrInternals" },
            { "spatialPolygonWithoutInternals" },
            { "streamingSurrogateRelation" },
            { "subjectABR" },
            { "subjectPlainText" },
            { "temporalABR" },
            { "temporalPlainText" },
            { "typeWithSchemeAndId" },
        });
        // @formatter:on
    }

    private String testName;

    public Ddm2EmdCrosswalkTest(String testName) {
        this.testName = testName;
    }

    @Test
    public void ddm2Emd() throws Exception {
        File ddmFile = new File(testFiles, String.format("%s.input.xml", this.testName));
        File emdFile = new File(testFiles, String.format("%s.output.xml", this.testName));

        assertTrue(ddmFile.exists());
        assertTrue(emdFile.exists());

        String ddm = FileUtils.readFileToString(ddmFile);
        String actualEmd = normalize(FileUtils.readFileToString(emdFile));
        String expectedEmd = normalize(emdElementFrom(ddm));

        assertThat(expectedEmd, is(actualEmd));
    }

    private String emdElementFrom(String ddm) throws CrosswalkException, XMLSerializationException {
        EasyMetadata emd = new Ddm2EmdCrosswalk(null).createFrom(ddm);
        return new EmdMarshaller(emd).getXmlString();
    }

    private String normalize(String s) {
        return s.replaceAll("\n", "");
    }
}
