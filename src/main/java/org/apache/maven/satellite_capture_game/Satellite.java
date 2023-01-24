package org.apache.maven.satellite_capture_game;

import java.io.File;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;

public class Satellite {
	
	// Variable used for storing the satellite model
	private SatelliteModel satObject;
	
	// Variable used for storing the satellite model
	private Position position;
	
	// Variable used for storing the model path file
	private File modelPath;

	// Constructor
	public Satellite(double lat, double lon, double alt, WorldWindow wwd, boolean isSource) {
		
		// Set the satellite position
		position = Position.fromDegrees(lat, lon, alt);
		
		// Check if the satellite is the one the player can control and if it is
		if (isSource) {
			
			// Set the source satellite model
			modelPath = VariablesUtils.getResourceFile("./src/main/java/satellite_source.dae");
		// If the satellite is the one the player has to get to
		} else {
			
			// Set the target satellite model
			modelPath = VariablesUtils.getResourceFile("./src/main/java/satellite_target.dae");
		}
		
		// Set the satellite model
		satObject = new SatelliteModel(modelPath, position, wwd, isSource);
		
		// Run the movement of the satellite on a different thread
		satObject.start();
	}
	
	// Getter - Satellite model
	public SatelliteModel getSatelliteShape() {
		return this.satObject;
	}
}
