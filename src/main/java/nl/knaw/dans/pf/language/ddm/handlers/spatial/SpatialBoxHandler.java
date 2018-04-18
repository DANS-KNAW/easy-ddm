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
package nl.knaw.dans.pf.language.ddm.handlers.spatial;

import nl.knaw.dans.pf.language.ddm.handlermaps.NameSpace;
import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.emd.types.Spatial;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SpatialBoxHandler extends CrosswalkHandler<EasyMetadata> {

    public static final String EPSG_URL_WGS84 = "http://www.opengis.net/def/crs/EPSG/0/4326";
    public static final String EPSG_URN_WGS84 = "urn:ogc:def:crs:EPSG::4326";
    public static final String EPSG_URL_RD = "http://www.opengis.net/def/crs/EPSG/0/28992";
    public static final String EPSG_URN_RD = "urn:ogc:def:crs:EPSG::28992";
    public static final String EAS_SPATIAL_SCHEME_WGS84 = "degrees";// WGS84, but in EASY we call it 'degrees'
    public static final String EAS_SPATIAL_SCHEME_RD = "RD";
    public static final String EAS_SPATIAL_SCHEME_LOCAL = "local"; // some other system not known by EASY

    private static final String SRS_NAME = "srsName";
    private String description = null;
    private Spatial.Point lower, upper = null;

    /**
     * Proper processing requires pushing/popping and inheriting the attribute, so we skip for the current implementation
     */
    // the srs is the EPSG_URL_WGS84 by default
    private String foundSRS = EPSG_URL_WGS84;

    @Override
    protected void initFirstElement(final String uri, final String localName, final Attributes attributes) {
        description = null;
        lower = upper = null;
        foundSRS = EPSG_URL_WGS84;
        checkSRS(attributes);
    }

    @Override
    protected void initElement(final String uri, final String localName, final Attributes attributes) {
        checkSRS(attributes);
    }

    private void checkSRS(final Attributes attributes) {
        for (int i = 0; i < attributes.getLength(); i++) {
            if (attributes.getLocalName(i).equals(SRS_NAME)) {
                foundSRS = attributes.getValue(i);
                break;
            }
        }
    }

    @Override
    protected void finishElement(final String uri, final String localName) throws SAXException {
        if ("description".equals(localName)) {
            description = getCharsSinceStart().trim();
        } else if ("lowerCorner".equals(localName))
            lower = createPoint();
        else if ("upperCorner".equals(localName))
            upper = createPoint();
        else if ("Envelope".equals(localName) && lower != null && upper != null)
            getTarget().getEmdCoverage().getEasSpatial().add(new Spatial(description, createBox()));
    }

    private Spatial.Box createBox() throws SAXException {
        final float upperY = Float.parseFloat(upper.getY());
        final float upperX = Float.parseFloat(upper.getX());
        final float lowerY = Float.parseFloat(lower.getY());
        final float lowerX = Float.parseFloat(lower.getX());
        final String n = "" + (upperY > lowerY ? upperY : lowerY);
        final String s = "" + (upperY < lowerY ? upperY : lowerY);
        final String e = "" + (upperX > lowerX ? upperX : lowerX);
        final String w = "" + (upperX < lowerX ? upperX : lowerX);
        return new Spatial.Box(srsName2EasScheme(foundSRS), n, e, s, w);
    }

    private Spatial.Point createPoint() throws SAXException {
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
