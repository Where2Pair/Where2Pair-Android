package org.where2pair;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

public class TestUtils {

	public static List<Venue> sampleVenues() {
		Venue venue1 = new Venue(99L, "name", 1.0, 0.1, new Address("addressLine1", "addressLine2", "addressLine3", "city", "postcode", "phoneNumber"), newArrayList("Wifi", "Seating"));
		Venue venue2 = new Venue(99L, "2name", 1.0, 0.1, new Address("2addressLine1", "2addressLine2", "2addressLine3", "2city", "2postcode", "2phoneNumber"), newArrayList("2Wifi", "2Seating"));
		return newArrayList(venue1, venue2);
	}
	
}
