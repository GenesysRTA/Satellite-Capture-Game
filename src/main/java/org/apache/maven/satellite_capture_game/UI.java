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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public final class UI
{
    private final JInternalFrame ew;

    private final JPanel mainPanel;
    private final JPanel subPanel;

    private final WorldWindowGLCanvas wwd;

    private Trajectory t;

    public UI()
    {
        JFrame mainFrame = new JFrame("Project");

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int Width = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        mainFrame.setSize(Width - 150, height - 150);

        mainFrame.setVisible(true);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setResizable(true);


        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(230, 230, 230));
        desktopPane.setVisible(true);

        mainFrame.add(desktopPane);


        ew = new JInternalFrame("Earth View");

        ew.setResizable(true);
        ew.setSize(1500,1000);
        ew.setMaximumSize(new Dimension(1150,1000));
        
        // Immobilize JInternalFrame
        for(MouseListener listener : ((javax.swing.plaf.basic.BasicInternalFrameUI) ew.getUI()).getNorthPane().getMouseListeners()){
        	((javax.swing.plaf.basic.BasicInternalFrameUI) ew.getUI()).getNorthPane().removeMouseListener(listener);
        }
        
        ew.setLocation(415,10);

        mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setMinimumSize(new Dimension(1500, 1000));
        mainPanel.setPreferredSize(new Dimension(1500, 1000));
        mainPanel.setBackground(new Color(160, 160, 160));
        mainPanel.setLayout(new FlowLayout(1, 3, 3));
        ew.add(mainPanel);

        subPanel = new JPanel();
        subPanel.setOpaque(true);
        subPanel.setMinimumSize(new Dimension(1480, 960));
        subPanel.setPreferredSize(new Dimension(1480, 960));
        subPanel.setBackground(new Color(192, 192, 192));
        subPanel.setLayout(new BorderLayout());
        subPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        mainPanel.add(subPanel);

        wwd = new WorldWindowGLCanvas();

        Model m = (Model)WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);

        wwd.setModel(m);

        subPanel.add(wwd);
        ew.setVisible(true);

        desktopPane.add(ew,JLayeredPane.MODAL_LAYER);
        
        ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
        insertBeforePlacenames(wwd, viewControlsLayer);
        wwd.addSelectListener(new ViewControlsSelectListener(wwd, viewControlsLayer));
    }

    public void trajectorySimulation()
    {
        t = getTrajectoryObject();
        t.loadWorldWindModel(wwd);

        while(true)
        {   
            t.propagateTrajectory();

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
    
    public Trajectory getTrajectoryObject()
    {
        return t;
    }
    
    public void loadTrajectoryObject(Trajectory t)
    {
        this.t = t;
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