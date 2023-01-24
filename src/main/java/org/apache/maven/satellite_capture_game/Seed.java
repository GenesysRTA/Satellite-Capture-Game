package org.apache.maven.satellite_capture_game;

import java.util.Random;

public class Seed {
	
	// Variable for storing the major axis
	private double ma;
	
	// Variable for storing the inclination
	private double i;
	
	// Variable for storing the right ascension of the ascending node
	private double raan;
	
	// Constructor
	public Seed() {
		
		// Define a variable to store a random number
		Random rand = new Random();
		
		// Generate MA
		this.ma = rand.nextDouble() * 360.0;
		
		// Generate I
		this.i = rand.nextDouble() * 90.0;
		
		// Generate RAAN
		this.raan = rand.nextDouble() * 360.0;
	}
	
	// Getter - MA
	public double getMa() {
		return this.ma;
	}
	
	// Getter - I
	public double getI() {
		return this.i;
	}

	// Getter - RAAN
	public double getRAAN() {
		return this.raan;
	}
}
