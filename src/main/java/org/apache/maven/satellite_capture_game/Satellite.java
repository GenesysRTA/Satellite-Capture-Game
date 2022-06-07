package org.apache.maven.satellite_capture_game;
import java.io.File;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;

public class Satellite {
	private SatelliteModel satellite;
	private Position position;
	private File modelPath;

	public Satellite(double lat, double lon, double alt, WorldWindow wwd, boolean isSource) {
		position = Position.fromDegrees(lat, lon, alt);
		//satellite = new Ellipsoid(position, 600000, 600000, 600000);
		if (isSource)
		{
			modelPath = new File("E:\\Licenta\\satellite-capture-game\\src\\main\\java\\satellite_visible.dae");
		}
		else
		{
			modelPath = new File("E:\\Licenta\\satellite-capture-game\\src\\main\\java\\satellite_invisible.dae");
		}
		satellite = new SatelliteModel(modelPath, position, wwd);
		satellite.run();
	}
	
	public SatelliteModel getSatelliteShape() {
		return this.satellite;
	}
}