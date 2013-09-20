package org.where2pair;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import com.google.common.base.Joiner;


public class VenueFinder {

	private VenueRequestor venueRequestor;
	
	public void fetchVenues(SearchRequest searchRequest, VenuesResultAction venuesResultAction) {
		Map<String, String> searchCriteria = newHashMap();
		String openFrom = searchRequest.getOpenFrom().hour + "." + searchRequest.getOpenFrom().minute;
		searchCriteria.put("openFrom", openFrom);
		String facilities = Joiner.on(',').join(searchRequest.getFacilities());
		searchCriteria.put("withFacilities", facilities);
		
		
		venueRequestor.findVenues(searchCriteria, venuesResultAction);
	}

}
