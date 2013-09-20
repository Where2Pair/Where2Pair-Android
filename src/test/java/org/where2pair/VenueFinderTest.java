package org.where2pair;

import static com.google.common.collect.Maps.newHashMap;
import static org.mockito.Mockito.verify;
import static org.where2pair.SearchRequestBuilder.aSearchRequest;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VenueFinderTest {

	private static final SimpleTime CURRENT_TIME = new SimpleTime(12, 30);
	List<Venue> expectedVenues;
	@Mock VenuesResultAction venuesResultAction;
	@Mock VenueRequestor venueRequestor;
	@InjectMocks VenueFinder venueFinder;
	
	@Test
	public void mapsASearchRequestIntoAWebRequestAndReturnsTheResult() {
		SearchRequest searchRequest = aSearchRequest().openFrom(CURRENT_TIME).withWifi().withSeating().build();
		Map<String, String> expectedSearchCriteria = newHashMap();
		expectedSearchCriteria.put("openFrom", "12.30");
		expectedSearchCriteria.put("withFacilities", "WIFI,SEATING");
		
		venueFinder.fetchVenues(searchRequest, venuesResultAction);
		
		verify(venueRequestor).findVenues(expectedSearchCriteria, venuesResultAction);
	}
	
	//TODO create custom matcher for params
	
}
