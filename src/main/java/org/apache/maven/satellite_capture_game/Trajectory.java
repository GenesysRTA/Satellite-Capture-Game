package org.apache.maven.satellite_capture_game;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Ellipsoid;
import gov.nasa.worldwind.render.Polyline;

import java.awt.Color;
import java.util.ArrayList;

public class Trajectory
{
    private Polyline path;
    private final ArrayList<Position> pos;
    private int index;

    private WorldWindowGLCanvas wwd;
    private final RenderableLayer layer;

    private double lat;
    private double lon;
    private double alt;

    private double latPrev;
    private double lonPrev;
    private double altPrev;
    
    private double lonRate;
    private double resolution;
    
    UI ui;
    private Satellite s;
    private Ellipsoid e;

    public Trajectory(double lat, double lon, double alt, UI ui) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
        this.latPrev = lat;
        this.lonPrev = lon;
        this.altPrev = alt;
        
        path = new Polyline();
        path.setColor(Color.RED);
        path.setLineWidth(3.0);
        path.setPathType(Polyline.LINEAR);
        path.setFollowTerrain(false);

        pos  = new ArrayList <>();
        
        layer = new RenderableLayer();

        index = 0;
        
        this.ui = ui;
        wwd = ui.getWorldWindowGLCanvas();
        s = new Satellite(this.lat, this.lon, this.alt, wwd);
        e = s.getShape();
    }

    public void propagateTrajectory()
    {
        pos.add(index, Position.fromDegrees(latPrev, lonPrev, altPrev));

        index++;

        lon = lon + lonRate;

        pos.add(index,Position.fromDegrees(lat, lon, alt));

        path.setPositions(pos);
        
        e.setCenterPosition(pos.get(index));

        wwd.redrawNow();
        
        latPrev = lat;
        lonPrev = lon;
        altPrev = alt;

        index--;
    }

    public void setTrajectoryResolution(double resolution)
    {
        this.resolution = resolution;
    }
    
    public void setTrajectory()
    {
        lonRate = (1.0 / resolution) * 2.0 * 0.3;
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