package org.where2pair;

import static org.where2pair.SearchRequestBuilder.aSearchRequest;

import java.util.List;

public class SearchRequestService {

	private TimeProvider timeProvider;

	public SearchRequestService(TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	public SearchRequest buildSearchRequest(List<Coordinates> locations) {
		SimpleTime currentTime = timeProvider.getCurrentTime();
		return aSearchRequest().openFrom(currentTime).near(locations).withWifi().withSeating().build();
	}
	
}
