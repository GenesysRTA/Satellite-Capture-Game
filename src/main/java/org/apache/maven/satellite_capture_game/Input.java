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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import com.jogamp.newt.event.KeyEvent;

public class Input {
	
	// Variable used for storing the name label
	private JLabel nameLabel;
	
	// Variable used for storing the force label
	private JLabel forceLabel;
	
	// Variable used for storing the angle label
	private JLabel angleLabel;
	
	// Variable used for storing the name field
	private JTextField nameField;
	
	// Variable used for storing the force spinner
	private JSpinner forceField;
	
	// Variable used for storing the angle field
	private JTextField angleField;
	
	// Variable used for storing the save button
	private JButton save;
	
	// Variable used for storing the clear button
	private JButton clear;
	
	// Variable used for storing the font
	private static String fontFamily = "Serif";
	
	// Constructor
	public Input(JFrame mainFrame) {
		// Create the name label
		nameLabel = new JLabel("Name");
		
		// Add name label settings
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font(fontFamily, Font.BOLD, 28));
		nameLabel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		// Create the force label
		forceLabel = new JLabel("Velocity (m/s)");
		
		// Add force label settings
		forceLabel.setForeground(Color.WHITE);
		forceLabel.setFont(new Font(fontFamily, Font.BOLD, 20));
		forceLabel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		// Create the angle label
		angleLabel = new JLabel("Angle (°)");
		
		// Add force label settings
		angleLabel.setForeground(Color.WHITE);
		angleLabel.setFont(new Font(fontFamily, Font.BOLD, 20));
		angleLabel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		// Create the name field
		nameField = new JTextField();
		
		// Create the force spinner
		forceField = new JSpinner();
		
		// Add force spinner settings
		forceField.setModel(new SpinnerNumberModel(0, -150, 150, 0.1));
		
		// Create the angle field
		angleField = new JTextField();
		
		// Create the save button
		save = new JButton("Save");
		
		// Add save button settings
		save.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// Create the clear button
		clear = new JButton("Clear");
		
		// Add clear button settings
		clear.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// Add additional settings
		nameLabel.setBounds(50, 73, 200, 25);
		forceLabel.setBounds(5, 113, 200, 25);
		angleLabel.setBounds(45, 153, 200, 25);
		
		nameField.setBounds(125, 75, 185, 25);
		nameField.setHorizontalAlignment(SwingConstants.CENTER);
		forceField.setBounds(125, 115, 200, 25);
		JComponent forceFieldEditor = forceField.getEditor();
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)forceFieldEditor;
		spinnerEditor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
		angleField.setBounds(125, 155, 185, 25);
		angleField.setHorizontalAlignment(SwingConstants.CENTER);
		
		nameField.setText("Anonymous");
		angleField.setText("0");
		
		save.setBounds(335, 90, 65, 80);
		clear.setBounds(100, 982, 200, 25);
		
		// Add all 3 label to the frame
		mainFrame.add(nameLabel);
		mainFrame.add(forceLabel);
		mainFrame.add(angleLabel);
		
		// Add all 3 fields to the frame
		mainFrame.add(nameField);
		mainFrame.add(forceField);
		mainFrame.add(angleField);
		
		// Add a listener to the name field
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
		
		//Add the clear button to the force field
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
		
		// Add a listener to the angle field
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
		
		//Add the save button to the frame
		mainFrame.add(save);
		
		// Add a listener to the save button
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VariablesUtils.setName(nameField.getText());
				try {
					forceField.commitEdit();
				} catch (ParseException ex) {
					Logger logger = Logger.getLogger(Main.class.getName());
		        	logger.log(Level.INFO, Arrays.toString(ex.getStackTrace()));
				}
				VariablesUtils.setForce(Double.parseDouble(forceField.getValue().toString()));
				VariablesUtils.setAngle(Double.parseDouble(angleField.getText()));
			}
		});
		
		//Add the clear button to the frame
		mainFrame.add(clear);
		
		// Add a listener to the clear button
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BestScoresTable.clearLeaderboard();
			}
		});
	}
}
