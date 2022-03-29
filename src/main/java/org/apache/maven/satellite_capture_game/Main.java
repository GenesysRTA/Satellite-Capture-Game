package org.apache.maven.satellite_capture_game;

public class Main
{
    public static void main(String[] args)
    {
        double lon = 0.0;
        double lat = 0.0;
        double alt = 2000000.0;
        double resolution = 10.0;
        int timeDelay = 1000;
        
        Camera.initialConfigurations();

        UI ui = new UI();
        
        Trajectory t = new Trajectory(lon, lat, alt, ui);
        t.setTrajectoryResolution(resolution);
        t.setTrajectory();
        
        ui.setTimeDelay(timeDelay);
        ui.loadTrajectoryObject(t);
        ui.trajectorySimulation();
    }
}