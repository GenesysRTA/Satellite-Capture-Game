package org.apache.maven.satellite_capture_game;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;

public class Camera {
	private static Camera cam = null;
	
	private Camera() {
		
	}
	
	public static Camera getCamera() {
        if (cam == null) {
        	cam = new Camera();
        }
 
        return cam;
    }
	
	public static void initialConfigurations(double lat, double lon) {
		Configuration.setValue(AVKey.INITIAL_LATITUDE, lat);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, lon);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 75000000.0);
        Configuration.setValue(AVKey.INITIAL_PITCH, 10.0);
	}
}