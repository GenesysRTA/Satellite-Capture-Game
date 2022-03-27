package org.apache.maven.satellite_capture_game;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;

public class EarthProj
{
    public static void main(String[] args)
    {
        double lon = 0.0;
        double lat = 0.0;
        double alt = 2000000.0;
        double resDial = 10.0;
        int timeDelay = 1000;
        
        Camera.initialConfigurations();
        
        Trajectory t = new Trajectory(lon, lat, alt);
        t.setTrajResolution(resDial);
        t.setTraj();

        UI ev = new UI();
        WorldWindowGLCanvas wwd = ev.getWorldWindowGLCanvas();
        Satellite s = new Satellite(wwd);
        
        ev.setTimeDelay(timeDelay);
        ev.loadTrajectoryObjects(t);
        ev.trajSim();
    }
}