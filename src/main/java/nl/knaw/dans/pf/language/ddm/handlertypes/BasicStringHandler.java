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
package nl.knaw.dans.pf.language.ddm.handlertypes;

import java.util.Map;

import javax.xml.XMLConstants;

import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.emd.types.BasicString;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkHandler;

import org.xml.sax.SAXException;

public abstract class BasicStringHandler extends CrosswalkHandler<EasyMetadata> {
    private final Map<String, String> vocabulary;
    private final String schemeId;

    public BasicStringHandler(final Map<String, String> vocabulary, final String schemeId) {
        this.vocabulary = vocabulary;
        this.schemeId = schemeId;
    }

    public BasicStringHandler() {
        this.vocabulary = null;
        this.schemeId = null;
    }

    protected BasicString createBasicString(final String uri, final String localName) throws SAXException {
        final String value = getCharsSinceStart().trim();
        if (value.length() == 0)
            return null;
        final BasicString basicString = new BasicString();
        final String language = getAttribute(XMLConstants.XML_NS_URI, "lang");
        if (language != null)
            basicString.setLanguage(language);
        if (schemeId != null)
            basicString.setSchemeId(schemeId);
        if (vocabulary == null)
            basicString.setValue(value);
        else {
            try {
                basicString.setValue(vocabulary.get(value));
            }
            catch (final IllegalArgumentException e) {
                error(e.getMessage());
            }
        }
        return basicString;
    }
}
