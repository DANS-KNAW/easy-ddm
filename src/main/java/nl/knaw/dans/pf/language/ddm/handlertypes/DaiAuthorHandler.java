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

import java.net.URI;
import java.net.URISyntaxException;

import nl.knaw.dans.common.lang.id.DAI;
import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.emd.types.Author;
import nl.knaw.dans.pf.language.emd.types.EmdConstants;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkHandler;

import org.xml.sax.SAXException;

public abstract class DaiAuthorHandler extends CrosswalkHandler<EasyMetadata> {
    protected Author createDaiAuthor(final String uri, final String localName) throws SAXException {
        final String value = getCharsSinceStart().trim();
        final String attribute = getAttribute("", "DAI").trim();
        if (value.length() == 0 || attribute.length() == 0)
            return null;
        final Author author = new Author();
        author.setSurname(value);
        return setDAI(author, attribute);
    }

    Author setDAI(final Author author, final String value) throws SAXException {
        if (value.startsWith("info")) {
            final String[] strings = value.split("/");
            final String entityId = strings[strings.length - 1];
            final String idSys = value.replaceAll(entityId + "$", "");
            author.setEntityId(entityId, EmdConstants.SCHEME_DAI);
            author.setIdentificationSystem(toURI(idSys));
        } else {
            author.setEntityId(value, EmdConstants.SCHEME_DAI);
            author.setIdentificationSystem(toURI(DAI.DAI_NAMESPACE));
        }
        if (!DAI.isValid(author.getEntityId())) {
            error("invalid DAI " + author.getEntityId());
            return null;
        }
        return author;
    }

    private URI toURI(final String string) throws SAXException {
        try {
            return new URI(string);
        }
        catch (final URISyntaxException e) {
            error(e.getMessage());
            return null;
        }
    }
}
