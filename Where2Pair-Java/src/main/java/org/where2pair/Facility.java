package org.where2pair;

public enum Facility {
	WIFI("Wifi"), SEATING("Seating"), BABY_CHANGING("Baby changing");
	
	private String label;

	private Facility(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return label;
	}
}
