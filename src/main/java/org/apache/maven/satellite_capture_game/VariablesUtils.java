package org.apache.maven.satellite_capture_game;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariablesUtils {
	
	// Variable storing the name of the player
	private static String name;
	// Variable storing the force applied to the satellite
	private static double force;
	// Variable storing the angle
	private static double angle;
	
	// Setter - Name
	public static void setName(String name) {
		VariablesUtils.name = name;
	}
	
	// Getter - Name
	public static String getName() {
		return VariablesUtils.name;
	}
	
	// Setter - Force
	public static void setForce(double force) {
		VariablesUtils.force = force;
	}
	
	// Getter - Force
	public static double getForce() {
		return VariablesUtils.force;
	}
	
	// Setter - Angle
	public static void setAngle(double angle) {
		VariablesUtils.angle = angle;
	}
	
	// Getter - Angle
	public static double getAngle() {
		return VariablesUtils.angle;
	}
	
	// Method used for dealing with files
	public static File getResourceFile(final String name) {
		try {
            final String className = "/" + Propagate.class.getName().replaceAll("\\.", "/") + ".class";
            final Pattern pattern = Pattern.compile("jar:file:([^!]+)!" + className + "$");
            final Matcher matcher = pattern.matcher(Propagate.class.getResource(className).toURI().toString());
            if (matcher.matches()) {
                final File resourceFile = new File(new File(matcher.group(1)).getParentFile(), name);
                if (resourceFile.exists()) {
                    return resourceFile;
                }
            }
            
            final URL resourceURL = Propagate.class.getResource(name);
            if (resourceURL != null) {
                return new File(resourceURL.toURI().getPath());
            }
            
            final File f = new File(name);
            if (f.exists()) {
                return f;
            }
            throw new IOException("Unable to find file");
        } catch (URISyntaxException use) {
        	Logger logger = Logger.getLogger(UI.class.getName());
        	logger.log(Level.SEVERE, "Time delay error: \n", use);
        } catch (IOException e) {
        	Logger logger = Logger.getLogger(UI.class.getName());
        	logger.log(Level.SEVERE, "Time delay error: \n", e);
        }
		return null;
	}
}
