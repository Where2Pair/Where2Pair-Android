package org.where2pair;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

public class SearchRequestBuilder {

	private Set<String> facilities = newHashSet();
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
		return SearchRequest.openFrom(openFrom, facilities);
	}

}
