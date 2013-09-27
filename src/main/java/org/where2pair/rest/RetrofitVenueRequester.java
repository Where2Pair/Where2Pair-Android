package org.where2pair.rest;

import org.where2pair.SearchRequest;
import org.where2pair.VenueRequester;
import org.where2pair.VenuesResultAction;

public class RetrofitVenueRequester implements VenueRequester {
    RetrofitVenueService retrofitVenueService;


    @Override
    public void requestVenues(SearchRequest searchRequest, VenuesResultAction venuesResultAction) {
    }
}
