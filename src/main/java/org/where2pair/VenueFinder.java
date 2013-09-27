package org.where2pair;

import static com.google.common.base.Joiner.*;
import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;



public class VenueFinder {

	private VenueRequester venueRequester;
	
	public void findVenues(SearchRequest searchRequest, VenuesResultAction venuesResultAction) {
		Map<String, String> searchCriteria = newHashMap();
		String openFrom = searchRequest.getOpenFrom().hour + "." + searchRequest.getOpenFrom().minute;
		searchCriteria.put("openFrom", openFrom);
		String facilities = on(',').join(searchRequest.getFacilities());
		searchCriteria.put("withFacilities", facilities);

        venueRequester.requestVenues(searchCriteria, venuesResultAction);
	}

}
