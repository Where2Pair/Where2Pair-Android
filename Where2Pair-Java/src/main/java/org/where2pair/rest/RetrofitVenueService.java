package org.where2pair.rest;

import java.util.List;

import org.where2pair.VenueWithDistances;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface RetrofitVenueService {
	
    @GET("/venues/nearest")
    void requestVenues(
    		@Query("location") List<String> locations,
    		@Query("openFrom") String openFrom, 
    		@Query("withFacilities") String withFacilities, 
    		Callback<List<VenueWithDistances>> venuesCallback);

}