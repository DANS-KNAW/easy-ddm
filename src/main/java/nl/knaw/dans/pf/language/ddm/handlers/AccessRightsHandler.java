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

import java.util.List;

import nl.knaw.dans.pf.language.ddm.handlertypes.BasicStringHandler;
import nl.knaw.dans.pf.language.emd.types.BasicString;

import org.xml.sax.SAXException;

public class AccessRightsHandler extends BasicStringHandler {
    @Override
    public final void finishElement(final String uri, final String localName) throws SAXException {
        final List<BasicString> accessRightsList = getTarget().getEmdRights().getTermsAccessRights();
        final BasicString basicString = createBasicString(uri, localName);
        basicString.setSchemeId("common.dcterms.accessrights");
        // skip access rights beyond the ddm:profile
        if (accessRightsList.size() <= 0 && basicString != null)
            accessRightsList.add(basicString);
    }
}
