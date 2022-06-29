package org.apache.maven.satellite_capture_game;

import java.io.File;
import java.io.IOException;

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
    	Seed seed = new Seed();
        final double i = seed.getI();
        
        UI ui = new UI(args);
        double minDistance = Double.MAX_VALUE;
        
        final File orekitData = VariablesUtils.getResourceFile("./data/orekit-data");
		DataProvidersManager.getInstance().addProvider(new DirectoryCrawler(orekitData));
		
		final TimeScale timeScale = TimeScalesFactory.getUTC();
		
		final int degree = 12;
        final int order = 12;
        
        UnnormalizedSphericalHarmonicsProvider unnormalized = GravityFieldFactory.getConstantUnnormalizedProvider(degree, order);
        final double mu = unnormalized.getMu();
        
        final Frame frame = FramesFactory.getEME2000();
        final Frame earthFrame = FramesFactory.getITRF(IERSConventions.IERS_2010, true);
        final OneAxisEllipsoid earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS, Constants.WGS84_EARTH_FLATTENING, earthFrame);
		
		final AbsoluteDate date = new AbsoluteDate("2022-01-01T03:03:05.970", timeScale);
        final double a = 8350;
        final double e = 0.0004342;
        final double raan = seed.getRAAN();;
        final double pa = 10.1842;
        
        final double maTarget = seed.getMa();
        final double maSource = maTarget - 10.0;
        
        final Orbit startOrbitTarget = new KeplerianOrbit(a * 1000.0, e, FastMath.toRadians(i), FastMath.toRadians(pa), FastMath.toRadians(raan), FastMath.toRadians(maTarget), PositionAngle.MEAN, frame, date, mu);
        final Orbit startOrbitSource = new KeplerianOrbit(a * 1000.0, e, FastMath.toRadians(i), FastMath.toRadians(pa), FastMath.toRadians(raan), FastMath.toRadians(maSource), PositionAngle.MEAN, frame, date, mu);
        
        final Vector3D initialTargetPosition = startOrbitTarget.getPVCoordinates().getPosition();
		final Vector3D initialSourcePosition = startOrbitSource.getPVCoordinates().getPosition();
		
		GeodeticPoint pointSource = earth.transform(startOrbitSource.getPVCoordinates().getPosition(), startOrbitSource.getFrame(), startOrbitSource.getDate());
        final double initLatitudeSource = FastMath.toDegrees(pointSource.getLatitude());
        final double initLongitudeSource = FastMath.toDegrees(pointSource.getLongitude());
        final double initAltitudeSource = pointSource.getAltitude();
        
        GeodeticPoint pointTarget = earth.transform(startOrbitTarget.getPVCoordinates().getPosition(), startOrbitTarget.getFrame(), startOrbitTarget.getDate());
        final double initLatitudeTarget = FastMath.toDegrees(pointTarget.getLatitude());
        final double initLongitudeTarget = FastMath.toDegrees(pointTarget.getLongitude());
        final double initAltitudeTarget = pointTarget.getAltitude();
		
		final WorldWindowGLCanvas wwd = ui.getWorldWindowGLCanvas();
		final View view = wwd.getSceneController().getView();
		view.goTo(Position.fromDegrees(initLatitudeSource, initLongitudeSource, initAltitudeSource), 10000000);
		
		double[] posVectSource = new double[3];
		double[] posVectTarget = new double[3];
		
		posVectSource[0] = initLatitudeSource;
		posVectSource[1] = initLongitudeSource;
		posVectSource[2] = initAltitudeSource;
		
		posVectTarget[0] = initLatitudeTarget;
		posVectTarget[1] = initLongitudeTarget;
		posVectTarget[2] = initAltitudeTarget;
        
		final Trajectory tTarget = new Trajectory(posVectTarget, ui, false);
		final Trajectory tSource = new Trajectory(posVectSource, ui, true);
        
        final JOptionPane info = new JOptionPane("The distance between satellites is: " + (int) (initialTargetPosition.subtract(initialSourcePosition).getNorm()) / 1000 + "km.");
        final JDialog message = info.createDialog(ui.getMainframe(), "Info");
        message.setLocation(1100, 95);
        message.setVisible(true);
		
		while (true) {
        	System.out.println("Waiting for Input...");
        	
        	if (VariablesUtils.getName() != null) {
                System.out.println("Started");
                final double duration = startOrbitTarget.getKeplerianPeriod() * 1.1;
                
                final double outputStep = 60.0;
                final double massTarget = 1000.0;
        		final double[][] positionTarget = Propagate.executePropagation(outputStep, massTarget, date, earth, startOrbitTarget, degree, order, false, duration);
              
        		final double massSource = 250.0;
        		final double[][] positionSource = Propagate.executePropagation(outputStep, massSource, date, earth, startOrbitSource, degree, order, true, duration);
              
        		final int timeDelay = 100;
              
        		ui.setTimeDelay(timeDelay);
        		tTarget.setPositions(positionTarget);
        		tSource.setPositions(positionSource);
        		ui.loadTrajectoryObjects(tTarget, tSource);
        		ui.trajectorySimulation();
        		
        		Vector3D[] positionTargetVector = new Vector3D[positionTarget.length];
        		Vector3D[] positionSourceVector = new Vector3D[positionSource.length];
        		double[] distances = new double[positionTarget.length];
        		
        		for (int j = 0; j < positionTarget.length; j++) {
        			positionTargetVector[j] = new Vector3D(positionTarget[j][3], positionTarget[j][4], positionTarget[j][5]);
        			positionSourceVector[j] = new Vector3D(positionSource[j][3], positionSource[j][4], positionSource[j][5]);
        			distances[j] = positionTargetVector[j].subtract(positionSourceVector[j]).getNorm();
        		}
        		
        		for (int j = 0; j < distances.length; j++) {
        			if (distances[j] < minDistance) {
        				minDistance = distances[j];
        			}
        		}
                
                break;
        	}
        }
        
        JOptionPane.showMessageDialog(ui.getMainframe(), "You were " + (int) minDistance + " m off!");
        
        BestScoresTable.CheckAndPlace(minDistance);
    }
}
