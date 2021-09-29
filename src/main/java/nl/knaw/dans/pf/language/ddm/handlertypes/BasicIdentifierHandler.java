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

import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.emd.types.BasicIdentifier;
import nl.knaw.dans.pf.language.emd.types.InvalidLanguageTokenException;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkHandler;

import org.xml.sax.SAXException;

public abstract class BasicIdentifierHandler extends CrosswalkHandler<EasyMetadata> {

    private final String scheme;

    public BasicIdentifierHandler() {
        this(null);
    }
    
    public BasicIdentifierHandler(String scheme) {
        super();
        this.scheme = scheme;
    }

    protected BasicIdentifier createIdentifier(final String uri, final String localName) throws SAXException {
        final String value = getCharsSinceStart().trim();
        if (value.length() != 0) {
            try {
                BasicIdentifier identifier = new BasicIdentifier(value.trim());
                if (scheme != null)
                    identifier.setScheme(scheme);
                return identifier;
            }
            catch (final InvalidLanguageTokenException e) {
                error(e.getMessage());
            }
        }
        return null;
    }

}
