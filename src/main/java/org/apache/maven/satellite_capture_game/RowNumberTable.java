package org.apache.maven.satellite_capture_game;

import javax.swing.*;
import javax.swing.table.*;

public class RowNumberTable extends JTable {
	private static final long serialVersionUID = 1L;
	
	// Variable for storing the table
	private JTable main;

	// Constructor
	public RowNumberTable(JTable table) {
		
		// Set the table
		main = table;
		
		// Set the table listener
		main.getModel().addTableModelListener( this );

		// Set table settings
		setFocusable( false );
		setAutoCreateColumnsFromModel( false );
		setSelectionModel( main.getSelectionModel() );

		// Define table column
		TableColumn column = new TableColumn();
		
		// Set the header value of the column
		column.setHeaderValue(" ");
		
		// Add the column to the table
		addColumn( column );

		// Set column settings
		getColumnModel().getColumn(0).setPreferredWidth(50);
		setPreferredScrollableViewportSize(getPreferredSize());
	}

	// Method used for getting the number of rows
	@Override
	public int getRowCount() {
		return main.getRowCount();
	}

	// Method used for getting the height of the row
	@Override
	public int getRowHeight(int row) {
		int rowHeight = main.getRowHeight(row);

		if (rowHeight != super.getRowHeight(row))
		{
			super.setRowHeight(row, rowHeight);
		}

		return rowHeight;
	}

	// Method used for getting the value of the specific row and column
	@Override
	public Object getValueAt(int row, int column) {
		return Integer.toString(row + 1);
	}

	// Method used for declaring that the cells of the table are not editable
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
}
