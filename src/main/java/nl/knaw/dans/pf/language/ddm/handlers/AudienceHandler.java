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
