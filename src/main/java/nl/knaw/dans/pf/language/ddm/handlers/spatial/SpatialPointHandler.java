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
import nl.knaw.dans.pf.language.emd.types.Spatial;
import nl.knaw.dans.pf.language.emd.types.Spatial.Point;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SpatialPointHandler extends AbstractSpatialHandler {

    private String description = null;
    private Point pos = null;

    @Override
    protected void initFirstElement(String uri, String localName, Attributes attributes) {
      super.initFirstElement(uri, localName, attributes);

      this.description = null;
        this.pos = null;
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

        final String[] coordinates = getCharsSinceStart().trim().split("\\s+");
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
}
