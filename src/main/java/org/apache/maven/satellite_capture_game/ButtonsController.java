package org.apache.maven.satellite_capture_game;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class ButtonsController implements ActionListener {
	
	// Variable used to store the controllers's button
	private RoundedButton currentButton;
	
	// Variable used to store the thread
	private Thread thread;
	
	// Variable used to store the program's arguments
	private String[] args;
	
	// Variable used to store the pause button
	private RoundedButton pauseButton;
	
	// Variable used to store the restart button
	private RoundedButton restartButton;
	
	// Constructor
	public ButtonsController(RoundedButton currentButton, Thread thread) {
		
		// Set the button's value
		this.currentButton = currentButton;
		
		// Set the thread value
		this.thread = thread;
	}
	
	// Constructor
	public ButtonsController(RoundedButton currentButton, Thread thread, RoundedButton pauseButton, RoundedButton restartButton) {
		
		// Set the button's value
		this.currentButton = currentButton;
		
		// Set the thread value
		this.thread = thread;
		
		// Set the pause button value
		this.pauseButton = pauseButton;
		
		// Set the restart button value
		this.restartButton = restartButton;
	}
	
	// Constructor
	public ButtonsController(RoundedButton currentButton, Thread thread, String[] args) {
		
		// Set the button's value
		this.currentButton = currentButton;
		
		// Set the thread value
		this.thread = thread;
		
		// Set the program arguments
		this.args = args;
	}
	
	// Method used for controlling the state machine
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent event) {
		
		// Check if the current text of the button is Pause and if it is...
		if (currentButton.getText().equals("Pause")) {
			
			// Change the text to Resume
			currentButton.setText("Resume");
			
			// Pause the game
			thread.suspend();
			
		// Check if the current text of the button is Resume and if it is...
		} else if (currentButton.getText().equals("Resume")) {
			
			// Change the text to Pause
			currentButton.setText("Pause");
			
			// Resume the game
			thread.resume();
			
		// Check if the current text of the button is Start and if it is...
		} else if (currentButton.getText().equals("Start")) {
			
			// Resume the game
			thread.resume();
			
			// Disable the start button
			currentButton.setEnabled(false);
			
			// Enable the pause Button
			pauseButton.setEnabled(true);
			
			// Enable the restart button
			restartButton.setEnabled(true);
			
		// Check if the current text of the button is Try Again and if it is...
		} else if (currentButton.getText().equals("Try Again")) {
			
			// Define the command to stop the application from running
			StringBuilder cmd = new StringBuilder();
	        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
	        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
	            cmd.append(jvmArg + " ");
	        }
	        cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
	        cmd.append(Main.class.getName()).append(" ");
	        for (String arg : args) {
	            cmd.append(arg).append(" ");
	        }
	        
	        // At runtime...
	        try {
	        	
	        	// Execute the command
				Runtime.getRuntime().exec(cmd.toString());
				
			// If there is an exception...
			} catch (IOException e) {
				
				// Print the stack trace
				e.printStackTrace();
			}
	        
	        // Close the application
	        System.exit(0);
	        
	    // Check if the current text of the button is Exit and if it is...
		} else if (currentButton.getText().equals("Exit")) {
			
			// Close the application
			System.exit(0);
		}
	}
}
