package org.where2pair;

import java.util.Map;


public interface VenueRequestor {

	void findVenues(Map<String, String> searchCriteria, VenuesResultAction venuesResultAction);

}
