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

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;

public class BestScoresTable {

	// Variable used for storing the scroll pane
	private JScrollPane scrollPane;
	
	// Variable used for storing the table model
	private static TableModel tablemodel = new TableModel();
	
	// Variable used for storing the path to the text file
	private static String path = "C:\\FinalizareStudii\\Satellite-Capture-Game\\src\\main\\java";
	
	// Variable used for storing the name of the file
	private static String leaderboardFile = "leaderboard.txt";
	
	// Constructor
	public BestScoresTable() {
		
		// Define the table object
		JTable table = new JTable(tablemodel);
		
		// Set table settings
		table.setRowHeight(40);
		table.setFocusable(false);
		table.setRowSelectionAllowed(false);
        
		// Define a scroll pane for the table
        scrollPane = new JScrollPane(table);
        
        // Set scroll pane settings
        scrollPane.setBounds(55, 550, 300, 423);
        
        // Define the 2 columns of the table
        tablemodel.addColumn("Name");
        tablemodel.addColumn("Distance");
        
        // Define the table for the rankings
        JTable places = new RowNumberTable(table);
        
        // Set rankings table settings
        places.setRowSelectionAllowed(false);
        
        // Add the rankings table to the original table
        scrollPane.setRowHeaderView(places);
        scrollPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, places.getTableHeader());
        
        // Use file for getting the data from the text file
        try {
        	
        	// Find the file
        	File file = VariablesUtils.getResourceFile("./src/main/java/leaderboard.txt");
        	
        	// Use scanner to read the lines from the file
        	try (Scanner scanner = new Scanner(file)) {
        		
        		// If there are more lines to read...
				while (scanner.hasNextLine()) {
					
					// Save the line
				    String data = scanner.nextLine();
				    
				    // Split the data by empty space
				    String[] components = data.split(" ");
				    
				    // Create a row in the table with the data
				    tablemodel.addRow(new Object[] {components[0], components[1] + " m"});
				}
			}
        	
        // If there is an exception...
        } catch (FileNotFoundException e) {
        	
        	// Print the stack trace
            e.printStackTrace();
        }
        
        // Define the column for the ranking
        TableColumn placesColumn;
        
        // Define the column for the name of the player
        TableColumn tColumn1;
        
        // Define the score of the player
        TableColumn tColumn2;
        
        // Define the background color of the table
        Color gray = new Color(100, 100, 100);
        
        // Set the settings for the columns
        TableColumnDesign centerRenderer = new TableColumnDesign(gray, Color.BLACK);
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Set the column for the rankings tabel
        placesColumn = places.getColumnModel().getColumn(0);
        
        // Set the columns for the base table
        tColumn1 = table.getColumnModel().getColumn(0);
        tColumn2 = table.getColumnModel().getColumn(1);
        
        // Display the value for the rankings table
        placesColumn.setCellRenderer(centerRenderer);
        
        // Display the values for the base table
        tColumn1.setCellRenderer(centerRenderer);
        tColumn2.setCellRenderer(centerRenderer);
		
	}
	
	// Method used for replacing the data in the table
	public static void checkAndPlace(double distance) {
		
		// For each row of the table...
		for (int i = 0; i < tablemodel.getRowCount(); i++) {
			
			// Variable used for storing the present value of the data from the row
			String prevValue = tablemodel.getValueAt(i, 1).toString();
			
			// Variable used for storing each piece of data from that row
			String[] components = prevValue.split(" ");
			
			// Variable used for storing the present score of the player as Double
			double prevScore = Double.parseDouble(components[0]);
			
			// Check if the new score is better the the previous one or if the previous one is equal to 0 and if it is...
			if (distance < prevScore || prevScore == 0.0f) {
				
				// Shift the row one position down
				shiftRows(i);
				
				// Set the new values in the current row
				tablemodel.setValueAt(VariablesUtils.getName(), i, 0);
				tablemodel.setValueAt((int) distance + " m", i, 1);
				
				// Save the data in the text file
				saveInFile(i, distance);
				
				// Exit loop
				break;
			}
		}
	}
	
	// Getter - ScrollPane
	public JScrollPane getScrollPane() {
		return this.scrollPane;
	}
	
	// Method used for shifting the rows of the table down
	private static void shiftRows(int position) {
		
		// For each table row that can be shifted
		for (int i = tablemodel.getRowCount() - 2; i >= position; i--) {
			
			// Shift it down 
			tablemodel.moveRow(i, i, i + 1);
		}
	}
	
	// Method used for saving the new data in the file
	private static void saveInFile(int position, double distance) {
		
		// Use list to read all the lines from the file
		try {
			
			// Variable used for storing all the lines from the text file
			List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(path, leaderboardFile), StandardCharsets.UTF_8));

			// Check if the previous score is equal to 0 and if it is not...
			if (!fileContent.get(position).equals("- 0")) {
				
				// For each relevant row that is affected by change
				for (int i = fileContent.size() - 2; i >= position; i--) {
					
					// Set the data
					int post = i + 1;
					fileContent.set(post, fileContent.get(i));
				}
			}
			
			// Replace the previous data with the new one
			fileContent.set(position, VariablesUtils.getName() + " " + (int) distance);
			
			// Save the changes in the file
			Files.write(Paths.get(path, leaderboardFile), fileContent, StandardCharsets.UTF_8);
		
		// If there is an exception...
		} catch (IOException e) {
			
			// Print the stack trace
			e.printStackTrace();
		}
	}
	
	// Method used to clear the rankings when the clear button is pressed
	public static void clearLeaderboard() {
		
		// Use list to read all the lines from the file
		try {
			
			// Variable used for storing all the lines from the text file
			List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(path, leaderboardFile), StandardCharsets.UTF_8));

			// For each line of the file
			for (int i = 0; i < fileContent.size(); i++) {
				
				// Set the values to the default state
				fileContent.set(i, "- 0");
				tablemodel.setValueAt("-", i, 0);
				tablemodel.setValueAt("0 m", i, 1);
			}
			
			// Save the changes in the file
			Files.write(Paths.get(path, leaderboardFile), fileContent, StandardCharsets.UTF_8);
		
		// If there is an exception...
		} catch (IOException e) {
			
			// Print the stack trace
			e.printStackTrace();
		}
	}
}
