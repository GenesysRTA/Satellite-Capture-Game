package org.apache.maven.satellite_capture_game;

import java.util.Random;

public class Seed {
	
	private int position;
	private int i;
	
	public Seed() {
		
		Random rand = new Random();
		
		this.position = rand.nextInt(360) + 100;
		this.i = rand.nextInt(360) + 1;
		
	}
	
	public int getPosition() {
		
		return this.position;
		
	}
	
	public int getI() {
		
		return this.i;
		
	}

}
