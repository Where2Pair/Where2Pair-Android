package org.where2pair;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.where2pair.DistanceUnit.KM;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class TestUtils {

	public static List<VenueWithDistances> sampleVenuesWithDistances() {
		List<VenueWithDistances> venuesWithDistance = newArrayList();
		List<Venue> venues = sampleVenues();
		double distance = 1;
		for (Venue venue : venues) {
			venuesWithDistance.add(sampleVenueWithDistances(distance++, venue));
		}
		return venuesWithDistance;
	}
	
	public static VenueWithDistances sampleVenueWithDistances(double averageDistance) {
		return sampleVenueWithDistances(averageDistance, sampleVenue());
	}
	
	private static VenueWithDistances sampleVenueWithDistances(double averageDistance, Venue venue) {
		Map<Coordinates, Distance> distances = newHashMap();
		for (int i = 0; i < 3; i++) {
			distances.put(new Coordinates(1.0 + i, 0.1 + i), new Distance(averageDistance, KM));
		}
		return new VenueWithDistances(distances, new Distance(averageDistance, KM), venue);
	}
	
	public static List<Venue> sampleVenues() {
		List<Venue> venues = newArrayList();
		for (int i = 0; i < 10; i++) venues.add(sampleVenue(i));
		return venues;
	}
	
	public static Venue sampleVenue() {
		return sampleVenue(0);
	}
	
	private static Venue sampleVenue(int differentiator) {
		return new Venue(99L + differentiator, differentiator + "name", new Coordinates(1.0, 0.1), new Address(differentiator + "addressLine1", differentiator + "addressLine2", differentiator + "addressLine3", differentiator + "city", differentiator + "postcode", differentiator + "phoneNumber"), 
				newArrayList(differentiator + "Wifi", differentiator + "Seating"), differentiator % 2 == 0 ? sampleWeeklyOpeningTimes() : anotherSampleWeeklyOpeningTimes());
	}
	
	public static WeeklyOpeningTimes sampleWeeklyOpeningTimes() {
		Map<DayOfWeek, DailyOpeningTimes> weeklyOpeningTimes = newHashMap();
		for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
			DailyOpeningTimes dailyOpeningTimes = new DailyOpeningTimes(Lists.newArrayList(new OpenPeriod(12, 30, 18, 30)));
			weeklyOpeningTimes.put(dayOfWeek, dailyOpeningTimes);
		}
		return new WeeklyOpeningTimes(weeklyOpeningTimes);
	}
	
	public static WeeklyOpeningTimes anotherSampleWeeklyOpeningTimes() {
		Map<DayOfWeek, DailyOpeningTimes> weeklyOpeningTimes = newHashMap();
		for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
			DailyOpeningTimes dailyOpeningTimes = new DailyOpeningTimes(Lists.newArrayList(new OpenPeriod(9, 30, 22, 45)));
			weeklyOpeningTimes.put(dayOfWeek, dailyOpeningTimes);
		}
		return new WeeklyOpeningTimes(weeklyOpeningTimes);
	}
}
