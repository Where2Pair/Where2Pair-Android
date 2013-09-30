package org.where2pair;

import java.util.List;

public class Venue {

	private final long id;
	private final String name;
	private final Coordinates location;
	private final Address address;
	private final List<String> features;
	
	public Venue(long id, String name, Coordinates location,
			Address address, List<String> features) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.address = address;
		this.features = features;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public Coordinates getLocation() {
		return location;
	}

	public Address getAddress() {
		return address;
	}

	public List<String> getFeatures() {
		return features;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((features == null) ? 0 : features.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Venue other = (Venue) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (features == null) {
			if (other.features != null)
				return false;
		} else if (!features.equals(other.features))
			return false;
		if (id != other.id)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
