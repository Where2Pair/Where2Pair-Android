package org.where2pair;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

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
		Venue venue1 = new Venue(99L, "name", new Coordinates(1.0, 0.1), new Address("addressLine1", "addressLine2", "addressLine3", "city", "postcode", "phoneNumber"), 
				newArrayList("Wifi", "Seating"), sampleWeeklyOpeningTimes());
		Venue venue2 = new Venue(199L, "2name", new Coordinates(1.0, 0.1), new Address("2addressLine1", "2addressLine2", "2addressLine3", "2city", "2postcode", "2phoneNumber"), 
				newArrayList("2Wifi", "2Seating"), anotherSampleWeeklyOpeningTimes());
		return newArrayList(venue1, venue2);
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
