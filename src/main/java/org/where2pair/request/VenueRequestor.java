package org.where2pair.request;

import java.util.List;
import java.util.Map;

import org.where2pair.Venue;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryParams;

public interface VenueRequestor {
	
	@GET("/venues/nearest")
	void requestVenues(@QueryParams Map<String, String> searchCriteria, Callback<List<Venue>> venuesCallback);
	
}
