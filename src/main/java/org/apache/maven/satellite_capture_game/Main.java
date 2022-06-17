package org.apache.maven.satellite_capture_game;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;

public class Main
{
    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException
    {
    	Seed seed = new Seed();
        final int i = seed.getI();
        
        UI ui = new UI(args);
        double minDistance = Double.MAX_VALUE;
        
        final File orekitData = VariablesUtils.getResourceFile("./data/orekit-data");
		DataProvidersManager.getInstance().addProvider(new DirectoryCrawler(orekitData));
        
        while (true) {
        	System.out.println("Waiting for Input...");
        	
        	if (VariablesUtils.getName() != null) {
                System.out.println("Started");
                
        		final double massTarget = 1000.0;
        		final double maTarget = seed.getPosition();
        		final double outputStepTarget = 6.0;
        		final double[][] positionTarget = Propagate.executePropagation(outputStepTarget, massTarget, maTarget, i, false);
              
        		final double massSource = 250.0;
        		final double maSource = maTarget - 10.0;
        		final double outputStepSource = 6.0;
        		final double[][] positionSource = Propagate.executePropagation(outputStepSource, massSource, maSource, i, true);
              
        		final int timeDelay = 100;
              
        		Trajectory tTarget = new Trajectory(positionTarget, ui, false);
        		Trajectory tSource = new Trajectory(positionSource, ui, true);
              
        		ui.setTimeDelay(timeDelay);
        		ui.loadTrajectoryObjects(tTarget, tSource);
        		
        		WorldWindowGLCanvas wwd = ui.getWorldWindowGLCanvas();
        		View view = wwd.getSceneController().getView();
		        view.goTo(Position.fromDegrees(positionSource[0][0], positionSource[0][1], positionSource[0][2]), 10000000);
		        
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
