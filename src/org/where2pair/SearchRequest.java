package org.where2pair;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

public class SearchRequest {

	private final SimpleTime openFrom;
	private final Set<String> facilities;
	
	public SearchRequest(SimpleTime openFrom, Set<String> facilities) {
		this.openFrom = openFrom;
		this.facilities = newHashSet(facilities);
	}

	public static SearchRequest openFrom(SimpleTime openFrom, Set<String> facilities) {
		return new SearchRequest(openFrom, facilities);
	}
	
	public SimpleTime getOpenFrom() {
		return openFrom;
	}

	public Set<String> getFacilities() {
		return facilities;
	}

	@Override
	public String toString() {
		return "SearchRequest [openFrom=" + openFrom + ", facilities="
				+ facilities + "]";
	}
}
