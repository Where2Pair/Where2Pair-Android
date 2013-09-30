package org.where2pair;

public class OpenPeriod {

	private final int openHour;
	private final int openMinute;
	private final int closeHour;
	private final int closeMinute;
	
	public OpenPeriod(int openHour, int openMinute, int closeHour,
			int closeMinute) {
		this.openHour = openHour;
		this.openMinute = openMinute;
		this.closeHour = closeHour;
		this.closeMinute = closeMinute;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + closeHour;
		result = prime * result + closeMinute;
		result = prime * result + openHour;
		result = prime * result + openMinute;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OpenPeriod other = (OpenPeriod) obj;
		if (closeHour != other.closeHour)
			return false;
		if (closeMinute != other.closeMinute)
			return false;
		if (openHour != other.openHour)
			return false;
		if (openMinute != other.openMinute)
			return false;
		return true;
	}
	
}
