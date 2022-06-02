package org.apache.maven.satellite_capture_game;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

public class Main
{
    public static void main(String[] args) throws IOException, XMLStreamException
    {
    	Seed seed = new Seed();
        final double duration = 6000.0;
        final int i = seed.getI(); //1
        
        final double massTarget = 1000.0;
        final double maTarget = seed.getPosition(); //350
        final double outputStepTarget = 6.0;
        double[][] posVelTarget = new double[(int) (duration / outputStepTarget + 1)][3];
        posVelTarget = Propagate.executePropagation(outputStepTarget, duration, massTarget, maTarget, i, false);
        
        final double massSource = 250.0;
        final double maSource = maTarget - 15.0;
        final double outputStepSource = 6.0;
        double[][] posVelSource = new double[(int) (duration / outputStepSource + 1)][3];
        posVelSource = Propagate.executePropagation(outputStepSource, duration, massSource, maSource, i, true);
        
        int timeDelay = 100;
        
        Camera.initialConfigurations(posVelSource[0][0], posVelSource[0][1]);

        LoadUI loadUI = new LoadUI(posVelTarget, posVelSource, timeDelay, args);
        
    }
}