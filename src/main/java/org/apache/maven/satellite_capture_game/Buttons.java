package org.apache.maven.satellite_capture_game;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JFrame;

public class Buttons extends Thread {
	
	// Variable storing the start button color
	private Color blue = new Color(127, 127, 255);
	
	// Variable storing the pause button color
	private Color green = new Color(127, 255, 127);
	
	// Variable storing the try again button color
	private Color yellow = new Color(255, 255, 127);
	
	// Variable storing the exit button color
	private Color red = new Color(255, 127, 127);
	
	// Variable storing the start button
	private RoundedButton btnStart = new RoundedButton("Start", 25, blue);
	
	// Variable storing the pause button
	private RoundedButton btnPause = new RoundedButton("Pause", 25, green);
	
	// Variable storing the try again button
	private RoundedButton btnRestart = new RoundedButton("Try Again", 25, yellow);
	
	// Variable storing the exit button
	private RoundedButton btnExit = new RoundedButton("Exit", 25, red);
	
	// Variable storing the start button controller
	private ButtonsController controllerStart;
	
	// Variable storing the pause button controller
	private ButtonsController controllerPause;
	
	// Variable storing the restart button controller
	private ButtonsController controllerRestart;
	
	// Variable storing the exit button controller
	private ButtonsController controllerExit;
	
	// Constructor
	public Buttons(JFrame mainFrame, Thread thread, String[] args) {
		
		// Initialize the start button controller
        controllerStart = new ButtonsController(btnStart, thread, btnPause, btnRestart);
        
        // Set start button settings
        btnStart.setBounds(55, 240, 300, 50);
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add the start button in the scene
        mainFrame.add(btnStart);
        
        // Set pause button settings
        btnPause.setBounds(55, 310, 300, 50);
        btnPause.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPause.setEnabled(false);
        
        // Initialize the pause button controller
        controllerPause = new ButtonsController(btnPause, thread);
        
        // Add the pause button in the scene
        mainFrame.add(btnPause);
        
        // Set restart button settings
        btnRestart.setEnabled(false);
        btnRestart.setBounds(55, 380, 300, 50);
        btnRestart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Initialize the restart button controller
        controllerRestart = new ButtonsController(btnRestart, thread, args);
        
        // Add the restart button in the scene
        mainFrame.add(btnRestart);
        
        // Set exit button settings
        btnExit.setBounds(55, 450, 300, 50);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Initialize the exit button controller
        controllerExit = new ButtonsController(btnExit, thread);
        
        // Add the exit button in the scene
        mainFrame.add(btnExit);
	}
	
	// Method used for listening to the controllers in the background using different threads
	@Override
	public void run() {
		
		// Add listener for each controller
		btnStart.addActionListener(controllerStart);
		btnPause.addActionListener(controllerPause);
		btnRestart.addActionListener(controllerRestart);
		btnExit.addActionListener(controllerExit);
	}
}
