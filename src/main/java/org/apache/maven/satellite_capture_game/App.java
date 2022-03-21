package org.apache.maven.satellite_capture_game;

import javax.swing.JFrame;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;


public class App extends JFrame
{
    public App()
    {
        WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
        wwd.setPreferredSize(new java.awt.Dimension(1000, 800));
        this.getContentPane().add(wwd, java.awt.BorderLayout.CENTER);
        wwd.setModel(new BasicModel());
    }

    public static void main(String[] args)
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame frame = new App();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}