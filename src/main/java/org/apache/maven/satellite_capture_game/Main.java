package org.apache.maven.satellite_capture_game;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.forces.gravity.potential.GravityFieldFactory;
import org.orekit.forces.gravity.potential.UnnormalizedSphericalHarmonicsProvider;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;

public class Main {
    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
    	
    	// Variable used for storing the seed
    	Seed seed = new Seed();
    	
    	// Variable used for generating the inclination
        final double i = seed.getI();
        
        // Variable used for generating all the UI elements
        UI ui = new UI(args);
        
        // Variable used for storing the minimum distance between the source satellite and the target satellite at any time
        double minDistance = Double.MAX_VALUE;
        
        // Variable used to get data from the file
        final File orekitData = VariablesUtils.getResourceFile("./data/orekit-data");
		DataProvidersManager.getInstance().addProvider(new DirectoryCrawler(orekitData));
		
		// Variable used to declare what timescale is used
		final TimeScale timeScale = TimeScalesFactory.getUTC();
		
		// Variables for setting the degree and order of the orbit
		final int degree = 12;
        final int order = 12;
        
        // Variable used for storing unnormalized spherical harmonics coefficients
        UnnormalizedSphericalHarmonicsProvider unnormalized = GravityFieldFactory.getConstantUnnormalizedProvider(degree, order);
        
        // Variable used for storing the standard gravitational parameter
        final double mu = unnormalized.getMu();
        
        // Variables used for storing using the frames udes by Orekit
        final Frame frame = FramesFactory.getEME2000();
        final Frame earthFrame = FramesFactory.getITRF(IERSConventions.IERS_2010, true);
        
        // Variables used for creating Earth object
        final OneAxisEllipsoid earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS, Constants.WGS84_EARTH_FLATTENING, earthFrame);
		
        // Variable used for storing for which date the data from the file is used
		final AbsoluteDate date = new AbsoluteDate("2022-01-01T03:03:05.970", timeScale);
        
		// Variable used for storing the altitude
		final double a = 8350;
		
		// Variable used for storing orbital eccentricity
        final double e = 0.0004342;
        
        // Variable for generating the right ascension of the ascending node
        final double raan = seed.getRAAN();
        
        // Variable used for storing the argument of periapsis
        final double pa = 10.1842;
        
        // Variables used for storing the major axis of the 2 satellites
        final double maTarget = seed.getMa();
        final double maSource = maTarget - 10.0;
        
        // Variables used for creating the 2 orbits of the satellites
        final Orbit startOrbitTarget = new KeplerianOrbit(a * 1000.0, e, FastMath.toRadians(i), FastMath.toRadians(pa), FastMath.toRadians(raan), FastMath.toRadians(maTarget), PositionAngle.MEAN, frame, date, mu);
        final Orbit startOrbitSource = new KeplerianOrbit(a * 1000.0, e, FastMath.toRadians(i), FastMath.toRadians(pa), FastMath.toRadians(raan), FastMath.toRadians(maSource), PositionAngle.MEAN, frame, date, mu);
        
        // Variables used for setting the initial position of the satellites
        final Vector3D initialTargetPosition = startOrbitTarget.getPVCoordinates().getPosition();
		final Vector3D initialSourcePosition = startOrbitSource.getPVCoordinates().getPosition();
		
		// Variable used to find the location relative to the surface of the Earth for the source satellite
		GeodeticPoint pointSource = earth.transform(startOrbitSource.getPVCoordinates().getPosition(), startOrbitSource.getFrame(), startOrbitSource.getDate());
        
		// Variable used for storing the initial latitude for the source satellite
		final double initLatitudeSource = FastMath.toDegrees(pointSource.getLatitude());
        
		// Variable used for storing the initial longitude for the source satellite
		final double initLongitudeSource = FastMath.toDegrees(pointSource.getLongitude());
        
		// Variable used for storing the initial altitude for the source satellite
		final double initAltitudeSource = pointSource.getAltitude();
        
		// Variable used to find the location relative to the surface of the Earth for the target satellite
        GeodeticPoint pointTarget = earth.transform(startOrbitTarget.getPVCoordinates().getPosition(), startOrbitTarget.getFrame(), startOrbitTarget.getDate());
        
        // Variable used for storing the initial latitude for the target satellite
        final double initLatitudeTarget = FastMath.toDegrees(pointTarget.getLatitude());
        
        // Variable used for storing the initial longitude for the target satellite
        final double initLongitudeTarget = FastMath.toDegrees(pointTarget.getLongitude());
        
        // Variable used for storing the initial altitude for the target satellite
        final double initAltitudeTarget = pointTarget.getAltitude();
		
        // Variable used for storing the window in which the game in played
		final WorldWindowGLCanvas wwd = ui.getWorldWindowGLCanvas();
		
		// Variable used for storing the view from which the player sees the scene
		final View view = wwd.getSceneController().getView();
		
		// Set view position
		view.goTo(Position.fromDegrees(initLatitudeSource, initLongitudeSource, initAltitudeSource), 10000000);
		
		// Variable used for storing the positions for the source satellite
		double[] posVectSource = new double[3];
		
		// Variable used for storing the positions for the target satellite
		double[] posVectTarget = new double[3];
		
		// Set the initial latitude for the source satellite
		posVectSource[0] = initLatitudeSource;
		
		// Set the initial longitude for the source satellite
		posVectSource[1] = initLongitudeSource;
		
		// Set the initial altitude for the source satellite
		posVectSource[2] = initAltitudeSource;
		
		// Set the initial latitude for the target satellite
		posVectTarget[0] = initLatitudeTarget;
		
		// Set the initial longitude for the target satellite
		posVectTarget[1] = initLongitudeTarget;
		
		// Set the initial altitude for the target satellite
		posVectTarget[2] = initAltitudeTarget;
        
		// Variable used for creating the trajectory of the source satellite
		final Trajectory tTarget = new Trajectory(posVectTarget, ui, false);
		
		// Variable used for creating the trajectory of the target satellite
		final Trajectory tSource = new Trajectory(posVectSource, ui, true);
        
		// Variable used for storing the panel with initial message before starting the simulation
        final JOptionPane info = new JOptionPane("The distance between satellites is: " + (int) (initialTargetPosition.subtract(initialSourcePosition).getNorm()) / 1000 + "km.");
        
		// Variable used for storing the initial message before starting the simulation
        final JDialog message = info.createDialog(ui.getMainframe(), "Info");
        
        // Add message settings
        message.setLocation(1100, 95);
        message.setVisible(true);
		
        // Execute the program continuously...
		while (true) {
			
			// Print a message in the console while the game is not yet started
        	Logger logger = Logger.getLogger(Main.class.getName());
        	logger.log(Level.INFO, "Waiting for Input...");
        	
        	// Check if the player added his name and if it did...
        	if (VariablesUtils.getName() != null) {
        		
        		// Print a message in the console indicating that the simulation has started
            	logger.log(Level.INFO, "Started");
            	
            	// Set the duration of the simulation
                final double duration = startOrbitTarget.getKeplerianPeriod() * 1.1;
                
                // Set the distance between every position of the satellite
                final double outputStep = 60.0;
                
                // Set the mass of the target satellite
                final double massTarget = 1000.0;
                
                // Calculate all the positions of the trajectory for the target satellite
        		final double[][] positionTarget = Propagate.executePropagation(outputStep, massTarget, date, earth, startOrbitTarget, degree, order, false, duration);
              
        		// Set the mass of the source satellite
        		final double massSource = 250.0;
        		
        		// Calculate all the positions of the trajectory for the source satellite
        		final double[][] positionSource = Propagate.executePropagation(outputStep, massSource, date, earth, startOrbitSource, degree, order, true, duration);
              
        		// Set the delay between each propagation
        		final int timeDelay = 100;
              
        		// Apply the delay
        		ui.setTimeDelay(timeDelay);
        		
        		// Set the position for the target satellite
        		tTarget.setPositions(positionTarget);
        		
        		// Set the position for the source satellite
        		tSource.setPositions(positionSource);
        		
        		// Load the trajectories
        		ui.loadTrajectoryObjects(tTarget, tSource);
        		
        		// Execute the simulation
        		ui.trajectorySimulation();
        		
        		// Declare the vector with all the positions of the target satellite
        		Vector3D[] positionTargetVector = new Vector3D[positionTarget.length];
        		
        		// Declare the vector with all the positions of the source satellite
        		Vector3D[] positionSourceVector = new Vector3D[positionSource.length];
        		
        		// Declare the vector with all the distances between the 2 satellites
        		double[] distances = new double[positionTarget.length];
        		
        		// For each element in the position vector
        		for (int j = 0; j < positionTarget.length; j++) {
        			
        			// Calculate the distance at every point between the 2 satellites
        			positionTargetVector[j] = new Vector3D(positionTarget[j][3], positionTarget[j][4], positionTarget[j][5]);
        			positionSourceVector[j] = new Vector3D(positionSource[j][3], positionSource[j][4], positionSource[j][5]);
        			distances[j] = positionTargetVector[j].subtract(positionSourceVector[j]).getNorm();
        		}
        		
        		// For each element in the minimum distance vector
        		for (int j = 0; j < distances.length; j++) {
        			
        			// If the value is lower than the current lowest value
        			if (distances[j] < minDistance) {
        				
        				// Update the lowest value with the new one
        				minDistance = distances[j];
        			}
        		}
                
                break;
        	}
        }
        
		// Display a message on the screen with game info
        JOptionPane.showMessageDialog(ui.getMainframe(), "You were " + (int) minDistance + " m off!");
        
        // Update the rankings table
        BestScoresTable.checkAndPlace(minDistance);
    }
}
