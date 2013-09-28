package org.where2pair;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.where2pair.Coordinates;
import org.where2pair.SearchRequest;
import org.where2pair.SimpleTime;

public class SearchRequestMatcher extends TypeSafeMatcher<SearchRequest> {
	private SearchRequest expectedSearchRequest;

	SearchRequestMatcher(SearchRequest expectedSearchRequest) {
		this.expectedSearchRequest = expectedSearchRequest;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(expectedSearchRequest.toString());
	}

	@Override
	protected boolean matchesSafely(SearchRequest actualSearchRequest) {
		Coordinates expectedLocation = expectedSearchRequest.getLocation();
		Coordinates actualLocation = actualSearchRequest.getLocation();
		SimpleTime expectedOpen = expectedSearchRequest.getOpenFrom();
		SimpleTime actualOpen = actualSearchRequest.getOpenFrom();
		return expectedOpen.hour == actualOpen.hour && expectedOpen.minute == actualOpen.minute
				&& expectedLocation.latitude == actualLocation.latitude
				&& expectedLocation.longitude == actualLocation.longitude
				&& expectedSearchRequest.getFacilities().equals(actualSearchRequest.getFacilities());
	}

	public static SearchRequestMatcher equalTo(SearchRequest expectedSearchRequest) {
		return new SearchRequestMatcher(expectedSearchRequest); 
	}
	
}