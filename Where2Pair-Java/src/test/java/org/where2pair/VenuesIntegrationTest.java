package org.where2pair;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static java.util.Collections.EMPTY_LIST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.where2pair.rest.RetrofitVenueService;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

public class VenuesIntegrationTest {

	private static final String SERVER_URL = "http://where2pair.org";
	private static final String EXPECTED_REQUEST_URL = SERVER_URL + "/venues/nearest?location=1.0%2C0.1&openFrom=12.30&withFacilities=WIFI%2CSEATING";
	
	@Test
	public void requestsAndParsesResponse() throws InterruptedException {
		VenuesCallbackForTest venuesCallback = new VenuesCallbackForTest();
		RetrofitVenueService retrofitVenueService = retrofitVenueServiceForTest();
		
		retrofitVenueService.requestVenues("1.0,0.1", "12.30", "WIFI,SEATING", venuesCallback);
		
		assertThat(venuesCallback.positiveResponseReceived, is(true));
		assertThat(venuesCallback.venues, equalTo(TestData.VENUES));
	}

	private RetrofitVenueService retrofitVenueServiceForTest() {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setClient(new MockClient())
				.setServer(SERVER_URL)
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
	
	static class VenuesCallbackForTest implements Callback<List<Venue>> {
		List<Venue> venues;
		boolean positiveResponseReceived = false;
		
		@Override
		public void success(List<Venue> venues, Response response) {
			this.venues = venues;
			positiveResponseReceived = true;
		}

		@Override
		public void failure(RetrofitError error) {
		}
	}
	
	static class TestData {
		static final List<Venue> VENUES = newArrayList(
				new Venue(40L, "Starbucks", 51.5139, -0.11017, new Address("30-32 Fleet Street", "Eldon Chambers", "Unit 2 Eldon Chambers", "London", "EC4Y 1AA", "02075834163"),
						newArrayList("Mobile payments", "Wifi")),
				new Venue(22L, "Starbucks", 51.51499, -0.09932, new Address("1 Paternoster House", "Unit 7", "", "London", "EC4M 7DX", "02072363014"),
						newArrayList("Wifi")));
		
		
		static final String VENUES_JSON = "[{\"id\":40,\"name\":\"Starbucks\",\"latitude\":51.5139,\"longitude\":-0.11017,"
				+ "\"address\":{\"addressLine1\":\"30-32 Fleet Street\",\"addressLine2\":\"Eldon Chambers\",\"addressLine3\":\"Unit 2 Eldon Chambers\","
				+ "\"city\":\"London\",\"postcode\":\"EC4Y 1AA\",\"phoneNumber\":\"02075834163\"},\"openHours\":{\"monday\":[{\"openHour\":6,\"openMinute\":0,"
				+ "\"closeHour\":19,\"closeMinute\":30}],\"tuesday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":19,\"closeMinute\":30}],\"wednesday\":"
				+ "[{\"openHour\":6,\"openMinute\":0,\"closeHour\":19,\"closeMinute\":30}],\"thursday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":19,"
				+ "\"closeMinute\":30}],\"friday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":19,\"closeMinute\":30}],\"saturday\":[{\"openHour\":8,"
				+ "\"openMinute\":0,\"closeHour\":18,\"closeMinute\":0}],\"sunday\":[{\"openHour\":8,\"openMinute\":30,\"closeHour\":18,\"closeMinute\":30}]},"
				+ "\"features\":[\"Mobile payments\",\"Wifi\"]},{\"id\":22,\"name\":\"Starbucks\",\"latitude\":51.51499,\"longitude\":-0.09932,"
				+ "\"address\":{\"addressLine1\":\"1 Paternoster House\",\"addressLine2\":\"Unit 7\",\"addressLine3\":\"\",\"city\":\"London\","
				+ "\"postcode\":\"EC4M 7DX\",\"phoneNumber\":\"02072363014\"},\"openHours\":{\"monday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":20,"
				+ "\"closeMinute\":0}],\"tuesday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":20,\"closeMinute\":0}],\"wednesday\":[{\"openHour\":6,"
				+ "\"openMinute\":0,\"closeHour\":20,\"closeMinute\":0}],\"thursday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":20,\"closeMinute\":0}],"
				+ "\"friday\":[{\"openHour\":6,\"openMinute\":0,\"closeHour\":20,\"closeMinute\":0}],\"saturday\":[{\"openHour\":8,\"openMinute\":0,"
				+ "\"closeHour\":18,\"closeMinute\":30}],\"sunday\":[{\"openHour\":8,\"openMinute\":0,\"closeHour\":18,\"closeMinute\":30}]},\"features\":"
				+ "[\"Wifi\"]}]";
	}
}
