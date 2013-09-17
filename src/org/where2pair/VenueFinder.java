package org.where2pair;


public class VenueFinder {

	private VenueRequestor venueRequestor;
	
	public void fetchVenues(SearchRequest searchRequest, VenuesResultAction venuesResultAction) {
		venueRequestor.findVenues("venues/nearest?openFrom=12.30&withFacilities=WIFI,SEATING", venuesResultAction);
	}

}
