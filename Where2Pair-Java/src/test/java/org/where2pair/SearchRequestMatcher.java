package org.where2pair;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

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
		List<Coordinates> expectedLocations = expectedSearchRequest.getLocations();
		List<Coordinates> actualLocations = actualSearchRequest.getLocations();
		SimpleTime expectedOpen = expectedSearchRequest.getOpenFrom();
		SimpleTime actualOpen = actualSearchRequest.getOpenFrom();
		return expectedOpen.hour == actualOpen.hour && expectedOpen.minute == actualOpen.minute
				&& expectedLocations.equals(actualLocations)
				&& expectedSearchRequest.getFacilities().equals(actualSearchRequest.getFacilities());
	}

	public static SearchRequestMatcher equalTo(SearchRequest expectedSearchRequest) {
		return new SearchRequestMatcher(expectedSearchRequest); 
	}
	
}