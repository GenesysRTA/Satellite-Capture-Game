package org.apache.maven.satellite_capture_game;

import java.awt.Color;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Polyline;

import java.util.ArrayList;

public class Trajectory {
    private final ArrayList<Position> pos;
    private int index;
    
    private double[][] positions;
    private int positionIndex;

    private WorldWindowGLCanvas wwd;
    private Polyline path;
    private final RenderableLayer layer;

    private double lat;
    private double lon;
    private double alt;

    private double latPrev;
    private double lonPrev;
    private double altPrev;
    
    private Satellite satellite;
    private SatelliteModel satelliteModel;
    
    private boolean first = true;

    public Trajectory(double[] positions, UI ui, boolean isSource) {
        this.lat = positions[0];
        this.lon = positions[1];
        this.alt = positions[2];
        this.latPrev = positions[0];
        this.lonPrev = positions[1];
        this.altPrev = positions[2];
        
        path = new Polyline();
        if (isSource) {
        	path.setColor(Color.GREEN);
        } else {
        	path.setColor(Color.RED);
        }
        path.setLineWidth(3.0);
        path.setPathType(Polyline.LINEAR);
        path.setFollowTerrain(false);
        
        layer = new RenderableLayer();

        pos  = new ArrayList <>();

        index = 0;
        positionIndex = 1;
        
        wwd = ui.getWorldWindowGLCanvas();
        satellite = new Satellite(this.lat, this.lon, this.alt, wwd, isSource);
        satelliteModel = satellite.getSatelliteShape();
    }
    
    public boolean propagateTrajectory() {
        pos.add(index, Position.fromDegrees(latPrev, lonPrev, altPrev));

        index++;

        pos.add(index, Position.fromDegrees(positions[positionIndex][0], positions[positionIndex][1], positions[positionIndex][2]));
        
        lat = positions[positionIndex][0];
        lon = positions[positionIndex][1];
        alt = positions[positionIndex][2];
        
        if (positionIndex < positions.length - 1) {
        	positionIndex++;
        }
        
        satelliteModel.setPosition(pos.get(index));
        path.setPositions(pos);

        wwd.redrawNow();
        
        latPrev = positions[positionIndex][0];
        lonPrev = positions[positionIndex][1];
        altPrev = positions[positionIndex][2];
        
        if (!first) {
        	if (latPrev == lat && lonPrev == lon && altPrev == alt) {
            	return false;
            }
        }

        index--;
        
        first = false;
        
        return true;
    }
    
    public void setPositions(double[][] positions) {
    	this.positions = positions;
    }
    
    public void loadWorldWindModel(WorldWindowGLCanvas wwd) {
        this.wwd = wwd;

        layer.addRenderable(path);

        this.wwd.getModel().getLayers().add(layer);
    }
}