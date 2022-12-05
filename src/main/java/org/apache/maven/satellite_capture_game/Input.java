package org.apache.maven.satellite_capture_game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.jogamp.newt.event.KeyEvent;

public class Input {
	
	private JLabel nameLabel;
	private JLabel forceLabel;
	private JLabel angleLabel;
	
	private JTextField nameField;
	private JSpinner forceField;
	private JTextField angleField;
	
	private JButton save;
	private JButton clear;
	
	private static String fontFamily = "Serif";
	
	public Input(JFrame mainFrame) {
		nameLabel = new JLabel("Name");
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font(fontFamily, Font.BOLD, 28));
		nameLabel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		forceLabel = new JLabel("Velocity (m/s)");
		forceLabel.setForeground(Color.WHITE);
		forceLabel.setFont(new Font(fontFamily, Font.BOLD, 20));
		forceLabel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		angleLabel = new JLabel("Angle (°)");
		angleLabel.setForeground(Color.WHITE);
		angleLabel.setFont(new Font(fontFamily, Font.BOLD, 20));
		angleLabel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		nameField = new JTextField();
		forceField = new JSpinner();
		forceField.setModel(new SpinnerNumberModel(0, -150, 150, 0.1));
		angleField = new JTextField();
		
		save = new JButton("Save");
		save.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		clear = new JButton("Clear");
		clear.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		nameLabel.setBounds(50, 73, 200, 25);
		forceLabel.setBounds(5, 113, 200, 25);
		angleLabel.setBounds(45, 153, 200, 25);
		
		nameField.setBounds(125, 75, 185, 25);
		nameField.setHorizontalAlignment(JTextField.CENTER);
		forceField.setBounds(125, 115, 200, 25);
		JComponent forceFieldEditor = forceField.getEditor();
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)forceFieldEditor;
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		angleField.setBounds(125, 155, 185, 25);
		angleField.setHorizontalAlignment(JTextField.CENTER);
		
		nameField.setText("Anonymous");
		angleField.setText("0");
		
		save.setBounds(335, 90, 65, 80);
		clear.setBounds(100, 982, 200, 25);
		
		mainFrame.add(nameLabel);
		mainFrame.add(forceLabel);
		mainFrame.add(angleLabel);
		
		mainFrame.add(nameField);
		mainFrame.add(forceField);
		mainFrame.add(angleField);
		
		nameField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				nameField.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				// No need
			}
	    });
		
		spinnerEditor.getTextField().addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				spinnerEditor.getTextField().setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				// No need
			}
	    });
		
		KeyListener restrictListener = new KeyListener() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				// No need
			}

			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {
				// No need
			}

			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();
			    if (c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
			    	if (!(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == '.' || c == '-')) {
			    		e.consume();
			    	}
			    }
			}
		};
		
		forceField.addKeyListener(restrictListener);
		
		angleField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				angleField.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				// No need
			}
	    });
		
		angleField.addKeyListener(restrictListener);
		
		mainFrame.add(save);
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VariablesUtils.setName(nameField.getText());
				try {
					forceField.commitEdit();
				} catch (ParseException ex) {
					System.out.println(ex.getStackTrace());
				}
				VariablesUtils.setForce(Double.parseDouble(forceField.getValue().toString()));
				VariablesUtils.setAngle(Double.parseDouble(angleField.getText()));
			}
		});
		
		mainFrame.add(clear);
		
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BestScoresTable.clearLeaderboard();
			}
		});
	}
}
