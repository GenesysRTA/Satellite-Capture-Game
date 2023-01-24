package org.apache.maven.satellite_capture_game;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {

	private static final long serialVersionUID = -7613283712124073985L;
	
	// Constructor
	public TableModel() {
		
		// Call the constructor of teh parent class
		super();
	}

	// Method used for declaring that the cells of the table are not editable
	@Override
	public boolean isCellEditable(int row, int col) {
	    return false;
	}
}
