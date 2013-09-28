package org.where2pair;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

public class SearchRequest {

	private final Coordinates location;
	private final SimpleTime openFrom;
	private final Set<String> facilities;
	
	public SearchRequest(Coordinates location, SimpleTime openFrom, Set<String> facilities) {
		this.location = location;
		this.openFrom = openFrom;
		this.facilities = newHashSet(facilities);
	}
	
	public Coordinates getLocation() {
		return location;
	}
	
	public SimpleTime getOpenFrom() {
		return openFrom;
	}

	public Set<String> getFacilities() {
		return facilities;
	}

	@Override
	public String toString() {
		return "SearchRequest [location=" + location + ", openFrom=" + openFrom + ", facilities=" + facilities + "]";
	}
}
