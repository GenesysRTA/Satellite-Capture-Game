package org.apache.maven.satellite_capture_game;

import javax.swing.*;
import javax.swing.table.*;

public class RowNumberTable extends JTable {
	private static final long serialVersionUID = 1L;
	
	private JTable main;

	public RowNumberTable(JTable table) {
		main = table;
		main.getModel().addTableModelListener( this );

		setFocusable( false );
		setAutoCreateColumnsFromModel( false );
		setSelectionModel( main.getSelectionModel() );


		TableColumn column = new TableColumn();
		column.setHeaderValue(" ");
		addColumn( column );

		getColumnModel().getColumn(0).setPreferredWidth(50);
		setPreferredScrollableViewportSize(getPreferredSize());
	}

	@Override
	public int getRowCount() {
		return main.getRowCount();
	}

	@Override
	public int getRowHeight(int row) {
		int rowHeight = main.getRowHeight(row);

		if (rowHeight != super.getRowHeight(row))
		{
			super.setRowHeight(row, rowHeight);
		}

		return rowHeight;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return Integer.toString(row + 1);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
}
