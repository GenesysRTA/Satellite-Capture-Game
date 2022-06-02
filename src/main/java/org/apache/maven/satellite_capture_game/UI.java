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
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

public final class UI
{
    private static final JInternalFrame ew = new JInternalFrame("Earth View");
    private boolean firstRun = true;

    private final JPanel mainPanel;
    private final JPanel subPanel;
    private BufferedImage icon;
    private Image bgImg;

    private final WorldWindowGLCanvas wwd;

    private Trajectory t, s;

    public UI(String[] args) throws IOException
    {
    	try {
            bgImg = ImageIO.read(new File("E:\\Licenta\\satellite-capture-game\\src\\main\\java\\bg.jpg"));
            icon = ImageIO.read(new File("E:\\Licenta\\satellite-capture-game\\src\\main\\java\\space.png"));
            for (int x = 0; x < icon.getWidth(); x++) {
                for (int y = 0; y < icon.getHeight(); y++) {
                    int rgba = icon.getRGB(x, y);
                    Color col = new Color(rgba, true);
                    col = new Color(255 - col.getRed(),
                                    255 - col.getGreen(),
                                    255 - col.getBlue());
                    icon.setRGB(x, y, col.getRGB());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	
        JFrame mainFrame = new JFrame("Satellite Capture Game");

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int Width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        mainFrame.setSize(Width - 150, height - 150);

        mainFrame.setVisible(true);  
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setResizable(true);
        
        try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        JLabel menuTitle = new JLabel("Menu");
        menuTitle.setBounds(170, 25, 200, 50);
        menuTitle.setForeground(Color.WHITE);
        menuTitle.setFont(new Font("Serif", Font.BOLD, 28));
        mainFrame.add(menuTitle);
        
        Thread thread = Thread.currentThread();
        Buttons buttons = new Buttons(mainFrame, thread, args);
        buttons.start();
        
        JLabel bestScoresTitle = new JLabel("Best Scores");
        bestScoresTitle.setBounds(140, 395, 200, 50);
        bestScoresTitle.setForeground(Color.WHITE);
        Font font = new Font("Serif", Font.PLAIN, 28);
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        bestScoresTitle.setFont(font.deriveFont(attributes));
        mainFrame.add(bestScoresTitle);
        
        BestScoresTable table = new BestScoresTable();
        JScrollPane pane = table.getScrollPane();
        pane.setVisible(true);
        mainFrame.add(pane);

        JDesktopPane desktopPane = new JDesktopPane() {
			private static final long serialVersionUID = 7225728388897955897L;

			@Override
            protected void paintComponent(Graphics grphcs) {
                super.paintComponent(grphcs);
                grphcs.drawImage(bgImg, 0, 0, null);
            }
        };
        desktopPane.setVisible(true);

        mainFrame.add(desktopPane);

        ImageIcon frameIcon = new ImageIcon(icon);
        ew.setResizable(true);
        ew.setSize(1500,1000);
        ew.setFrameIcon(frameIcon);
        ew.setMaximumSize(new Dimension(1150,1000));
        
        // Immobilize JInternalFrame
        for(MouseListener listener : ((javax.swing.plaf.basic.BasicInternalFrameUI) ew.getUI()).getNorthPane().getMouseListeners()){
        	((javax.swing.plaf.basic.BasicInternalFrameUI) ew.getUI()).getNorthPane().removeMouseListener(listener);
        }
        
        ew.setLocation(415, 10);

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
        t = getTargetTrajectoryObject();
        s = getSourceTrajectoryObject();

        while(true)
        {   
            t.propagateTrajectory(timeDelay);
            s.propagateTrajectory(timeDelay);
            
            try
            {
                Thread.sleep(timeDelay);
            }
            catch(Exception err)
            {
                System.out.println("Time delay error: " + err + "\n");
            }
            if (firstRun) {
            	Thread.currentThread().suspend();
            	firstRun = false;
            }
        }
    }
    
    private int timeDelay;
    
    public Trajectory getTargetTrajectoryObject()
    {
        return t;
    }
    
    public Trajectory getSourceTrajectoryObject()
    {
        return s;
    }
    
    public void loadTrajectoryObjects(Trajectory t, Trajectory s)
    {
        this.t = t;
        this.s = s;
    }
    
    public void setTimeDelay(int timeDelay)
    {
        this.timeDelay = timeDelay;
    }
    
    public WorldWindowGLCanvas getWorldWindowGLCanvas()
    {
        return wwd;
    }
    
    public static void insertBeforePlacenames(WorldWindow wwd2, Layer layer)
    {
        int compassPosition = 0;
        LayerList layers = wwd2.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof PlaceNameLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition, layer);
    }
}