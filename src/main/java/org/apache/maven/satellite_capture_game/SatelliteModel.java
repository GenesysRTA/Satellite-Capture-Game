package org.apache.maven.satellite_capture_game;

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.ogc.collada.ColladaRoot;
import gov.nasa.worldwind.ogc.collada.impl.ColladaController;

import javax.swing.*;

public class SatelliteModel extends Thread {
	
	// Variable used for storing the source of the model
	private Object colladaSource;
	
	// Variable used for storing the position of the model
	private Position position;
	
	// Variable used for storing the window the game is played in
	private WorldWindow wwd;
	
	// Variable used for storing the root element of the model
	private ColladaRoot colladaRoot;
	
	// Variable used as a flag to check if the model is used for the source satellite
	private boolean isSource;

	// Constructor
	public SatelliteModel(Object colladaSource, Position position, WorldWindow wwd, boolean isSource) {
        
		// Set the source of the model
		this.colladaSource = colladaSource;
		
		// Set the position of the model
        this.position = position;
        
        // Set the window of the game
        this.wwd = wwd;
        
        // Set the satellite's type
        this.isSource = isSource;
    }

	// Method used for updating the satellite model in the game
	@Override
	public void run() {
		// Create the root element of the model
        try {
        	// Store the root element
            colladaRoot = ColladaRoot.createAndParse(this.colladaSource);
            
            // Set element position
            colladaRoot.setPosition(this.position);
            
            // Check if the satellite is source and if it is not...
            if (!isSource) {
            	
            	// Set a pitch of 180 degrees
            	colladaRoot.setPitch(Angle.fromDegrees(180));
            }
            
            // Set satellite altitude model to be relative to the ground
            colladaRoot.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            
            // Set satellite scale
            colladaRoot.setModelScale(new Vec4(50000, 50000, 50000));

            // Add element to the layer
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    addColladaLayer(colladaRoot);
                }
            });
        }
        
        // If there is an exception...
        catch (Exception e)
        {
        	
        	// Print the stack trace
            e.printStackTrace();
        }
    }

	// Method used for adding a layer for the new satellite object so it can be displayed in the window
	private void addColladaLayer(ColladaRoot colladaRoot) {
		
		// Declare a controller for the root element
        ColladaController colladaController = new ColladaController(colladaRoot);

        // Declare a layer for the element
        RenderableLayer layer = new RenderableLayer();
        
        // Add the controller to the layer
        layer.addRenderable(colladaController);
        
        // Add the layer to the window
        wwd.getModel().getLayers().add(layer);
    }
	
	// Setter - Position
	public void setPosition(Position position) {
		this.colladaRoot.setPosition(position);
	}

}
