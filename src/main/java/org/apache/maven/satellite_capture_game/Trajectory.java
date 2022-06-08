package org.apache.maven.satellite_capture_game;

import java.awt.Color;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Polyline;

import java.util.ArrayList;

public class Trajectory
{
    private final ArrayList<Position> pos;
    private int index;
    
    private double[][] posVel;
    private int posVelIndex;

    private WorldWindowGLCanvas wwd;
    private Polyline path;
    private final RenderableLayer layer;

    private double lat;
    private double lon;
    private double alt;

    private double latPrev;
    private double lonPrev;
    private double altPrev;
    
    UI ui;
    private Satellite s;
    private SatelliteModel e;
    
    private boolean first = true;

    public Trajectory(double[][] posVel, UI ui, boolean isSource) {
        this.lat = posVel[0][0];
        this.lon = posVel[0][1];
        this.alt = posVel[0][2];
        this.latPrev = posVel[0][0];
        this.lonPrev = posVel[0][1];
        this.altPrev = posVel[0][2];
        this.posVel = posVel;
        
        path = new Polyline();
        if (isSource) {
        	path.setColor(Color.RED);
        } else {
        	path.setColor(Color.BLUE);
        }
        path.setLineWidth(3.0);
        path.setPathType(Polyline.LINEAR);
        path.setFollowTerrain(false);
        
        layer = new RenderableLayer();

        pos  = new ArrayList <>();

        index = 0;
        posVelIndex = 1;
        
        this.ui = ui;
        wwd = ui.getWorldWindowGLCanvas();
        s = new Satellite(this.lat, this.lon, this.alt, wwd, isSource);
        e = s.getSatelliteShape();

    }
    
    public boolean propagateTrajectory(int timeDelay)
    {
        pos.add(index, Position.fromDegrees(latPrev, lonPrev, altPrev));

        index++;

        pos.add(index, Position.fromDegrees(posVel[posVelIndex][0], posVel[posVelIndex][1], posVel[posVelIndex][2]));
        
        lat = posVel[posVelIndex][0];
        lon = posVel[posVelIndex][1];
        alt = posVel[posVelIndex][2];
        
        if (posVelIndex < posVel.length - 1) {
        	posVelIndex++;
        }
        
        e.setPosition(pos.get(index));
        path.setPositions(pos);

        wwd.redrawNow();
        
        latPrev = posVel[posVelIndex][0];
        lonPrev = posVel[posVelIndex][1];
        altPrev = posVel[posVelIndex][2];
        
        if (!first) {
        	
        	if (latPrev == lat && lonPrev == lon && altPrev == alt) {
            	return false;
            }
        	
        }

        index--;
        
        first = false;
        
        return true;
    }
    
    public void loadWorldWindModel(WorldWindowGLCanvas wwd)
    {
        this.wwd = wwd;

        layer.addRenderable(path);

        this.wwd.getModel().getLayers().add(layer);
    }

    public double[] getInitialPosition() {
    	double[] position = new double[3];
    	
    	position[0] = this.lat;
    	position[1] = this.lon;
    	position[2] = this.alt;
    	
    	return position;
    }
}