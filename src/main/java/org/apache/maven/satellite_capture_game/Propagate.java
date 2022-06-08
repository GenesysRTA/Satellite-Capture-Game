package org.apache.maven.satellite_capture_game;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.ode.nonstiff.AdaptiveStepsizeIntegrator;
import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;
import org.orekit.forces.ForceModel;
import org.orekit.forces.drag.DragForce;
import org.orekit.forces.drag.IsotropicDrag;
import org.orekit.forces.gravity.HolmesFeatherstoneAttractionModel;
import org.orekit.forces.gravity.ThirdBodyAttraction;
import org.orekit.forces.gravity.potential.GravityFieldFactory;
import org.orekit.forces.gravity.potential.NormalizedSphericalHarmonicsProvider;
import org.orekit.forces.gravity.potential.UnnormalizedSphericalHarmonicsProvider;
import org.orekit.forces.maneuvers.ImpulseManeuver;
import org.orekit.forces.radiation.IsotropicRadiationSingleCoefficient;
import org.orekit.forces.radiation.SolarRadiationPressure;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.models.earth.atmosphere.Atmosphere;
import org.orekit.models.earth.atmosphere.HarrisPriester;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.DateDetector;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.numerical.NumericalPropagator;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;

public class Propagate {
	
	private static File getResourceFile(final String name) {
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
            throw new IOException("Unable to find orekit-data directory");

        } catch (URISyntaxException use) {
            throw new RuntimeException(use);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	@SuppressWarnings("deprecation")
	public static double[][] executePropagation(final double outputStep, final double satelliteMass, final double ma, final double i, final boolean source) {
		try {

			final File orekitData = getResourceFile("./data/orekit-data");
			DataProvidersManager.getInstance().addProvider(new DirectoryCrawler(orekitData));
			
			final TimeScale timeScale = TimeScalesFactory.getUTC();
			
			final int degree = 12;
	        final int order = 12;
	        
	        UnnormalizedSphericalHarmonicsProvider unnormalized = GravityFieldFactory.getConstantUnnormalizedProvider(degree, order);
	        final double mu = unnormalized.getMu();
	        
	        final Frame frame = FramesFactory.getEME2000();
	        final OneAxisEllipsoid earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS, Constants.WGS84_EARTH_FLATTENING, frame);
	        
	        final double mass = satelliteMass;
	        final double surface = 10.0;
	        
	        final AbsoluteDate date = new AbsoluteDate("2022-01-01T03:03:05.970", timeScale);
	        final double a = 20000;
	        final double e = 0.0004342;
	        final double raan = 323.6970;
	        final double pa = 10.1842;
	        
	        final Orbit startOrbit = new KeplerianOrbit(a * 1000.0, e, FastMath.toRadians(i), FastMath.toRadians(pa), FastMath.toRadians(raan), FastMath.toRadians(ma), PositionAngle.MEAN, frame, date, mu);
	        
	        final double[][] tol = NumericalPropagator.tolerances(1.0, startOrbit, startOrbit.getType());
	        final AdaptiveStepsizeIntegrator integrator = new DormandPrince853Integrator(0.001, 1000.0, tol[0], tol[1]);
	        integrator.setInitialStepSize(0.01);
		
	        final NumericalPropagator numProp = new NumericalPropagator(integrator);
	        numProp.setInitialState(new SpacecraftState(startOrbit, mass));

	        NormalizedSphericalHarmonicsProvider normalized = GravityFieldFactory.getConstantNormalizedProvider(degree, order);
	        numProp.addForceModel(new HolmesFeatherstoneAttractionModel(earth.getBodyFrame(), normalized));
	        
	        numProp.addForceModel(new ThirdBodyAttraction(CelestialBodyFactory.getSun()));
	        numProp.addForceModel(new ThirdBodyAttraction(CelestialBodyFactory.getMoon()));
	        
	        final double cd = 2.0;
	        final Atmosphere atm = new HarrisPriester(CelestialBodyFactory.getSun(), earth, 6.0);

	        final IsotropicDrag idf = new IsotropicDrag(surface, cd, 0, 0);
	        numProp.addForceModel(new DragForce(atm, idf));
	        
	        final double kR = 0.7;
	        final IsotropicRadiationSingleCoefficient irsc = new IsotropicRadiationSingleCoefficient(surface, kR);
	        ForceModel solarRadiationPressure = new SolarRadiationPressure(CelestialBodyFactory.getSun(), earth.getEquatorialRadius(), irsc);
	        numProp.addForceModel(solarRadiationPressure);
	        
	        final OrbitHandler handler = new OrbitHandler();
	        numProp.setMasterMode(outputStep, handler);
	        
	        final double duration = 600;
	        
	        Vector3D vect = new Vector3D(Variables.force * Math.cos(Variables.angle), Variables.force * Math.sin(Variables.angle), 0);
			DateDetector d = new DateDetector(date);
			EventDetector thrust = new ImpulseManeuver<EventDetector>(d, vect, 0.001);
			
	        if (source)
	        {
	        	numProp.addEventDetector(thrust);
	        }
	        
	        try {
				numProp.propagate(date, date.shiftedBy(duration));
				
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
	        
	        double[][] posVel = new double[(int) (duration / outputStep + 1)][6];
	        int index = 0;
	        
	        for (Orbit o : handler.getOrbits()) {
	            GeodeticPoint point = earth.transform(o.getPVCoordinates().getPosition(), o.getFrame(), o.getDate());
	            
	            posVel[index][0] = FastMath.toDegrees(point.getLatitude());
	            posVel[index][1] = FastMath.toDegrees(point.getLongitude());
	            posVel[index][2] = point.getAltitude();
	            posVel[index][3] = o.getPVCoordinates().getPosition().getX();
	            posVel[index][4] = o.getPVCoordinates().getPosition().getY();
	            posVel[index][5] = o.getPVCoordinates().getPosition().getZ();
	            index++;
	        }
	        return posVel;
	        
		} catch (OrekitException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static class OrbitHandler implements OrekitFixedStepHandler {
        private final List<Orbit> orbits;
        
        private OrbitHandler() {
            orbits = new ArrayList<Orbit>();
        }
        public void handleStep(final SpacecraftState currentState, final boolean isLast) {
            orbits.add(currentState.getOrbit());
        }
        public List<Orbit> getOrbits() {
            return orbits;
        }
    }
}