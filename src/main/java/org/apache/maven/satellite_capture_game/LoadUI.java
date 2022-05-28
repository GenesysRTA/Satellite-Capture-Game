package org.apache.maven.satellite_capture_game;

import java.io.IOException;

public class LoadUI {
	
	public LoadUI(double[][] posVelTarget, double[][] posVelSource, int timeDelay, String[] args) throws IOException {
		
		UI ui = new UI(args);
        
        Trajectory tTarget = new Trajectory(posVelTarget, ui, false);
        Trajectory tSource = new Trajectory(posVelSource, ui, true);
        
        ui.setTimeDelay(timeDelay);
        ui.loadTrajectoryObjects(tTarget, tSource);
        ui.trajectorySimulation();
		
	}
}
