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

import nl.knaw.dans.pf.language.ddm.handlertypes.BasicStringHandler;
import nl.knaw.dans.pf.language.emd.types.BasicString;
import org.xml.sax.SAXException;

import static org.apache.commons.lang.StringUtils.isBlank;

public class TermsSpatialIso3166Handler extends BasicStringHandler {

    @Override
    protected void finishElement(final String uri, final String localName) throws SAXException {
        BasicString basicString = createBasicString(uri, localName);

        if (basicString != null) {
            String newValue = convertIsoValue(basicString.getValue());
            basicString.setValue(newValue);

            // the uri check prevents creating an element for each level of
            // for example: <dcterms:spatial><Point><pos>
            if (!isBlank(uri))
                getTarget().getEmdCoverage().getTermsSpatial().add(basicString);
        }
    }

    private String convertIsoValue(String value) {
        String s = value.toUpperCase();
        if ("NLD".equals(s)) {
            return "Netherlands";
        } else {
            return value;
        }
    }
}
