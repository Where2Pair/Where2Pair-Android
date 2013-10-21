package org.where2pair.rest;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static java.util.Collections.EMPTY_LIST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.where2pair.DayOfWeek.FRIDAY;
import static org.where2pair.DayOfWeek.MONDAY;
import static org.where2pair.DayOfWeek.SATURDAY;
import static org.where2pair.DayOfWeek.SUNDAY;
import static org.where2pair.DayOfWeek.THURSDAY;
import static org.where2pair.DayOfWeek.TUESDAY;
import static org.where2pair.DayOfWeek.WEDNESDAY;
import static org.where2pair.DistanceUnit.KM;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.where2pair.Address;
import org.where2pair.Coordinates;
import org.where2pair.DailyOpeningTimes;
import org.where2pair.DayOfWeek;
import org.where2pair.Distance;
import org.where2pair.OpenPeriod;
import org.where2pair.Venue;
import org.where2pair.VenueWithDistances;
import org.where2pair.WeeklyOpeningTimes;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

import com.google.common.collect.ImmutableMap;

public class RequestVenuesIntegrationTest {

	private static final String SERVER_URL = "http://where2pair.org";
	private static final String EXPECTED_REQUEST_URL = SERVER_URL + "/venues/nearest?location=1.0%2C0.1&openFrom=12.30&withFacilities=WIFI%2CSEATING";
	
	@Test
	public void requestsAndParsesResponse() throws InterruptedException {
		VenuesCallbackForTest venuesCallback = new VenuesCallbackForTest();
		RetrofitVenueService retrofitVenueService = retrofitVenueServiceForTest();
		
		System.out.println(TestData.VENUES_JSON);
		
		retrofitVenueService.requestVenues("1.0,0.1", "12.30", "WIFI,SEATING", venuesCallback);
		
		assertThat(venuesCallback.positiveResponseReceived, is(true));
		assertThat(venuesCallback.venues, equalTo(TestData.VENUES));
	}

	private RetrofitVenueService retrofitVenueServiceForTest() {
		RestAdapter restAdapter = new RetrofitVenueServiceAdapterFactory(SERVER_URL).createRestAdapterBuilder()
				.setClient(new MockClient())
				.setExecutors(sameThreadExecutor(), sameThreadExecutor()).build();
		return restAdapter.create(RetrofitVenueService.class);
	}

	static class MockClient implements Client {
		@SuppressWarnings("unchecked")
		@Override
		public Response execute(Request request) throws IOException {
			String url = request.getUrl();
			
			if (url.equals(EXPECTED_REQUEST_URL)) {
				String responseString = TestData.VENUES_JSON;
				return new Response(200, "no reason", EMPTY_LIST, new TypedByteArray("application/json", responseString.getBytes()));
			}
			
			return new Response(500, "incorrect url", EMPTY_LIST, new TypedString("incorrect url"));
		}
	}
	
	static class VenuesCallbackForTest implements Callback<List<VenueWithDistances>> {
		List<VenueWithDistances> venues;
		boolean positiveResponseReceived = false;
		
		@Override
		public void success(List<VenueWithDistances> venues, Response response) {
			this.venues = venues;
			positiveResponseReceived = true;
		}

		@Override
		public void failure(RetrofitError error) {
			System.out.println(error.toString());
		}
	}
	
	static class TestData {
		@SuppressWarnings("serial")
		static final List<VenueWithDistances> VENUES = newArrayList(
				new VenueWithDistances(ImmutableMap.of(new Coordinates(1.0, 0.1), new Distance(0.34362823973381357, KM)),
						new Distance(0.34362823973381357, KM),
						new Venue(40L, "Starbucks", new Coordinates(51.5139, -0.11017), new Address("30-32 Fleet Street", "Eldon Chambers", "Unit 2 Eldon Chambers", "London", "EC4Y 1AA", "02075834163"),
								newArrayList("Mobile payments", "Wifi"), new WeeklyOpeningTimes(new HashMap<DayOfWeek, DailyOpeningTimes>(){
									{
										put(MONDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(6, 0, 19, 30))));
										put(TUESDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(6, 0, 19, 30))));
										put(WEDNESDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(6, 0, 19, 30))));
										put(THURSDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(6, 0, 19, 30))));
										put(FRIDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(6, 0, 19, 30))));
										put(SATURDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(8, 0, 18, 0))));
										put(SUNDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(8, 30, 18, 30))));
									}
								}))), 
				new VenueWithDistances(ImmutableMap.of(new Coordinates(1.0, 0.1), new Distance(1.783973264721356, KM)),
						new Distance(1.783973264721356, KM),
						new Venue(22L, "Starbucks", new Coordinates(51.51499, -0.09932), new Address("1 Paternoster House", "Unit 7", "", "London", "EC4M 7DX", "02072363014"),
								newArrayList("Wifi"), new WeeklyOpeningTimes(new HashMap<DayOfWeek, DailyOpeningTimes>(){
									{
										put(MONDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(6, 0, 20, 0))));
										put(TUESDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(6, 0, 20, 0))));
										put(WEDNESDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(6, 0, 20, 0))));
										put(THURSDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(6, 0, 20, 0))));
										put(FRIDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(6, 0, 20, 0))));
										put(SATURDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(8, 0, 18, 30))));
										put(SUNDAY, new DailyOpeningTimes(newArrayList(new OpenPeriod(8, 0, 18, 30))));
									}
								}))));
		
		
		static final String VENUES_JSON = "[{\"distances\":[{\"location\":{\"latitude\":1.0,\"longitude\":0.1},\"distance\":{\"distance\":0.34362823973381357,\"unit\":\"km\"}}],"
				+ "\"averageDistance\":{\"distance\":0.34362823973381357,\"unit\":\"km\"},\"venue\":{\"id\":40,\"name\":\"Starbucks\",\"location\":{\"latitude\":51.5139,\"longitude\":-0.11017},"
				+ "\"address\":{\"addressLine1\":\"30-32 Fleet Street\",\"addressLine2\":\"Eldon Chambers\",\"addressLine3\":\"Unit 2 Eldon Chambers\","
				+ "\"city\":\"London\",\"postcode\":\"EC4Y 1AA\",\"phoneNumber\":\"02075834163\"},\"openHours\":{\"monday\":[{\"openHour\":6,\"openMinute\":0,"
				+ "\"closeHour\":19,\"closeMinute\":30}],\"tuesday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":19,\"closeMinute\":30}],\"wednesday\":"
				+ "[{\"openHour\":6,\"openMinute\":0,\"closeHour\":19,\"closeMinute\":30}],\"thursday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":19,"
				+ "\"closeMinute\":30}],\"friday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":19,\"closeMinute\":30}],\"saturday\":[{\"openHour\":8,"
				+ "\"openMinute\":0,\"closeHour\":18,\"closeMinute\":0}],\"sunday\":[{\"openHour\":8,\"openMinute\":30,\"closeHour\":18,\"closeMinute\":30}]},"
				+ "\"features\":[\"Mobile payments\",\"Wifi\"]}},{\"distances\":[{\"location\":{\"latitude\":1.0,\"longitude\":0.1},\"distance\":{\"distance\":1.783973264721356,\"unit\":\"km\"}}],"
				+ "\"averageDistance\":{\"distance\":1.783973264721356,\"unit\":\"km\"},\"venue\":{\"id\":22,\"name\":\"Starbucks\",\"location\":{\"latitude\":51.51499,\"longitude\":-0.09932},"
				+ "\"address\":{\"addressLine1\":\"1 Paternoster House\",\"addressLine2\":\"Unit 7\",\"addressLine3\":\"\",\"city\":\"London\","
				+ "\"postcode\":\"EC4M 7DX\",\"phoneNumber\":\"02072363014\"},\"openHours\":{\"monday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":20,"
				+ "\"closeMinute\":0}],\"tuesday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":20,\"closeMinute\":0}],\"wednesday\":[{\"openHour\":6,"
				+ "\"openMinute\":0,\"closeHour\":20,\"closeMinute\":0}],\"thursday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":20,\"closeMinute\":0}],"
				+ "\"friday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":20,\"closeMinute\":0}],\"saturday\":[{\"openHour\":8,\"openMinute\":0,"
				+ "\"closeHour\":18,\"closeMinute\":30}],\"sunday\":[{\"openHour\":8,\"openMinute\":0,\"closeHour\":18,\"closeMinute\":30}]},\"features\":"
				+ "[\"Wifi\"]}}]";
	}
}
