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
package nl.knaw.dans.pf.language.ddm.handlertypes;

import nl.knaw.dans.pf.language.emd.types.Author;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public abstract class AuthorDetailsHandler extends DaiAuthorHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthorDetailsHandler.class);
    protected Author author;

    @Override
    public void finishElement(final String uri, final String localName) throws SAXException {
        if ("author".equals(localName))
            logger.debug("collected: " + author.toString());
        else {
            final String value = getCharsSinceStart().trim();
            if (value.length() == 0)
                return;
            if ("initials".equals(localName))
                author.setInitials(value);
            else if ("surname".equals(localName))
                author.setSurname(value);
            else if ("titles".equals(localName))
                author.setTitle(value);
            else if ("name".equals(localName)/* part of organization */)
                author.setOrganization(value);
            else if ("DAI".equals(localName))
                setDAI(author, value); // TODO dai system as attribute?
            else if ("insertions".equals(localName))
                author.setPrefix(value);
            else if ("role".equals(localName))
                author.setRole(new Author.Role(value));
        }
    }
}
