package org.where2pair.rest;

import org.where2pair.Venue;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryParams;

import java.util.List;
import java.util.Map;

public interface RetrofitVenueService {
    @GET("/venues/nearest")
    void requestVenues(@Query("openFrom") String openFrom, @Query("withFacilities") String withFacilities,
                       Callback<List<Venue>> venuesCallback);
}
