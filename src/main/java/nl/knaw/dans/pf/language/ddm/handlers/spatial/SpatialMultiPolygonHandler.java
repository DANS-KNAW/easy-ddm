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
package nl.knaw.dans.pf.language.ddm.handlers.spatial;

import nl.knaw.dans.pf.language.emd.types.Polygon;
import nl.knaw.dans.pf.language.emd.types.Spatial;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public class SpatialMultiPolygonHandler extends AbstractSpatialHandler {

    private final SpatialPolygonHandler polygonHandler;

    public SpatialMultiPolygonHandler(SpatialPolygonHandler polygonHandler) {
        this.polygonHandler = polygonHandler;
    }

    private String multiSurfaceDescription = null;
    private List<Polygon> polygons = null;

    @Override
    protected void initFirstElement(String uri, String localName, Attributes attributes) {
        super.initFirstElement(uri, localName, attributes);

        this.multiSurfaceDescription = null;
        this.polygons = new ArrayList<Polygon>();
    }

    @Override
    protected void initElement(String uri, String localName, Attributes attributes) {
        super.initElement(uri, localName, attributes);

        if ("Polygon".equals(localName)) {
            // start of a multi-polygon
            // ask SpatialPolygonHandler to add the Polygon it found to the list using a callback
            this.polygonHandler.setMultiPolygonHandler(new Consumer<Polygon>() {

                @Override
                public void accept(Polygon polygon) {
                    SpatialMultiPolygonHandler.this.polygons.add(polygon);
                }
            });
        }
    }

    @Override
    protected void finishElement(String uri, String localName) throws SAXException {
        super.finishElement(uri, localName);

        // value extraction
        if ("name".equals(localName))
            multiSurfaceDescription = getCharsSinceStart().trim();
        else if ("MultiSurface".equals(localName))
            createAndAddSpatial(multiSurfaceDescription, polygons);
    }
}
