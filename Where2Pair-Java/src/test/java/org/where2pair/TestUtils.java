package org.where2pair;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.common.collect.ImmutableMap;

public class TestUtils {

	public static List<VenueWithDistance> sampleVenuesWithDistance() {
		List<VenueWithDistance> venuesWithDistance = newArrayList();
		List<Venue> venues = sampleVenues();
		double distance = 3;
		for (Venue venue : venues) {
			venuesWithDistance.add(new VenueWithDistance(ImmutableMap.of("location", distance++), venue));
		}
		return venuesWithDistance;
	}
	
	public static List<Venue> sampleVenues() {
		Venue venue1 = new Venue(99L, "name", 1.0, 0.1, new Address("addressLine1", "addressLine2", "addressLine3", "city", "postcode", "phoneNumber"), newArrayList("Wifi", "Seating"));
		Venue venue2 = new Venue(199L, "2name", 1.0, 0.1, new Address("2addressLine1", "2addressLine2", "2addressLine3", "2city", "2postcode", "2phoneNumber"), newArrayList("2Wifi", "2Seating"));
		return newArrayList(venue1, venue2);
	}
	
}
