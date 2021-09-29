/*
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
package nl.knaw.dans.pf.language.nl.knaw.dans.pf.language.ddm.handlermaps;

import nl.knaw.dans.pf.language.ddm.handlermaps.NameSpace;
import org.junit.Test;

import java.io.File;

import static nl.knaw.dans.pf.language.ddm.api.SpecialValidator.LOCAL_SCHEMA_DIR;
import static nl.knaw.dans.pf.language.ddm.api.SpecialValidator.RECENT_SCHEMAS;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class NameSpaceTest {
    @Test
    public void usesLatestVersions() throws Exception {
        for (NameSpace ns : NameSpace.values()) {
            if (ns.xsd != null && ns.xsd.contains("easy.dans.knaw.nl/schemas")) {
                String name = new File(ns.xsd).getName();
                String localPath = RECENT_SCHEMAS.get(name).toString();
                String expectedRelativePath = localPath.replace(LOCAL_SCHEMA_DIR, "");
                String implementedRelativePath = ns.xsd.replace("http://easy.dans.knaw.nl/schemas/", "");
                implementedRelativePath = implementedRelativePath.replace("https://easy.dans.knaw.nl/schemas/", "");
                assertThat("latest xsd for NameSpace " + ns, implementedRelativePath, is(expectedRelativePath));
            }
        }
    }
}
