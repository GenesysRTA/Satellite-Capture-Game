package org.apache.maven.satellite_capture_game;

import java.io.File;
import java.io.IOException;

import javax.swing.JInternalFrame;
import javax.xml.stream.XMLStreamException;

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
	//private Ellipsoid satellite;
	private SatelliteModel satellite;
	private RenderableLayer layer;
	private ShapeAttributes attrs;
	private Position position;
	private File modelPath;
	
	public Satellite(double lat, double lon, double alt, WorldWindow wwd, boolean isSource) throws IOException, XMLStreamException {
		position = Position.fromDegrees(lat, lon, alt);
		//satellite = new Ellipsoid(position, 600000, 600000, 600000);
		if (isSource)
		{
			modelPath = new File("E:\\Licenta\\satellite-capture-game\\src\\main\\java\\untitled2.dae");
		}
		else
		{
			modelPath = new File("E:\\Licenta\\satellite-capture-game\\src\\main\\java\\untitled.dae");
		}
		satellite = new SatelliteModel(modelPath, position, wwd);
		satellite.run();
        layer = new RenderableLayer();
		attrs = new BasicShapeAttributes();
		
		//createSatellite(satellite, layer, attrs);
        
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
	
//	public Ellipsoid getShape() {
//		return this.satellite;
//	}
	
	public SatelliteModel getSatelliteShape() {
		return this.satellite;
	}
}