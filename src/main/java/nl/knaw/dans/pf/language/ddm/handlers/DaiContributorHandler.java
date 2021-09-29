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

import nl.knaw.dans.pf.language.ddm.handlertypes.DaiAuthorHandler;
import nl.knaw.dans.pf.language.emd.types.Author;

import org.xml.sax.SAXException;

public class DaiContributorHandler extends DaiAuthorHandler {
    @Override
    protected void finishElement(final String uri, final String localName) throws SAXException {
        final Author author = createDaiAuthor(uri, localName);
        if (author != null)
            getTarget().getEmdContributor().getEasContributor().add(author);
    }
}
