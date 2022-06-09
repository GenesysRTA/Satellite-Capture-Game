package org.apache.maven.satellite_capture_game;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class ListenerController implements ActionListener {
	
	private RoundedButton currentButton;
	private Thread thread;
	private String[] args;
	private RoundedButton pauseButton;
	private RoundedButton restartButton;
	
	public ListenerController(RoundedButton currentButton, Thread thread) {
		this.currentButton = currentButton;
		this.thread = thread;
	}
	
	public ListenerController(RoundedButton currentButton, Thread thread, RoundedButton pauseButton, RoundedButton restartButton) {
		this.currentButton = currentButton;
		this.thread = thread;
		this.pauseButton = pauseButton;
		this.restartButton = restartButton;
	}
	
	public ListenerController(RoundedButton currentButton, Thread thread, String[] args) {
		this.currentButton = currentButton;
		this.thread = thread;
		this.args = args;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent event) {
		if (currentButton.getText() == "Pause") {
			currentButton.setText("Resume");
			thread.suspend();
		} else if (currentButton.getText() == "Resume") {
			currentButton.setText("Pause");
			thread.resume();
		} else if (currentButton.getText() == "Start") {
			thread.resume();
			currentButton.setEnabled(false);
			pauseButton.setEnabled(true);
			restartButton.setEnabled(true);
		} else if (currentButton.getText() == "Try Again") {
			//
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
	        try {
				Runtime.getRuntime().exec(cmd.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
	        System.exit(0);
		} else if (currentButton.getText() == "Exit") {
			System.exit(0);
		}
		
	}

}
