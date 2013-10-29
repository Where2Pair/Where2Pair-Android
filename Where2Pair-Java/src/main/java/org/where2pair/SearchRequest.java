package org.where2pair;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.util.List;
import java.util.Set;

public class SearchRequest {

	private final List<Coordinates> locations;
	private final SimpleTime openFrom;
	private final Set<String> facilities;
	
	public SearchRequest(List<Coordinates> locations, SimpleTime openFrom, Set<String> facilities) {
		this.locations = newArrayList(locations);
		this.openFrom = openFrom;
		this.facilities = newHashSet(facilities);
	}
	
	public List<Coordinates> getLocations() {
		return locations;
	}
	
	public SimpleTime getOpenFrom() {
		return openFrom;
	}

	public Set<String> getFacilities() {
		return facilities;
	}

	@Override
	public String toString() {
		return "SearchRequest [location=" + locations + ", openFrom=" + openFrom + ", facilities=" + facilities + "]";
	}
}
