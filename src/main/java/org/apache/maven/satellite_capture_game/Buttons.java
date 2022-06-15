package org.apache.maven.satellite_capture_game;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JFrame;

public class Buttons extends Thread {
	
	private Color blue = new Color(127, 127, 255);
	private Color green = new Color(127, 255, 127);
	private Color yellow = new Color(255, 255, 127);
	private Color red = new Color(255, 127, 127);
	
	private RoundedButton btnStart = new RoundedButton("Start", 25, blue);
	private RoundedButton btnPause = new RoundedButton("Pause", 25, green);
	private RoundedButton btnRestart = new RoundedButton("Try Again", 25, yellow);
	private RoundedButton btnExit = new RoundedButton("Exit", 25, red);
	
	private ButtonsController controllerStart;
	private ButtonsController controllerPause;
	private ButtonsController controllerRestart;
	private ButtonsController controllerExit;
	
	public Buttons(JFrame mainFrame, Thread thread, String[] args) {
        controllerStart = new ButtonsController(btnStart, thread, btnPause, btnRestart);
        btnStart.setBounds(55, 240, 300, 50);
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainFrame.add(btnStart);
        
        btnPause.setBounds(55, 310, 300, 50);
        btnPause.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPause.setEnabled(false);
        controllerPause = new ButtonsController(btnPause, thread);
        mainFrame.add(btnPause);
        
        btnRestart.setEnabled(false);
        btnRestart.setBounds(55, 380, 300, 50);
        btnRestart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        controllerRestart = new ButtonsController(btnRestart, thread, args);
        mainFrame.add(btnRestart);
        
        btnExit.setBounds(55, 450, 300, 50);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        controllerExit = new ButtonsController(btnExit, thread);
        mainFrame.add(btnExit);
	}
	
	public void run() {
		btnStart.addActionListener(controllerStart);
		btnPause.addActionListener(controllerPause);
		btnRestart.addActionListener(controllerRestart);
		btnExit.addActionListener(controllerExit);
	}
}
