package org.apache.maven.satellite_capture_game;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;

import javax.swing.JButton;

class RoundedButton extends JButton {

	private static final long serialVersionUID = -5871741219891486130L;
	
	// Variable for storing the corner radius
	private int radius;
	
	// Variable for storing the background color
	private Color bgColor;

	// Constructor
	public RoundedButton(String text, int radius, Color bgColor) {
		
		// Call the constructor of the parent class
        super(text);

        // Set the size
        Dimension size = getPreferredSize();
        
        // Set other settings
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);
        setContentAreaFilled(false);
        
        // Set the radius
        this.radius = radius;
        
        // Set the background color
        this.bgColor = bgColor;
    }

	// Method used for drawing the button
    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.GRAY);
        } else {
            g.setColor(bgColor);
        }
        setForeground(Color.BLACK);
        g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, radius, radius);
        g.setFont(new Font("Cambria", Font.PLAIN, 28));

        super.paintComponent(g);
    }

    // Method used for for drawing the border
    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, radius, radius);
    }
}
