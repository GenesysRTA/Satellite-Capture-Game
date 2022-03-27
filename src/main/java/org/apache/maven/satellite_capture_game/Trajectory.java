package org.apache.maven.satellite_capture_game;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Polyline;

import java.awt.Color;
import java.util.ArrayList;

public class Trajectory
{
    private Polyline path;
    private final ArrayList<Position> pos;

    private WorldWindow wwd;
    private final RenderableLayer layer;

    private int ict;
    private double lonRate;
    private double resDial;

    private double lat;
    private double lon;
    private double alt;

    private double latPrev;
    private double lonPrev;
    private double altPrev;

    public Trajectory(double lat, double lon, double alt) {
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

        ict = 0;
    }

    public void propagateTrajectory()
    {
        pos.add(ict, Position.fromDegrees(latPrev, lonPrev, altPrev));

        ict++;

        lon = lon + lonRate;

        pos.add(ict,Position.fromDegrees(lat, lon, alt));

        path.setPositions(pos);

        wwd.redrawNow();
        
        latPrev = lat;
        lonPrev = lon;
        altPrev = alt;

        ict--;
    }

    public void setTrajResolution(double resDial)
    {
        this.resDial = resDial;
    }
    
    public void setTraj()
    {
        lonRate = (1.0 / resDial) * 2.0 * 0.30;
    }

    public void loadWorldWindModel(WorldWindow wwd)
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