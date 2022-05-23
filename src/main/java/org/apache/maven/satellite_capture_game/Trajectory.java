package org.apache.maven.satellite_capture_game;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Ellipsoid;

import java.util.ArrayList;

public class Trajectory
{
    private final ArrayList<Position> pos;
    private int index;
    
    private double[][] posVel;
    private int posVelIndex;

    private WorldWindowGLCanvas wwd;

    private double lat;
    private double lon;
    private double alt;

    private double latPrev;
    private double lonPrev;
    private double altPrev;
    
    UI ui;
    private Satellite s;
    private Ellipsoid e;

    public Trajectory(double[][] posVel, UI ui) {
        this.lat = posVel[0][0];
        this.lon = posVel[0][1];
        this.alt = posVel[0][2];
        this.latPrev = posVel[0][0];
        this.lonPrev = posVel[0][1];
        this.altPrev = posVel[0][2];
        this.posVel = posVel;

        pos  = new ArrayList <>();

        index = 0;
        posVelIndex = 1;
        
        this.ui = ui;
        wwd = ui.getWorldWindowGLCanvas();
        s = new Satellite(this.lat, this.lon, this.alt, wwd);
        e = s.getShape();
    }

    public void propagateTrajectory()
    {
        pos.add(index, Position.fromDegrees(latPrev, lonPrev, altPrev));

        index++;

        pos.add(index, Position.fromDegrees(posVel[posVelIndex][0], posVel[posVelIndex][1], posVel[posVelIndex][2]));
        
        if (posVelIndex < posVel.length - 1) {
        	posVelIndex++;
        }
        
        e.setCenterPosition(pos.get(index));

        wwd.redrawNow();
        
        latPrev = lat;
        lonPrev = lon;
        altPrev = alt;

        index--;
    }
    
    public double[] getInitialPosition() {
    	double[] position = new double[3];
    	
    	position[0] = this.lat;
    	position[1] = this.lon;
    	position[2] = this.alt;
    	
    	return position;
    }
}