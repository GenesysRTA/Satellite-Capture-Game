package org.apache.maven.satellite_capture_game;

import java.io.File;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;

public class Satellite {
	private SatelliteModel satObject;
	private Position position;
	private File modelPath;

	public Satellite(double lat, double lon, double alt, WorldWindow wwd, boolean isSource) {
		position = Position.fromDegrees(lat, lon, alt);
		if (isSource) {
			modelPath = VariablesUtils.getResourceFile("./src/main/java/satellite_source.dae");
		} else {
			modelPath = VariablesUtils.getResourceFile("./src/main/java/satellite_target.dae");
		}
		satObject = new SatelliteModel(modelPath, position, wwd, isSource);
		satObject.run();
	}
	
	public SatelliteModel getSatelliteShape() {
		return this.satObject;
	}
}
