package nl.knaw.dans.pf.language.ddm.handlers.spatial;

import nl.knaw.dans.pf.language.ddm.handlermaps.NameSpace;
import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.emd.types.Spatial;
import nl.knaw.dans.pf.language.emd.types.Spatial.Point;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SpatialPointHandler extends CrosswalkHandler<EasyMetadata> {

    public static final String EPSG_URL_WGS84 = "http://www.opengis.net/def/crs/EPSG/0/4326";
    public static final String EPSG_URN_WGS84 = "urn:ogc:def:crs:EPSG::4326";
    public static final String EPSG_URL_RD = "http://www.opengis.net/def/crs/EPSG/0/28992";
    public static final String EPSG_URN_RD = "urn:ogc:def:crs:EPSG::28992";
    public static final String EAS_SPATIAL_SCHEME_WGS84 = "degrees";// WGS84, but in EASY we call it 'degrees'
    public static final String EAS_SPATIAL_SCHEME_RD = "RD";
    public static final String EAS_SPATIAL_SCHEME_LOCAL = "local"; // some other system not known by EASY

    private static final String SRS_NAME = "srsName";
    private String description = null;
    private Point pos = null;

    /**
     * Proper processing requires pushing/popping and inheriting the attribute, so we skip for the current implementation
     */
    // the srs is the EPSG_URL_WGS84 by default
    private String foundSRS = EPSG_URL_WGS84;

    @Override
    protected void initFirstElement(String uri, String localName, Attributes attributes) {
        this.description = null;
        this.pos = null;
        foundSRS = EPSG_URL_WGS84;
        checkSRS(attributes);
    }

    @Override
    protected void initElement(String uri, String localName, Attributes attributes) {
        checkSRS(attributes);
    }

    @Override
    protected void finishElement(String uri, String localName) throws SAXException {
        if ("description".equals(localName))
            description = getCharsSinceStart().trim();
        else if ("pos".equals(localName))
            pos = createPoint();
        else if ("Point".equals(localName) && pos != null)
            getTarget().getEmdCoverage().getEasSpatial().add(new Spatial(description, pos));
    }

    private Point createPoint() throws SAXException {
        final String type = getAttribute(NameSpace.XSI.uri, "type");
        if (type != null)
            warning("ignored: not yet implemented");

        final String[] coordinates = getCharsSinceStart().trim().split(" ");
        if (coordinates.length < 2) {
            error("expected at least two coordinate numbers separated with a space");
            return null;
        }

        String easScheme = srsName2EasScheme(foundSRS);
        if (easScheme != null && easScheme.contentEquals("RD")) {
            // RD; coordinate order is east, north = x y
            return new Spatial.Point(easScheme, coordinates[0], coordinates[1]);
        } else {
            // WGS84, or at least the order is yx
            // http://wiki.esipfed.org/index.php/CRS_Specification
            // urn:ogc:def:crs:EPSG::4326 has coordinate order latitude(north), longitude(east) = y x
            // we make this the default order
            return new Spatial.Point(easScheme, coordinates[1], coordinates[0]);
        }
    }

    private void checkSRS(final Attributes attributes) {
        for (int i = 0; i < attributes.getLength(); i++) {
            if (attributes.getLocalName(i).equals(SRS_NAME)) {
                foundSRS = attributes.getValue(i);
                break;
            }
        }
    }

    /**
     * EASY now only supports schemes (for coordinate systems) 'RD' and 'degrees' (WGS84) in the EMD. The official EPSG codes are 28992 for RD in meters x,y and
     * 4326 for WGS84 in decimal degrees lat,lon
     * 
     * @param srsName
     * @return
     */
    private static String srsName2EasScheme(final String srsName) {
        if (srsName == null)
            return null;
        else if (srsName.contentEquals(EPSG_URL_RD) || srsName.contentEquals(EPSG_URN_RD))
            return EAS_SPATIAL_SCHEME_RD;
        else if (srsName.contentEquals(EPSG_URL_WGS84) || srsName.contentEquals(EPSG_URN_WGS84))
            return EAS_SPATIAL_SCHEME_WGS84;
        else
            return EAS_SPATIAL_SCHEME_LOCAL; // suggesting otherwise it could be 'global', but we can't map it to something else
    }

    public void setSRS(String foundSRS) {
        this.foundSRS = foundSRS;
    }
}
