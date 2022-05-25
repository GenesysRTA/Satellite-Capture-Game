package org.apache.maven.satellite_capture_game;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class BestScoresTable {

	private JScrollPane MyScrollPane;
	
	public BestScoresTable() {
		
		TableModel MyTablemodel = new TableModel();
		
        JTable MyTableView = new JTable(MyTablemodel);
        MyTableView.setRowHeight(40);
        MyTableView.setFocusable(false);
        MyTableView.setRowSelectionAllowed(false);
        
        MyScrollPane= new JScrollPane(MyTableView);
        MyScrollPane.setBounds(55, 450, 300, 423);
        
        MyTablemodel.addColumn("Place");
        MyTablemodel.addColumn("Distance");
        
        for (int i = 1; i < 11; i++) {
        	MyTablemodel.addRow(new Object[] {i, ""});
        }
        
        TableColumn tColumn1;
        TableColumn tColumn2;
        Color gray = new Color(100, 100, 100);
        TableColumnDesign centerRenderer = new TableColumnDesign(gray, Color.BLACK);
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tColumn1 = MyTableView.getColumnModel().getColumn(0);
        tColumn2 = MyTableView.getColumnModel().getColumn(1);
        tColumn1.setCellRenderer(centerRenderer);
        tColumn2.setCellRenderer(centerRenderer);
		
	}
	
	public JScrollPane getScrollPane() {
		return MyScrollPane;
	}

}
