package org.apache.maven.satellite_capture_game;

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.ogc.collada.ColladaRoot;
import gov.nasa.worldwind.ogc.collada.impl.ColladaController;

import javax.swing.*;
import javax.xml.stream.XMLStreamException;

import java.awt.event.*;
import java.io.IOException;

public class SatelliteModel extends Thread {
	protected Object colladaSource;
	protected Position position;
	protected WorldWindow wwd;
	protected static ColladaRoot colladaRoot;
	
	public SatelliteModel(Object colladaSource, Position position, WorldWindow wwd) throws IOException, XMLStreamException
    {
        this.colladaSource = colladaSource;
        this.position = position;
        this.wwd = wwd;
        colladaRoot = ColladaRoot.createAndParse(this.colladaSource);
    }
	
	public void run()
    {
        try
        {
            colladaRoot.setPosition(this.position);
            colladaRoot.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            colladaRoot.setModelScale(new Vec4(100000, 100000, 100000));

            // Schedule a task on the EDT to add the parsed document to a layer
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    addColladaLayer(colladaRoot);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	
	protected void addColladaLayer(ColladaRoot colladaRoot)
    {
        // Create a ColladaController to adapt the ColladaRoot to the World Wind renderable interface.
        ColladaController colladaController = new ColladaController(colladaRoot);

        // Adds a new layer containing the ColladaRoot to the end of the WorldWindow's layer list.
        RenderableLayer layer = new RenderableLayer();
        layer.addRenderable(colladaController);
        wwd.getModel().getLayers().add(layer);
    }
	
	public static void move(Position oldPos, Position newPos, int timeDelay)
	{
		int delay = timeDelay; //milliseconds
        ActionListener taskPerformer = new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                //double deltaDegrees = 0.001;
                //Position position = colladaRoot.getPosition();
                Position newPosition = newPos;

                // Move the model
                colladaRoot.setPosition(newPosition);
            }
        };
        new Timer(delay, taskPerformer).start();
	}
	
	public void setCenterPosition(Position pos)
	{
		this.position = pos;
	}

}
