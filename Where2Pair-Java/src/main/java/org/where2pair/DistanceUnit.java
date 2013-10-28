package org.where2pair;

public enum DistanceUnit {

	KM("km"), 
	MILES("m");
	
	private String displayValue;

	private DistanceUnit(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}
	
}
