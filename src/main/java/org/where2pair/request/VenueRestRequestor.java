package org.where2pair.request;

import java.util.List;
import java.util.Map;

import org.where2pair.Venue;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface VenueRestRequestor {
	
	@GET("/venues/nearest")
	void fetchVenues(@QueryParams Map<String, String> searchCriteria, Callback<List<Venue>> venuesCallback);
	
}
