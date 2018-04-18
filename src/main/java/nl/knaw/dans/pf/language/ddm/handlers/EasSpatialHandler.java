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
package nl.knaw.dans.pf.language.ddm.handlers;

import nl.knaw.dans.pf.language.ddm.handlers.spatial.SpatialBoxHandler;
import nl.knaw.dans.pf.language.ddm.handlers.spatial.SpatialPointHandler;
import nl.knaw.dans.pf.language.ddm.handlers.spatial.SpatialPolygonHandler;
import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.xml.crosswalk.CrosswalkHandler;
import org.xml.sax.Attributes;

public class EasSpatialHandler extends CrosswalkHandler<EasyMetadata> {

    private final SpatialPointHandler pointHandler;
    private final SpatialBoxHandler boxHandler;
    private final SpatialPolygonHandler polygonHandler;

    public EasSpatialHandler(SpatialPointHandler pointHandler, SpatialBoxHandler boxHandler, SpatialPolygonHandler polygonHandler) {
        this.pointHandler = pointHandler;
        this.boxHandler = boxHandler;
        this.polygonHandler = polygonHandler;
    }

    public static final String EPSG_URL_WGS84 = "http://www.opengis.net/def/crs/EPSG/0/4326";
    public static final String EAS_SPATIAL_SCHEME_WGS84 = "degrees";// WGS84, but in EASY we call it 'degrees'
    public static final String EAS_SPATIAL_SCHEME_RD = "RD";

    private static final String SRS_NAME = "srsName";

    /**
     * Proper processing requires pushing/popping and inheriting the attribute, so we skip for the current implementation
     */
    // the srs is the EPSG_URL_WGS84 by default
    private String foundSRS = EPSG_URL_WGS84;

    @Override
    protected void initFirstElement(final String uri, final String localName, final Attributes attributes) {
        foundSRS = EPSG_URL_WGS84;
        checkSRS(attributes);
    }

    @Override
    protected void initElement(final String uri, final String localName, final Attributes attributes) {
        checkSRS(attributes);
        if ("Point".equals(localName)) {
            this.pointHandler.setSRS(foundSRS);
        } else if ("Envelope".equals(localName)) {
            this.boxHandler.setSRS(foundSRS);
        } else if ("Polygon".equals(localName)) {
            this.polygonHandler.setSRS(foundSRS);
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
}
