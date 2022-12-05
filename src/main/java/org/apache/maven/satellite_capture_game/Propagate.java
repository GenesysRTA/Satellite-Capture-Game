package org.apache.maven.satellite_capture_game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.ode.nonstiff.AdaptiveStepsizeIntegrator;
import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;
import org.hipparchus.util.FastMath;
import org.orekit.attitudes.AttitudeProvider;
import org.orekit.attitudes.BodyCenterPointing;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.errors.OrekitException;
import org.orekit.forces.ForceModel;
import org.orekit.forces.drag.DragForce;
import org.orekit.forces.drag.IsotropicDrag;
import org.orekit.forces.gravity.HolmesFeatherstoneAttractionModel;
import org.orekit.forces.gravity.ThirdBodyAttraction;
import org.orekit.forces.gravity.potential.GravityFieldFactory;
import org.orekit.forces.gravity.potential.NormalizedSphericalHarmonicsProvider;
import org.orekit.forces.maneuvers.ImpulseManeuver;
import org.orekit.forces.radiation.IsotropicRadiationSingleCoefficient;
import org.orekit.forces.radiation.SolarRadiationPressure;
import org.orekit.models.earth.atmosphere.Atmosphere;
import org.orekit.models.earth.atmosphere.HarrisPriester;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.DateDetector;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.numerical.NumericalPropagator;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;
import org.orekit.time.AbsoluteDate;

public class Propagate {
	public static double[][] executePropagation(final double outputStep, final double satelliteMass, final AbsoluteDate date, final OneAxisEllipsoid earth, final Orbit startOrbit, final int degree, final int order, final boolean source, final double duration) {
		try {
	        final double mass = satelliteMass;
	        final double surface = 10.0;
	        
	        final double[][] tol = NumericalPropagator.tolerances(1.0, startOrbit, startOrbit.getType());
	        final AdaptiveStepsizeIntegrator integrator = new DormandPrince853Integrator(0.001, 1000.0, tol[0], tol[1]);
	        integrator.setInitialStepSize(0.01);
	        
	        final AttitudeProvider attitudeProvider = new BodyCenterPointing(startOrbit.getFrame(), earth);
		
	        final NumericalPropagator numProp = new NumericalPropagator(integrator);
	        numProp.setInitialState(new SpacecraftState(startOrbit, mass));
	        
	        numProp.setAttitudeProvider(attitudeProvider);

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
	        
	        double angle = FastMath.toRadians(VariablesUtils.getAngle());
	        
	        Vector3D vect = new Vector3D(VariablesUtils.getForce() * FastMath.cos(angle), 0, VariablesUtils.getForce() * FastMath.sin(angle));
			DateDetector d = new DateDetector(date.shiftedBy(1));
			EventDetector thrust = new ImpulseManeuver<EventDetector>(d, vect, 1);
			
	        if (source) {
	        	numProp.addEventDetector(thrust);
	        }
	        
	        try {
				numProp.propagate(date, date.shiftedBy(duration));
				
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
	        
	        double[][] position = new double[(int) (duration / outputStep + 1)][6];
	        int index = 0;
	        
	        for (Orbit o : handler.getOrbits()) {
	            GeodeticPoint point = earth.transform(o.getPVCoordinates().getPosition(), o.getFrame(), o.getDate());
	            
	            position[index][0] = FastMath.toDegrees(point.getLatitude());
	            position[index][1] = FastMath.toDegrees(point.getLongitude());
	            position[index][2] = point.getAltitude();
	            position[index][3] = o.getPVCoordinates().getPosition().getX();
	            position[index][4] = o.getPVCoordinates().getPosition().getY();
	            position[index][5] = o.getPVCoordinates().getPosition().getZ();
	            index++;
	        }
	        return position;
	        
		} catch (OrekitException e) {
			e.printStackTrace();
		}
		
		return new double[0][0];
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