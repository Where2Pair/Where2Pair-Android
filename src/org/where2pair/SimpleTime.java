package org.where2pair;

public class SimpleTime {

	public final int hour;
	public final int minute;

	public SimpleTime(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
	}

	@Override
	public String toString() {
		return "SimpleTime [hour=" + hour + ", minute=" + minute + "]";
	}	
}
