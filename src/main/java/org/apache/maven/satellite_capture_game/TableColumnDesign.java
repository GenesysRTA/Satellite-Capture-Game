package org.apache.maven.satellite_capture_game;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableColumnDesign extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 7217799136079231243L;
	private Color backgroundColor, foregroundColor;
	
	public TableColumnDesign(Color backgroundColor, Color foregroundColor) {
	   super();
	   this.backgroundColor = backgroundColor;
	   this.foregroundColor = foregroundColor;
	   this.setHorizontalTextPosition(JLabel.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	      Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	      cell.setBackground(backgroundColor);
	      if (row == 0) {
	    	  cell.setForeground(Color.YELLOW);
	      } else if (row == 1) {
	    	  cell.setForeground(new Color(200, 200, 200));
	      } else if (row == 2) {
	    	  cell.setForeground(Color.ORANGE);
	      }else {
	    	  cell.setForeground(foregroundColor);
	      }
	      return cell;
	   }
	
}
