package org.apache.maven.satellite_capture_game;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Ellipsoid;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;

public class Satellite {
	private Ellipsoid satellite;
	private RenderableLayer layer;
	private ShapeAttributes attrs;
	private Position position;
	
	public Satellite(WorldWindow wwd) {
		position = Position.fromDegrees(0.0, 0.0, 5000000.0);
		satellite = new Ellipsoid(position, 60000, 60000, 60000);
        layer = new RenderableLayer();
		attrs = new BasicShapeAttributes();
		
		createSatellite(satellite, layer, attrs);
        
        UI.insertBeforePlacenames(wwd, layer);
	}
	
	public void createSatellite(Ellipsoid satellite, RenderableLayer layer, ShapeAttributes attrs) {
		attrs.setInteriorMaterial(Material.YELLOW);
        attrs.setInteriorOpacity(1);
        attrs.setEnableLighting(true);
        attrs.setOutlineWidth(2d);
        attrs.setDrawInterior(true);
        attrs.setDrawOutline(false);
        
        satellite.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        satellite.setAttributes(attrs);
        satellite.setValue(AVKey.DISPLAY_NAME, "Satellite");
        
        layer.addRenderable(satellite);
	}
}