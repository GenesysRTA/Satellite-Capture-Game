package org.apache.maven.satellite_capture_game;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JFrame;

public class Buttons extends Thread {
	
	Color blue = new Color(127, 127, 255);
	Color green = new Color(127, 255, 127);
	Color yellow = new Color(255, 255, 127);
	Color red = new Color(255, 127, 127);
	
	RoundedButton btnStart = new RoundedButton("Start", 25, blue);
	RoundedButton btnPause = new RoundedButton("Pause", 25, green);
	RoundedButton btnRestart = new RoundedButton("Try Again", 25, yellow);
	RoundedButton btnExit = new RoundedButton("Exit", 25, red);
	
	ListenerController controllerStart;
	ListenerController controllerPause;
	ListenerController controllerRestart;
	ListenerController controllerExit;
	
    Thread thread;
	
	public Buttons(JFrame mainFrame, Thread thread, String[] args) {
		
		this.thread = thread;
		
        controllerStart = new ListenerController(btnStart, thread, btnPause, btnRestart);
        btnStart.setBounds(55, 80, 300, 50);
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainFrame.add(btnStart);
        
        btnPause.setBounds(55, 150, 300, 50);
        btnPause.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPause.setEnabled(false);
        controllerPause = new ListenerController(btnPause, thread);
        mainFrame.add(btnPause);
        
        btnRestart.setEnabled(false);
        btnRestart.setBounds(55, 220, 300, 50);
        btnRestart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        controllerRestart = new ListenerController(btnRestart, thread, args);
        mainFrame.add(btnRestart);
        
        btnExit.setBounds(55, 290, 300, 50);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        controllerExit = new ListenerController(btnExit, thread);
        mainFrame.add(btnExit);
		
	}
	
	public void run() {
		
		btnStart.addActionListener(controllerStart);
		btnPause.addActionListener(controllerPause);
		btnRestart.addActionListener(controllerRestart);
		btnExit.addActionListener(controllerExit);
		
	}

}
