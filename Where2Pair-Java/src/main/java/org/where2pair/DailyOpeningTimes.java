package org.where2pair;

import java.util.List;

public class DailyOpeningTimes {

	private final List<OpenPeriod> dailyOpeningTimes;

	public DailyOpeningTimes(List<OpenPeriod> dailyOpeningTimes) {
		this.dailyOpeningTimes = dailyOpeningTimes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dailyOpeningTimes == null) ? 0 : dailyOpeningTimes.hashCode());
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
		DailyOpeningTimes other = (DailyOpeningTimes) obj;
		if (dailyOpeningTimes == null) {
			if (other.dailyOpeningTimes != null)
				return false;
		} else if (!dailyOpeningTimes.equals(other.dailyOpeningTimes))
			return false;
		return true;
	}
	
}
