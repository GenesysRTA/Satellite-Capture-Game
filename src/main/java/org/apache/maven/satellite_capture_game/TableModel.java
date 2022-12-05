package org.apache.maven.satellite_capture_game;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {

	private static final long serialVersionUID = -7613283712124073985L;
	
	public TableModel() {
		super();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
	    return false;
	}
}
