package nl.knaw.dans.pf.language.ddm.handlers.spatial;

import nl.knaw.dans.pf.language.ddm.handlermaps.NameSpace;
import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.emd.types.Polygon;
import nl.knaw.dans.pf.language.emd.types.PolygonPoint;
import nl.knaw.dans.pf.language.emd.types.Spatial;
import nl.knaw.dans.pf.language.emd.types.Spatial.Box;
import nl.knaw.dans.pf.language.emd.types.Spatial.Point;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSpatialHandler extends CrosswalkHandler<EasyMetadata> {

    private static final String EPSG_URL_WGS84 = "http://www.opengis.net/def/crs/EPSG/0/4326";
    private static final String EPSG_URN_WGS84 = "urn:ogc:def:crs:EPSG::4326";
    private static final String EPSG_URL_RD = "http://www.opengis.net/def/crs/EPSG/0/28992";
    private static final String EPSG_URN_RD = "urn:ogc:def:crs:EPSG::28992";
    public static final String EAS_SPATIAL_SCHEME_WGS84 = "degrees";// WGS84, but in EASY we call it 'degrees'
    public static final String EAS_SPATIAL_SCHEME_RD = "RD";
    private static final String EAS_SPATIAL_SCHEME_LOCAL = "local"; // some other system not known by EASY

    private static final String SRS_NAME = "srsName";

    /**
     * Proper processing requires pushing/popping and inheriting the attribute, so we skip for the current implementation
     */
    // the srs is the EPSG_URL_WGS84 by default
    private String foundSRS = EPSG_URL_WGS84;

    private void checkSRS(Attributes attributes) {
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
    static String srsName2EasScheme(String srsName) {
        if (srsName == null)
            return null;
        else if (srsName.contentEquals(EPSG_URL_RD) || srsName.contentEquals(EPSG_URN_RD))
            return EAS_SPATIAL_SCHEME_RD;
        else if (srsName.contentEquals(EPSG_URL_WGS84) || srsName.contentEquals(EPSG_URN_WGS84))
            return EAS_SPATIAL_SCHEME_WGS84;
        else
            return EAS_SPATIAL_SCHEME_LOCAL; // suggesting otherwise it could be 'global', but we can't map it to something else
    }

    protected String getFoundSRS() {
        return this.foundSRS;
    }

    public void setFoundSRS(String foundSRS) {
        this.foundSRS = foundSRS;
    }

    @Override
    protected void initFirstElement(String uri, String localName, Attributes attributes) {
        foundSRS = EPSG_URL_WGS84;
        checkSRS(attributes);
    }

    @Override
    protected void initElement(String uri, String localName, Attributes attributes) {
        checkSRS(attributes);
    }

    void createAndAddSpatial(String description, Point pos) {
        addSpatial(new Spatial(description, pos));
    }

    void createAndAddSpatial(String description, Point upper, Point lower) {
        addSpatial(new Spatial(description, createBox(upper, lower)));
    }

    void createAndAddSpatial(Polygon polygon) {
        addSpatial(new Spatial(null, polygon));
    }

    void createAndAddSpatial(String description, List<Polygon> polygons) {
        addSpatial(new Spatial(description, polygons));
    }

    private void addSpatial(Spatial spatial) {
        getTarget().getEmdCoverage().getEasSpatial().add(spatial);
    }

    Point createPoint() throws SAXException {
        String type = getAttribute(NameSpace.XSI.uri, "type");
        if (type != null)
            warning("ignored: not yet implemented");

        String[] coordinates = getCharsSinceStart().trim().split("\\s+");
        if (coordinates.length < 2) {
            error("expected at least two coordinate numbers separated with a space");
            return null;
        }

        String easScheme = srsName2EasScheme(getFoundSRS());
        if (easScheme != null && easScheme.contentEquals("RD")) {
            // RD; coordinate order is east, north = x y
            return new Point(easScheme, coordinates[0], coordinates[1]);
        } else {
            // WGS84, or at least the order is yx
            // http://wiki.esipfed.org/index.php/CRS_Specification
            // urn:ogc:def:crs:EPSG::4326 has coordinate order latitude(north), longitude(east) = y x
            // we make this the default order
            return new Point(easScheme, coordinates[1], coordinates[0]);
        }
    }

    private Box createBox(Point upper, Point lower) {
        String easScheme = srsName2EasScheme(getFoundSRS());
        float upperY = Float.parseFloat(upper.getY());
        float upperX = Float.parseFloat(upper.getX());
        float lowerY = Float.parseFloat(lower.getY());
        float lowerX = Float.parseFloat(lower.getX());
        String n = "" + (upperY > lowerY ? upperY : lowerY);
        String s = "" + (upperY < lowerY ? upperY : lowerY);
        String e = "" + (upperX > lowerX ? upperX : lowerX);
        String w = "" + (upperX < lowerX ? upperX : lowerX);
        return new Box(easScheme, n, e, s, w);
    }

    List<PolygonPoint> createPolygonPoints() throws SAXException {
        String[] coordinates = getCharsSinceStart().trim().split("\\s+");
        int length = coordinates.length;
        if (length < 8) {
            error("expected at least 4 coordinate pairs to construct at least a triangle");
            return null;
        } else if (length % 2 == 1) {
            error("expected an even number of coordinates since they're taken in pairs of two");
            return null;
        } else if (!coordinates[0].equals(coordinates[length - 2]) && !coordinates[1].equals(coordinates[length - 1])) {
            error("first pair of coordinates should equal the last pair of coordinates");
            return null;
        }

        String easScheme = srsName2EasScheme(getFoundSRS());
        boolean isRD = easScheme != null && easScheme.contentEquals("RD");
        List<PolygonPoint> result = new ArrayList<PolygonPoint>(length / 2);
        for (int i = 0; i < length; i += 2) {
            String x = coordinates[i];
            String y = coordinates[i + 1];

            if (isRD)
                result.add(new PolygonPoint(y, x));
            else
                result.add(new PolygonPoint(x, y));
        }
        return result;
    }
}
