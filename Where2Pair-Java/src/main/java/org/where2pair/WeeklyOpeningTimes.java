package org.where2pair;

import java.util.Map;

public class WeeklyOpeningTimes {

	private final Map<DayOfWeek, DailyOpeningTimes> weeklyOpeningTimes;

	public WeeklyOpeningTimes(Map<DayOfWeek, DailyOpeningTimes> weeklyOpeningTimes) {
		this.weeklyOpeningTimes = weeklyOpeningTimes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((weeklyOpeningTimes == null) ? 0 : weeklyOpeningTimes.hashCode());
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
		WeeklyOpeningTimes other = (WeeklyOpeningTimes) obj;
		if (weeklyOpeningTimes == null) {
			if (other.weeklyOpeningTimes != null)
				return false;
		} else if (!weeklyOpeningTimes.equals(other.weeklyOpeningTimes))
			return false;
		return true;
	}
	
}
