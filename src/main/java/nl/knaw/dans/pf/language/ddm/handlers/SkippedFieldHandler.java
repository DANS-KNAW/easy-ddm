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

import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkHandler;

public class SkippedFieldHandler extends CrosswalkHandler<EasyMetadata> {
    private String warn;

    public SkippedFieldHandler(String string) {
        this.warn = string;
    }

    @Override
    protected void finishElement(final String uri, final String localName) throws SAXException {
        // in this case we might want qName of the endElement
        if (warn != null)
            warning("skipped " + uri + " " + localName + " [" + warn + "]");
    }
}
