package org.apache.maven.satellite_capture_game;

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

        UI ev = new UI();
        
        Trajectory t = new Trajectory(lon, lat, alt, ev);
        t.setTrajResolution(resDial);
        t.setTraj();
        
        ev.setTimeDelay(timeDelay);
        ev.loadTrajectoryObjects(t);
        ev.trajSim();
    }
}