package org.apache.maven.satellite_capture_game;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class TableColumnDesign extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 7217799136079231243L;
	
	private Color bgColor;
	private Color color;
	
	public TableColumnDesign(Color bgColor, Color color) {
	   super();
	   this.bgColor = bgColor;
	   this.color = color;
	   this.setHorizontalTextPosition(SwingConstants.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	      Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	      
	      cell.setBackground(bgColor);
	      if (row == 0) {
	    	  cell.setForeground(Color.YELLOW);
	      } else if (row == 1) {
	    	  cell.setForeground(new Color(200, 200, 200));
	      } else if (row == 2) {
	    	  cell.setForeground(Color.ORANGE);
	      }else {
	    	  cell.setForeground(color);
	      }
	      return cell;
	}
}
