package org.where2pair;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Distance {

	public final double value;
	public final DistanceUnit unit;

	public Distance(double value, DistanceUnit unit) {
		this.value = value;
		this.unit = unit;
	}

	public String toHumanReadableString() {
		BigDecimal decimal = new BigDecimal(value);
		BigDecimal roundedDecimal = decimal.setScale(2, RoundingMode.HALF_UP);
		return roundedDecimal.doubleValue() + unit.getDisplayValue();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
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
		Distance other = (Distance) obj;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		if (unit != other.unit)
			return false;
		return true;
	}
}
