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

import org.xml.sax.SAXException;

import nl.knaw.dans.pf.language.ddm.handlertypes.BasicStringHandler;
import nl.knaw.dans.pf.language.emd.types.BasicString;

public class DcLanguageHandler extends BasicStringHandler {
    @Override
    protected void finishElement(final String uri, final String localName) throws SAXException {
        BasicString basicString = createBasicString(uri, localName);
        if (basicString != null) {
            String emdLanguageCode = getEmdLanguageCode(basicString.getValue());
            if (emdLanguageCode != null) {
                basicString.setValue(emdLanguageCode);
                basicString.setScheme("ISO 639");
                basicString.setSchemeId("common.dc.language");
            }
            getTarget().getEmdLanguage().getDcLanguage().add(basicString);
        }
    }

    private String getEmdLanguageCode(final String language) {
        String code = null;

        if ("dut".equals(language) || "nld".equals(language))
            code = "dut/nld";
        else if ("deu".equals(language) || "ger".equals(language))
            code = "ger/deu";
        else if ("fra".equals(language) || "fre".equals(language))
            code = "fre/fra";
        else if ("eng".equals(language))
            code = "eng";

        return code;
    }
}
