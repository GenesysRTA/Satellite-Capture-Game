package org.apache.maven.satellite_capture_game;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.stream.XMLStreamException;

import org.hipparchus.geometry.euclidean.threed.Vector3D;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;

public class Main
{
    public static void main(String[] args) throws IOException, XMLStreamException
    {
    	Seed seed = new Seed();
        final int i = seed.getI();
        
        UI ui = new UI(args);
        double minDistance = Double.MAX_VALUE;
        
        while (true) {
        	
        	System.out.println("Paused");
        	
        	if (Variables.name != null) {
        		
                System.out.println("Started");
        		final double massTarget = 1000.0;
        		final double maTarget = seed.getPosition();
        		final double outputStepTarget = 6.0;
        		double[][] posVelTarget;
        		posVelTarget = Propagate.executePropagation(outputStepTarget, massTarget, maTarget, i, false);
              
        		final double massSource = 250.0;
        		final double maSource = maTarget - 15.0;
        		final double outputStepSource = 6.0;
        		double[][] posVelSource;
        		posVelSource = Propagate.executePropagation(outputStepSource, massSource, maSource, i, true);
              
        		int timeDelay = 100;
              
        		Trajectory tTarget = new Trajectory(posVelTarget, ui, false);
        		Trajectory tSource = new Trajectory(posVelSource, ui, true);
              
        		ui.setTimeDelay(timeDelay);
        		ui.loadTrajectoryObjects(tTarget, tSource);
        		
        		WorldWindowGLCanvas wwd = ui.getWorldWindowGLCanvas();
        		View view = wwd.getSceneController().getView();
		        view.goTo(Position.fromDegrees(posVelSource[0][0], posVelSource[0][1], posVelSource[0][2]), 40000000);
		        
        		ui.trajectorySimulation();
        		
        		Vector3D[] positionTarget = new Vector3D[posVelTarget.length];
        		Vector3D[] positionSource = new Vector3D[posVelSource.length];
        		double[] distances = new double[posVelTarget.length];
        		
        		for (int j = 0; j < posVelTarget.length; j++) {
        			
        			positionTarget[j] = new Vector3D(posVelTarget[j][3], posVelTarget[j][4], posVelTarget[j][5]);
        			positionSource[j] = new Vector3D(posVelSource[j][3], posVelSource[j][4], posVelSource[j][5]);
        			distances[j] = positionTarget[j].subtract(positionSource[j]).getNorm();
        			
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
