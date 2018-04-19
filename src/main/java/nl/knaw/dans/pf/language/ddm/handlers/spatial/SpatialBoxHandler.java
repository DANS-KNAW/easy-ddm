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

import nl.knaw.dans.pf.language.emd.types.Spatial.Point;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SpatialBoxHandler extends AbstractSpatialHandler {

    private String description = null;
    private Point lower, upper = null;

    @Override
    protected void initFirstElement(String uri, String localName, Attributes attributes) {
        super.initFirstElement(uri, localName, attributes);

        description = null;
        lower = upper = null;
    }

    @Override
    protected void finishElement(String uri, String localName) throws SAXException {
        super.finishElement(uri, localName);

        if ("description".equals(localName))
            description = getCharsSinceStart().trim();
        else if ("lowerCorner".equals(localName))
            lower = createPoint();
        else if ("upperCorner".equals(localName))
            upper = createPoint();
        else if ("Envelope".equals(localName) && lower != null && upper != null)
            createAndAddSpatial(description, upper, lower);
    }
}
