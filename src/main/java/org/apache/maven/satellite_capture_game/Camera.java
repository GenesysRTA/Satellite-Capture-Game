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
	
	public static void initialConfigurations() {
		Configuration.setValue(AVKey.INITIAL_LATITUDE, 0.0);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, 0.0);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 12000000.0);
        Configuration.setValue(AVKey.INITIAL_PITCH, 50.0);
	}
}