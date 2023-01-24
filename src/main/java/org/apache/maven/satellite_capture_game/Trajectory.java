package org.apache.maven.satellite_capture_game;

import java.awt.Color;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Polyline;

import java.util.ArrayList;

public class Trajectory {
	
	// Variable used for storing the list of positions of the satellite
    private final ArrayList<Position> pos;
    
    // Variable used for marking the moment the satellite has passed from the initial position to the second position
    private int index;
    
    // Variable used for storing all positions of the satellite
    private double[][] positions;
    
    // Variable used for storing the current position
    private int positionIndex;

    // Variable used for storing the window in which the game takes place
    private WorldWindowGLCanvas wwd;
    
    // Variable used for the drawing the path the satellite takes
    private Polyline path;
    
    // Variable used for storing the layer of the path
    private final RenderableLayer layer;

    // Variable used for storing the current latitude of the satellite
    private double lat;
    
    // Variable used for storing the current longitude of the satellite
    private double lon;
    
    // Variable used for storing the current altitude of the satellite
    private double alt;

    // Variable used for storing the current previous latitude of the satellite
    private double latPrev;
    
    // Variable used for storing the current previous longitude of the satellite
    private double lonPrev;
    
    // Variable used for storing the current previous altitude of the satellite
    private double altPrev;
    
    // Variable used for storing the satellite
    private Satellite satellite;
    
    // Variable used for storing the satellite model
    private SatelliteModel satelliteModel;
    
    // Variable used as a flag to indicate the first transition between positions
    private boolean first = true;

    // Constructor
    public Trajectory(double[] positions, UI ui, boolean isSource) {
    	
    	// Set latitude position
        this.lat = positions[0];
        
        // Set longitude position
        this.lon = positions[1];
        
        // Set altitude position
        this.alt = positions[2];
        
        // Set previous latitude position
        this.latPrev = positions[0];
        
        // Set previous longitude position
        this.lonPrev = positions[1];
        
        // Set previous altitude position
        this.altPrev = positions[2];
        
        // Declare the path
        path = new Polyline();
        
        // Check if the satellite is the one controlled by the player and if it is...
        if (isSource) {
        	
        	// Set path color to green
        	path.setColor(Color.GREEN);
        	
        // If it is the target satellite...
        } else {
        	
        	// Set path color to red
        	path.setColor(Color.RED);
        }
        
        // Set path width
        path.setLineWidth(3.0);
        
        // Set path type
        path.setPathType(Polyline.LINEAR);
        
        // Set path to not follow the terrain
        path.setFollowTerrain(false);
        
        // Declare a new layer for the path
        layer = new RenderableLayer();

        // Declare the positions list
        pos  = new ArrayList <>();

        // Set the transition index as from previos location to current one
        index = 0;
        
        // Set the position index
        positionIndex = 1;
        
        // Set the game window
        wwd = ui.getWorldWindowGLCanvas();
        
        // Create the satellite
        satellite = new Satellite(this.lat, this.lon, this.alt, wwd, isSource);
        
        // Add the model to the satellite
        satelliteModel = satellite.getSatelliteShape();
    }
    
    // Method used for drawing the path followed by the satellite
    public boolean propagateTrajectory() {
    	
    	// Add the previous position to the list
        pos.add(index, Position.fromDegrees(latPrev, lonPrev, altPrev));

        // Increase the index to mark that there is a transition towards a new position
        index++;

        // Add the new position to the list
        pos.add(index, Position.fromDegrees(positions[positionIndex][0], positions[positionIndex][1], positions[positionIndex][2]));
        
        // Set the latitude to be the previous latitude value
        lat = positions[positionIndex][0];
        
        // Set the longitude to be the previous longitude value
        lon = positions[positionIndex][1];
        
        // Set the altitude to be the previous altitude value
        alt = positions[positionIndex][2];
        
        // Increase the position index from the list when a transition is made
        if (positionIndex < positions.length - 1) {
        	positionIndex++;
        }
        
        // Set satellite position to be the current one
        satelliteModel.setPosition(pos.get(index));
        
        // Set the path position to be the current onw
        path.setPositions(pos);

        // Redraw the scene
        wwd.redrawNow();
        
        // Set the previous latitude to be the current latitude value
        latPrev = positions[positionIndex][0];
        
        // Set the previous longitude to be the current longitude value
        lonPrev = positions[positionIndex][1];
        
        // Set the previous altitude to be the current altitude value
        altPrev = positions[positionIndex][2];
        
        // Continue to repeat the process until the previous values are equal to the new ones
        if (!first) {
        	if (latPrev == lat && lonPrev == lon && altPrev == alt) {
            	return false;
            }
        }

        // Decrease the index marking that a new transition can be made
        index--;
        
        // Mark that the first transition was made
        first = false;
        
        // Return true until the previous values are equal to the new ones
        return true;
    }
    
    // Setter - Positions
    public void setPositions(double[][] positions) {
    	this.positions = positions;
    }
    
    // Method used for loading the window
    public void loadWorldWindModel(WorldWindowGLCanvas wwd) {
    	
    	// Set the window
        this.wwd = wwd;

        // Add the path to the layer
        layer.addRenderable(path);

        // Add the layer to the window
        this.wwd.getModel().getLayers().add(layer);
    }
}