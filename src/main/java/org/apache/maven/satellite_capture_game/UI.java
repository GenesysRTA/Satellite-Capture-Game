package org.apache.maven.satellite_capture_game;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public final class UI
{
    // The JFrame upon which the Earth model is mounted.  Since we're using a
    // JDesktop as the main frame, JInternalFrame is used instead of JFrame.
    private final JInternalFrame ew;

    // Main JPanel that's mounted to the JInternalFrame.
    private final JPanel mainPanel;
    private final JPanel subPanel1;

    private final BuildJPanel bp1;
    private final BuildJPanel bp2;

    // WorldWind Earth model canvas.
    private final WorldWindowGLCanvas wwd;

    // Trajectory object that will be propagated in the simulation.
    private Trajectory to;

    public UI()
    {
        // Create the main JFrame and Desktop frames
        JFrame mainJFrame = new JFrame("Project");
        // Set so that the simulation will stop when the JFrame is closed manually by the user.
        mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int Width = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        mainJFrame.setSize(Width - 150, height - 150);
        // Make the JFrame visible.
        mainJFrame.setVisible(true);
        // Set the JFrame size.
        mainJFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainJFrame.setResizable(true);

        // Create the desktop
        JDesktopPane FSimdesktop = new JDesktopPane();
        // Set the desktop background color using RGB values.
        FSimdesktop.setBackground(new Color(230, 230, 230));
        FSimdesktop.setVisible(true);
        // Load the desktop into the JFrame
        mainJFrame.add(FSimdesktop);

        // Set up basic JInternalFrame
        //**********************************************************************
        ew = new JInternalFrame("Earth View");
        // Make the JInternalFame re-sizable.  The Earth model size will 
        // remain the same, but the user can manually resize the frame size.
        ew.setResizable(true);
        // Set the size of the JInternalFrame.
        ew.setSize(1500,1000);
        ew.setMaximumSize(new Dimension(1150,1000));
        
        // Immobilize JInternalFrame
        for(MouseListener listener : ((javax.swing.plaf.basic.BasicInternalFrameUI) ew.getUI()).getNorthPane().getMouseListeners()){
        	((javax.swing.plaf.basic.BasicInternalFrameUI) ew.getUI()).getNorthPane().removeMouseListener(listener);
        }
        
        //ew.setMinimumSize(new Dimension(1150,875));
        // Set location of the JInternalFrame relative to the JDesktop.
        ew.setLocation(415,10);

        // Set up main JPanel
        //**********************************************************************
        // Set up main JPanel - it will be mounted to the JInternalFrame and 
        // all other JPanels will be mounted to this JPanel.
        int[] fl    = {1, 3, 3};        // Flow control numbers
        int[] dim   = {1500, 1000};       // Dimensions 1150, 835
        int[] col   = {160, 160, 160};    // RGB Color numbers

        // Build the custom BuildJPanel object - it contains 
        // the specified JPanel.
        bp1 = new BuildJPanel(fl,dim,col,false,false);
        // Retrieve the JPanel and make it the main JPanel.
        mainPanel = bp1.getJPanel();
        // Add the main JPanel to the JInternalFrame
        ew.add(mainPanel);

        // Set up sub JPanel - for WorldWind Earth model
        //**********************************************************************
        int[] fl2  = {1, 3, 3};       // Flow control numbers
        int[] dim2 = {1480, 960};      // Dimensions
        int[] col2 = {192, 192, 192};   // RGB Color numbers

        // Build the custom BuildJPanel object - it contains 
        // the specified JPanel.  This JPanel will contain the Earth model.
        bp2 = new BuildJPanel(fl2,dim2,col2,true,true);
        // Retrieve the JPanel and make it the subpanel.
        subPanel1 = bp2.getJPanel();

        // Add the subpanel to the main panel.
        mainPanel.add(subPanel1);

        // Build a WorldWindowGLCanvas object.
        // Note that a WorldWindowsGLCanvas acts like a JPanel object.
        wwd = new WorldWindowGLCanvas();

        // Build a Model object.
        Model m = (Model)WorldWind.createConfigurationComponent
                (AVKey.MODEL_CLASS_NAME);

        wwd.setModel(m);

        // Load WorldWind model into the sub JPanel
        subPanel1.add(wwd);
        // Make the main JInternalFrame visible
        ew.setVisible(true);

        // Add JInternalFrame to JDesktopPane
        FSimdesktop.add(ew,JLayeredPane.MODAL_LAYER);
        
     // Create and install the view controls layer and register a controller for it with the World Window.
        ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
        insertBeforePlacenames(wwd, viewControlsLayer);
        wwd.addSelectListener(new ViewControlsSelectListener(wwd, viewControlsLayer));
    }

    public void trajSim()
    {
        to = getTrajectoryObjects();
        to.loadWorldWindModel(wwd);

        while(true)
        {   
            to.propagateTrajectory();

            try
            {
                Thread.sleep(timeDelay);
            }
            catch(Exception err)
            {
                System.out.println("Time delay error: " + err + "\n");
            }
        }
    }
    
    private int timeDelay;
    
    public Trajectory getTrajectoryObjects()
    {
        return to;
    }
    
    public void loadTrajectoryObjects(Trajectory to)
    {
        this.to = to;
    }
    
    public void setTimeDelay(int timeDelay)
    {
        this.timeDelay = timeDelay;
    }
    
    public WorldWindowGLCanvas getWorldWindowGLCanvas()
    {
        return wwd;
    }
    
    public static void insertBeforePlacenames(WorldWindow wwd, Layer layer)
    {
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof PlaceNameLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition, layer);
    }
}