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

import nl.knaw.dans.pf.language.emd.types.PolygonPart;
import nl.knaw.dans.pf.language.emd.types.PolygonPoint;
import nl.knaw.dans.pf.language.emd.types.Polygon;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

import static nl.knaw.dans.pf.language.ddm.handlers.spatial.PolygonParsingState.END_EXTERIOR;
import static nl.knaw.dans.pf.language.ddm.handlers.spatial.PolygonParsingState.END_INTERIOR;
import static nl.knaw.dans.pf.language.ddm.handlers.spatial.PolygonParsingState.END_POLYGON;
import static nl.knaw.dans.pf.language.ddm.handlers.spatial.PolygonParsingState.EXTERIOR;
import static nl.knaw.dans.pf.language.ddm.handlers.spatial.PolygonParsingState.E_DESCR;
import static nl.knaw.dans.pf.language.ddm.handlers.spatial.PolygonParsingState.E_POSLIST;
import static nl.knaw.dans.pf.language.ddm.handlers.spatial.PolygonParsingState.INTERIOR;
import static nl.knaw.dans.pf.language.ddm.handlers.spatial.PolygonParsingState.I_DESCR;
import static nl.knaw.dans.pf.language.ddm.handlers.spatial.PolygonParsingState.I_POSLIST;
import static nl.knaw.dans.pf.language.ddm.handlers.spatial.PolygonParsingState.P_DESCR;

public class SpatialPolygonHandler extends AbstractSpatialHandler {

    private PolygonParsingState state = null;
    private String polygonDescription = null;
    private String exteriorDescription = null;
    private List<PolygonPoint> exteriorPoints = null;
    private PolygonPart exteriorPart = null;
    private String interiorDescription = null;
    private List<PolygonPoint> interiorPoints = null;
    private List<PolygonPart> interiorParts = null;

    private Consumer<Polygon> multiPolygonCallback = null;

    void setMultiPolygonHandler(Consumer<Polygon> callback) {
        multiPolygonCallback = callback;
    }

    @Override
    protected void initFirstElement(final String uri, final String localName, final Attributes attributes) {
        super.initFirstElement(uri, localName, attributes);

        state = null;
        polygonDescription = exteriorDescription = interiorDescription = null;
        exteriorPoints = null;
        exteriorPart = null;
        interiorPoints = null;
        interiorParts = new ArrayList<PolygonPart>();
    }

    @Override
    protected void initElement(final String uri, final String localName, final Attributes attributes) {
        super.initElement(uri, localName, attributes);

        if ("description".equals(localName) && state == null)
            this.state = P_DESCR;
        else if ("description".equals(localName) && (state == EXTERIOR || state == INTERIOR))
            state = state.getNextState();
        else if ("exterior".equals(localName) && state == P_DESCR)
            state = state.getNextState();
        else if ("exterior".equals(localName) && state == null)
            // no description in the polygon
            state = P_DESCR.getNextState();
        else if ("posList".equals(localName) && (state == E_DESCR || state == I_DESCR))
            state = state.getNextState();
        else if ("posList".equals(localName) && state == EXTERIOR)
            state = E_DESCR.getNextState();
        else if ("posList".equals(localName) && state == INTERIOR)
            state = I_DESCR.getNextState();
        else if ("interior".equals(localName))
            state = INTERIOR;
    }

    @Override
    protected void finishElement(final String uri, final String localName) throws SAXException {
        super.finishElement(uri, localName);

        if ("description".equals(localName) && state == P_DESCR)
            polygonDescription = getCharsSinceStart().trim();
        else if ("description".equals(localName) && state == E_DESCR)
            exteriorDescription = getCharsSinceStart().trim();
        else if ("description".equals(localName) && state == I_DESCR)
            interiorDescription = getCharsSinceStart().trim();
        else if ("posList".equals(localName) && state == E_POSLIST) {
            exteriorPoints = createPolygonPoints();
            state = state.getNextState();
        } else if ("posList".equals(localName) && state == I_POSLIST) {
            interiorPoints = createPolygonPoints();
            state = state.getNextState();
        } else if ("exterior".equals(localName) && state == END_EXTERIOR) {
            exteriorPart = new PolygonPart(exteriorDescription, exteriorPoints);
            state = state.getNextState();
        } else if ("interior".equals(localName) && state == END_INTERIOR) {
            interiorParts.add(new PolygonPart(interiorDescription, interiorPoints));
            state = state.getNextState();
        } else if ("Polygon".equals(localName)) {
            Polygon polygon = new Polygon(srsName2EasScheme(getFoundSRS()), polygonDescription, exteriorPart, interiorParts);

            if (multiPolygonCallback == null)
                createAndAddSpatial(polygon);
            else
                multiPolygonCallback.accept(polygon);

            // state transition
            if (state == END_POLYGON)
                state = state.getNextState();
            else if (state == INTERIOR)
                // no interior(s) found
                state = END_POLYGON.getNextState();
        }
        // other types than point/box/polygon(s) not supported by EMD: don't warn
    }
}

/**
 * Parsing a polygon with the current parser is quite complex. A state machine helps with this. Read these states from bottom to top as [state]([next-state])
 */
enum PolygonParsingState {
    // @formatter:off
    END_POLYGON,
    END_INTERIOR(END_POLYGON),
    I_POSLIST(END_INTERIOR),
    I_DESCR(I_POSLIST),
    INTERIOR(I_DESCR),
    END_EXTERIOR(INTERIOR),
    E_POSLIST(END_EXTERIOR),
    E_DESCR(E_POSLIST),
    EXTERIOR(E_DESCR),
    P_DESCR(EXTERIOR);
    // @formatter:on

    private PolygonParsingState nextState;

    PolygonParsingState(PolygonParsingState nextState) {
        this.nextState = nextState;
    }

    PolygonParsingState() {
        this(null);
    }

    public PolygonParsingState getNextState() {
        return this.nextState;
    }
}
