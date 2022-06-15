package org.apache.maven.satellite_capture_game;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class BestScoresTable {

	private JScrollPane scrollPane;
	private static TableModel tablemodel;
	
	public BestScoresTable() {
		tablemodel = new TableModel();
		
		JTable table = new JTable(tablemodel);
		table.setRowHeight(40);
		table.setFocusable(false);
		table.setRowSelectionAllowed(false);
        
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(55, 550, 300, 423);
        
        tablemodel.addColumn("Name");
        tablemodel.addColumn("Distance");
        
        JTable places = new RowNumberTable(table);
        places.setRowSelectionAllowed(false);
        scrollPane.setRowHeaderView(places);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, places.getTableHeader());
        
        try {
        	File file = new File("E:\\Licenta\\satellite-capture-game\\src\\main\\java\\leaderboard.txt");
        	try (Scanner scanner = new Scanner(file)) {
				while (scanner.hasNextLine()) {
				    String data = scanner.nextLine();
				    String[] components = data.split(" ");
				    tablemodel.addRow(new Object[] {components[0], components[1] + " m"});
				}
			}
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        TableColumn placesColumn;
        TableColumn tColumn1;
        TableColumn tColumn2;
        
        Color gray = new Color(100, 100, 100);
        
        TableColumnDesign centerRenderer = new TableColumnDesign(gray, Color.BLACK);
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        placesColumn = places.getColumnModel().getColumn(0);
        
        tColumn1 = table.getColumnModel().getColumn(0);
        tColumn2 = table.getColumnModel().getColumn(1);
        
        placesColumn.setCellRenderer(centerRenderer);
        
        tColumn1.setCellRenderer(centerRenderer);
        tColumn2.setCellRenderer(centerRenderer);
		
	}
	
	public static void CheckAndPlace(double distance) {
		for (int i = 0; i < tablemodel.getRowCount(); i++) {
			
			String prevValue = tablemodel.getValueAt(i, 1).toString();
			String[] components = prevValue.split(" ");
			double prevScore = Double.parseDouble(components[0]);
			
			if (distance < prevScore || prevScore == 0.0f) {
				ShiftRows(i);
				
				tablemodel.setValueAt(VariablesUtils.getName(), i, 0);
				tablemodel.setValueAt((int) distance + " m", i, 1);
				
				SaveInFile(i, distance);
				
				break;
			}
		}
	}
	
	public JScrollPane getScrollPane() {
		return this.scrollPane;
	}
	
	private static void ShiftRows(int position) {
		for (int i = tablemodel.getRowCount() - 2; i >= position; i--) {
			tablemodel.moveRow(i, i, i + 1);
		}
	}
	
	private static void SaveInFile(int position, double distance) {
		try {
			List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get("E:\\Licenta\\satellite-capture-game\\src\\main\\java", "leaderboard.txt"), StandardCharsets.UTF_8));

			if (!fileContent.get(position).equals("- 0")) {
				for (int i = fileContent.size() - 2; i >= position; i--) {
					int post = i + 1;
					fileContent.set(post, fileContent.get(i));
				}
			}
			fileContent.set(position, VariablesUtils.getName() + " " + (int) distance);
			
			Files.write(Paths.get("E:\\Licenta\\satellite-capture-game\\src\\main\\java", "leaderboard.txt"), fileContent, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void ClearLeaderboard() {
		try {
			List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get("E:\\Licenta\\satellite-capture-game\\src\\main\\java", "leaderboard.txt"), StandardCharsets.UTF_8));

			for (int i = 0; i < fileContent.size(); i++) {
				fileContent.set(i, "- 0");
				tablemodel.setValueAt("-", i, 0);
				tablemodel.setValueAt("0 m", i, 1);
			}
			
			Files.write(Paths.get("E:\\Licenta\\satellite-capture-game\\src\\main\\java", "leaderboard.txt"), fileContent, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
