package org.where2pair;

import org.where2pair.request.VenueRequestor;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.google.common.base.Joiner.*;
import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;



public class VenueFinder {

	private VenueRequestor venueRequestor;
	
	public void findVenues(SearchRequest searchRequest, final VenuesResultAction venuesResultAction) {
		Map<String, String> searchCriteria = newHashMap();
		String openFrom = searchRequest.getOpenFrom().hour + "." + searchRequest.getOpenFrom().minute;
		searchCriteria.put("openFrom", openFrom);
		String facilities = on(',').join(searchRequest.getFacilities());
		searchCriteria.put("withFacilities", facilities);

        venueRequestor.requestVenues(searchCriteria, new VenuesCallback(venuesResultAction));
	}

}
