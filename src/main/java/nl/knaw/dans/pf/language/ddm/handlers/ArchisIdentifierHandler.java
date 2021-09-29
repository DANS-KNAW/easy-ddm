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
package nl.knaw.dans.pf.language.ddm.handlers;

import nl.knaw.dans.pf.language.emd.types.BasicIdentifier;
import org.xml.sax.SAXException;

import java.net.URI;
import java.net.URISyntaxException;

public class ArchisIdentifierHandler extends IdentifierHandler {

    public ArchisIdentifierHandler() {
        super("Archis_onderzoek_m_nr");
    }

    @Override
    protected void setScheme(BasicIdentifier identifier) throws SAXException {
        super.setScheme(identifier);

        identifier.setSchemeId("archaeology.dc.identifier");

        try {
            String value = identifier.getValue();
            if (value.length() < 10) {
                identifier.setIdentificationSystem(new URI("http://archis2.archis.nl"));
            } else if (value.length() == 10) {
                identifier.setIdentificationSystem(new URI("https://archis.cultureelerfgoed.nl"));
            } else {
                throw new SAXException("identifier '" + value + "' should have 10 or less characters");
            }
        }
        catch (URISyntaxException e) {
            throw new SAXException(e);
        }
    }
}
