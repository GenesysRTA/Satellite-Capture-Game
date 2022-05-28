package org.apache.maven.satellite_capture_game;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

public class Main
{
    public static void main(String[] args) throws IOException, XMLStreamException
    {
        final double duration = 6000.0;
        
        final double massTarget = 1000.0;
        final double maTarget = 349.8637;
        final double outputStepTarget = 6.0;
        double[][] posVelTarget = new double[(int) (duration / outputStepTarget + 1)][3];
        posVelTarget = Propagate.executePropagation(outputStepTarget, duration, massTarget, maTarget, false);
        
        final double massSource = 250.0;
        final double maSource = 334.8637;
        final double outputStepSource = 10.0;
        double[][] posVelSource = new double[(int) (duration / outputStepSource + 1)][3];
        posVelSource = Propagate.executePropagation(outputStepSource, duration, massSource, maSource, true);
        
        int timeDelay = 100;
        
        Camera.initialConfigurations();

        LoadUI loadUI = new LoadUI(posVelTarget, posVelSource, timeDelay, args);
        
    }
}