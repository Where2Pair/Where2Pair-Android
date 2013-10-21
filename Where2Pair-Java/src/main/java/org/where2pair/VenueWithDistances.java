package org.where2pair;

import java.util.Map;

public class VenueWithDistances {
	
	public final Map<Coordinates, Distance> distances;
	public final Distance averageDistance;
	public final Venue venue;
	
	public VenueWithDistances(Map<Coordinates, Distance> distances, Distance averageDistance, Venue venue) {
		this.distances = distances;
		this.averageDistance = averageDistance;
		this.venue = venue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((averageDistance == null) ? 0 : averageDistance.hashCode());
		result = prime * result
				+ ((distances == null) ? 0 : distances.hashCode());
		result = prime * result + ((venue == null) ? 0 : venue.hashCode());
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
		VenueWithDistances other = (VenueWithDistances) obj;
		if (averageDistance == null) {
			if (other.averageDistance != null)
				return false;
		} else if (!averageDistance.equals(other.averageDistance))
			return false;
		if (distances == null) {
			if (other.distances != null)
				return false;
		} else if (!distances.equals(other.distances))
			return false;
		if (venue == null) {
			if (other.venue != null)
				return false;
		} else if (!venue.equals(other.venue))
			return false;
		return true;
	}

}
