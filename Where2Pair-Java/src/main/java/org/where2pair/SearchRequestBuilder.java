package org.where2pair;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.util.List;
import java.util.Set;

public class SearchRequestBuilder {

	private Set<String> facilities = newHashSet();
	private List<Coordinates> locations = newArrayList();
	private SimpleTime openFrom;
	
	public static SearchRequestBuilder aSearchRequest() {
		return new SearchRequestBuilder();
	}
	
	public SearchRequestBuilder openFrom(SimpleTime openFrom) {
		this.openFrom = openFrom;
		return this;
	}

	public SearchRequestBuilder withWifi() {
		facilities.add("WIFI");
		return this;
	}

	public SearchRequestBuilder withSeating() {
		facilities.add("SEATING");
		return this;
	}
	
	public SearchRequest build() {
		return new SearchRequest(locations, openFrom, facilities);
	}

	public SearchRequestBuilder near(List<Coordinates> locations) {
		this.locations.addAll(locations);
		return this;
	}

}
