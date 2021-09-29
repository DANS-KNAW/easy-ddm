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

import nl.knaw.dans.pf.language.ddm.handlermaps.AudienceFormatMap;
import nl.knaw.dans.pf.language.ddm.handlertypes.BasicStringHandler;
import nl.knaw.dans.pf.language.emd.EasyMetadataImpl;
import nl.knaw.dans.pf.language.emd.types.ApplicationSpecific.MetadataFormat;
import nl.knaw.dans.pf.language.emd.types.BasicString;
import nl.knaw.dans.pf.language.emd.types.EmdConstants;
import org.xml.sax.SAXException;

import java.util.List;
import java.util.Map;

public class AudienceHandler extends BasicStringHandler {
    public AudienceHandler() {}

    public AudienceHandler(final Map<String, String> vocabulary) {
        super(vocabulary, EmdConstants.SCHEME_ID_DISCIPLINES);
    }

    @Override
    protected void finishElement(final String uri, final String localName) throws SAXException {
        final BasicString basicString = createBasicString(uri, localName);
        if (basicString == null)
            return;
        final List<BasicString> audienceList = getTarget().getEmdAudience().getTermsAudience();
        audienceList.add(basicString);

        // EMD-format based on the first audience
        if (audienceList.size() == 1) {
            final MetadataFormat format = AudienceFormatMap.get(basicString);
            final EasyMetadataImpl emd = (EasyMetadataImpl) getTarget();
            emd.getEmdOther().getEasApplicationSpecific().setMetadataFormat(format);
        }
    }
}
