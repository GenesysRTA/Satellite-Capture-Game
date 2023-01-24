package org.apache.maven.satellite_capture_game;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class TableColumnDesign extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 7217799136079231243L;
	
	// Variable used for storing the background color
	private Color bgColor;
	
	// Variable used for storing the font color
	private Color color;
	
	// Constructor
	public TableColumnDesign(Color bgColor, Color color) {
		
	   // Call the constructor of the parent class
	   super();
	   // Set the background color
	   this.bgColor = bgColor;
	   
	   // Set the font color
	   this.color = color;
	   
	   // Set the text position to center
	   this.setHorizontalTextPosition(SwingConstants.CENTER);
	}

	// Method used for displaying the table elements
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	      
		  // Declare the cell element
		  Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	      
		  // Set cell background color
	      cell.setBackground(bgColor);
	      
	      // Check if the row is the first one and if it is
	      if (row == 0) {
	    	  
	    	  // Set text color to yellow
	    	  cell.setForeground(Color.YELLOW);
	    	  
	      // Check if the row is the second one and if it is
	      } else if (row == 1) {
	    	  
	    	  // Set text color to gray
	    	  cell.setForeground(new Color(200, 200, 200));
	    	  
	      // Check if the row is the third one and if it is
	      } else if (row == 2) {
	    	  // Set text color to orange
	    	  cell.setForeground(Color.ORANGE);
	    	  
	      // For any other row
	      }else {
	    	  
	    	  // Set the font color to the default one
	    	  cell.setForeground(color);
	      }
	      
	      // Get the cell
	      return cell;
	}
}
