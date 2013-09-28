package org.where2pair;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

public class SearchRequestBuilder {

	private Set<String> facilities = newHashSet();
	private SimpleTime openFrom;
	private Coordinates location;
	
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
		return new SearchRequest(location, openFrom, facilities);
	}

	public SearchRequestBuilder near(Coordinates location) {
		this.location = location;
		return this;
	}

}
