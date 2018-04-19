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

        if ("name".equals(localName)) {
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
            getTarget().getEmdCoverage().getEasSpatial().add(new Spatial(multiSurfaceDescription, polygons));
    }
}
