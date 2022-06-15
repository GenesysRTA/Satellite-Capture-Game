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
	private Object colladaSource;
	private Position position;
	private WorldWindow wwd;
	private ColladaRoot colladaRoot;
	
	private boolean isSource;

	public SatelliteModel(Object colladaSource, Position position, WorldWindow wwd, boolean isSource) {
        this.colladaSource = colladaSource;
        this.position = position;
        this.wwd = wwd;
        this.isSource = isSource;
    }

	public void run() {
        try {
            colladaRoot = ColladaRoot.createAndParse(this.colladaSource);
            colladaRoot.setPosition(this.position);
            if (!isSource) {
            	colladaRoot.setPitch(Angle.fromDegrees(180));
            }
            colladaRoot.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            colladaRoot.setModelScale(new Vec4(100000, 100000, 100000));

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    addColladaLayer(colladaRoot);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	protected void addColladaLayer(ColladaRoot colladaRoot) {
        ColladaController colladaController = new ColladaController(colladaRoot);

        RenderableLayer layer = new RenderableLayer();
        layer.addRenderable(colladaController);
        wwd.getModel().getLayers().add(layer);
    }
	
	public void setPosition(Position position) {
		this.colladaRoot.setPosition(position);;
	}

}
