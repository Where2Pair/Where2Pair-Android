package org.where2pair.rest;

import java.util.List;

import org.where2pair.Venue;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface RetrofitVenueService {
	
    @GET("/venues/nearest")
    void requestVenues(
    		@Query("locations") String locations,
    		@Query("openFrom") String openFrom, 
    		@Query("withFacilities") String withFacilities, 
    		Callback<List<Venue>> venuesCallback);

}
