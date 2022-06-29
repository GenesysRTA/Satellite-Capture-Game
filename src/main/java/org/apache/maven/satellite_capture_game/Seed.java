package org.apache.maven.satellite_capture_game;

import java.util.Random;

public class Seed {
	private double ma;
	private double i;
	private double raan;
	
	public Seed() {
		Random rand = new Random();
		
		this.ma = rand.nextDouble() * 360.0;
		this.i = rand.nextDouble() * 90.0;
		this.raan = rand.nextDouble() * 360.0;
	}
	
	public double getMa() {
		return this.ma;
	}
	
	public double getI() {
		return this.i;
	}

	public double getRAAN() {
		return this.raan;
	}
}
