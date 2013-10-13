package org.where2pair.rest;

import static com.google.common.base.Joiner.on;

import org.where2pair.Coordinates;
import org.where2pair.SearchRequest;
import org.where2pair.SimpleTime;
import org.where2pair.VenueFinder;
import org.where2pair.VenuesCallback;
import org.where2pair.VenuesResultAction;

public class RetrofitVenueFinder implements VenueFinder {
	
    private RetrofitVenueService retrofitVenueService;
    
    public RetrofitVenueFinder(RetrofitVenueService retrofitVenueService) {
		this.retrofitVenueService = retrofitVenueService;
	}

	@Override
    public void findVenues(SearchRequest searchRequest, VenuesResultAction venuesResultAction) {
    	retrofitVenueService.requestVenues(
    			userLocationParam(searchRequest), 
    			openFromParam(searchRequest), 
    			facilitiesParam(searchRequest), 
    			new VenuesCallback(venuesResultAction));
    }
    
	private String userLocationParam(SearchRequest searchRequest) {
		Coordinates location = searchRequest.getLocation();
		return location.latitude + "," + location.longitude;
	}
	
	private String openFromParam(SearchRequest searchRequest) {
		SimpleTime openFrom = searchRequest.getOpenFrom();
		return openFrom.hour + "." + openFrom.minute;
	}
	
	private String facilitiesParam(SearchRequest searchRequest) {
		return on(',').join(searchRequest.getFacilities());
	}
}
