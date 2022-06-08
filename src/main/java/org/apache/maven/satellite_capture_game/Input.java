package org.apache.maven.satellite_capture_game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Input {
	
	private JLabel nameLabel;
	private JLabel forceLabel;
	private JLabel angleLabel;
	
	private JTextField nameField;
	private JTextField forceField;
	private JTextField angleField;
	
	private JButton save;
	
	public Input(JFrame mainFrame, UI ui) {
		
		nameLabel = new JLabel("Name");
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Serif", Font.BOLD, 28));
		nameLabel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		forceLabel = new JLabel("Force (m/s)");
		forceLabel.setForeground(Color.WHITE);
		forceLabel.setFont(new Font("Serif", Font.BOLD, 20));
		forceLabel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		angleLabel = new JLabel("Angle (°)");
		angleLabel.setForeground(Color.WHITE);
		angleLabel.setFont(new Font("Serif", Font.BOLD, 20));
		angleLabel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		nameField = new JTextField();
		forceField = new JTextField();
		angleField = new JTextField();
		
		save = new JButton("Save");
		save.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		nameLabel.setBounds(50, 73, 200, 25);
		forceLabel.setBounds(25, 113, 200, 25);
		angleLabel.setBounds(45, 153, 200, 25);
		
		nameField.setBounds(125, 75, 200, 25);
		forceField.setBounds(125, 115, 200, 25);
		angleField.setBounds(125, 155, 200, 25);
		
		nameField.setText("Anonymous");
		forceField.setText("0");
		angleField.setText("0");
		
		save.setBounds(335, 90, 65, 80);
		
		mainFrame.add(nameLabel);
		mainFrame.add(forceLabel);
		mainFrame.add(angleLabel);
		
		mainFrame.add(nameField);
		mainFrame.add(forceField);
		mainFrame.add(angleField);
		
		nameField.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				nameField.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {}
	    });
		
		forceField.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				forceField.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {}
	    });
		
		angleField.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				angleField.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {}
	    });
		
		mainFrame.add(save);
		
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Variables.name = nameField.getText();
				Variables.force = Double.parseDouble(forceField.getText());
				Variables.angle = Double.parseDouble(angleField.getText());
			}
		});
		
	}

}
