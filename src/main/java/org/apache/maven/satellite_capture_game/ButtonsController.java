package org.apache.maven.satellite_capture_game;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class ButtonsController implements ActionListener {
	
	private RoundedButton currentButton;
	private Thread thread;
	private String[] args;
	private RoundedButton pauseButton;
	private RoundedButton restartButton;
	
	public ButtonsController(RoundedButton currentButton, Thread thread) {
		this.currentButton = currentButton;
		this.thread = thread;
	}
	
	public ButtonsController(RoundedButton currentButton, Thread thread, RoundedButton pauseButton, RoundedButton restartButton) {
		this.currentButton = currentButton;
		this.thread = thread;
		this.pauseButton = pauseButton;
		this.restartButton = restartButton;
	}
	
	public ButtonsController(RoundedButton currentButton, Thread thread, String[] args) {
		this.currentButton = currentButton;
		this.thread = thread;
		this.args = args;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent event) {
		if (currentButton.getText().equals("Pause")) {
			currentButton.setText("Resume");
			thread.suspend();
		} else if (currentButton.getText().equals("Resume")) {
			currentButton.setText("Pause");
			thread.resume();
		} else if (currentButton.getText().equals("Start")) {
			thread.resume();
			currentButton.setEnabled(false);
			pauseButton.setEnabled(true);
			restartButton.setEnabled(true);
		} else if (currentButton.getText().equals("Try Again")) {
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
		} else if (currentButton.getText().equals("Exit")) {
			System.exit(0);
		}
	}
}
