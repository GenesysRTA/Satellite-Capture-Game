package org.apache.maven.satellite_capture_game;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class ListenerController implements ActionListener {
	
	private RoundedButton button;
	private Thread thread;
	private String[] args;
	
	public ListenerController(RoundedButton button, Thread thread) {
		this.button = button;
		this.thread = thread;
	}
	
	public ListenerController(RoundedButton button, Thread thread, String[] args) {
		this.button = button;
		this.thread = thread;
		this.args = args;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (button.getText() == "Pause") {
			// ? Rankings disappear
			button.setText("Resume");
			thread.suspend();
		} else if (button.getText() == "Resume") {
			// Same
			button.setText("Pause");
			thread.resume();
		} else if (button.getText() == "Start") {
			thread.resume();
			button.setEnabled(false);
		} else if (button.getText() == "Try Again") {
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
		} else if (button.getText() == "Exit") {
			System.exit(0);
		}
		
	}

}
