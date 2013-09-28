package org.where2pair.rest;

import org.where2pair.SearchRequest;
import org.where2pair.VenueFinder;
import org.where2pair.VenuesCallback;
import org.where2pair.VenuesResultAction;

public class RetrofitVenueRequester implements VenueFinder {
	
    private RetrofitVenueService retrofitVenueService;
    
    public RetrofitVenueRequester(RetrofitVenueService retrofitVenueService) {
		this.retrofitVenueService = retrofitVenueService;
	}

	@Override
    public void findVenues(SearchRequest searchRequest, VenuesResultAction venuesResultAction) {
    	retrofitVenueService.requestVenues("1.0,0.1", "12.30", "WIFI,SEATING", new VenuesCallback(venuesResultAction));
    }
    
}
