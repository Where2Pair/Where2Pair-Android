package org.where2pair.rest

import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor
import static org.where2pair.DayOfWeek.FRIDAY
import static org.where2pair.DayOfWeek.MONDAY
import static org.where2pair.DayOfWeek.SATURDAY
import static org.where2pair.DayOfWeek.SUNDAY
import static org.where2pair.DayOfWeek.THURSDAY
import static org.where2pair.DayOfWeek.TUESDAY
import static org.where2pair.DayOfWeek.WEDNESDAY
import static org.where2pair.DistanceUnit.KM

import org.where2pair.Address
import org.where2pair.Coordinates
import org.where2pair.DailyOpeningTimes
import org.where2pair.Distance
import org.where2pair.OpenPeriod
import org.where2pair.Venue
import org.where2pair.VenueWithDistances
import org.where2pair.WeeklyOpeningTimes

import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Client
import retrofit.client.Request
import retrofit.client.Response
import retrofit.mime.TypedByteArray
import retrofit.mime.TypedString
import spock.lang.Specification

class RequestVenuesIntegrationSpec extends Specification {

	static final SERVER_URL = "http://where2pair.org"
	static final EXPECTED_REQUEST_URL = SERVER_URL + "/venues/nearest?location=1.0%2C0.1&openFrom=12.30&withFacilities=WIFI%2CSEATING"
	
	def "sends request and parses response"() throws InterruptedException {
		given:
		VenuesCallbackForTest venuesCallback = new VenuesCallbackForTest()
		RetrofitVenueService retrofitVenueService = retrofitVenueServiceForTest()
		
		when:
		retrofitVenueService.requestVenues("1.0,0.1", "12.30", "WIFI,SEATING", venuesCallback)
		
		then:
		venuesCallback.positiveResponseReceived
		venuesCallback.venues == TestData.VENUES
	}

	private RetrofitVenueService retrofitVenueServiceForTest() {
		RestAdapter restAdapter = new RetrofitVenueServiceAdapterFactory(SERVER_URL).createRestAdapterBuilder()
				.setClient(new MockClient())
				.setExecutors(sameThreadExecutor(), sameThreadExecutor()).build()
		restAdapter.create(RetrofitVenueService.class)
	}

	static class MockClient implements Client {
		@Override
		Response execute(Request request) throws IOException {
			String url = request.getUrl()
			
			if (url.equals(EXPECTED_REQUEST_URL)) {
				String responseString = TestData.VENUES_JSON
				return new Response(200, "no reason", [], new TypedByteArray("application/json", responseString.getBytes()))
			}
			
			new Response(500, "incorrect url", [], new TypedString("incorrect url"))
		}
	}
	
	static class VenuesCallbackForTest implements Callback<List<VenueWithDistances>> {
		List<VenueWithDistances> venues
		boolean positiveResponseReceived = false
		
		@Override
		void success(List<VenueWithDistances> venues, Response response) {
			this.venues = venues
			positiveResponseReceived = true
		}

		@Override
		void failure(RetrofitError error) {
			println error
		}
	}
	
	static class TestData {
		static final def VENUES = [
				new VenueWithDistances([(new Coordinates(1.0, 0.1)): new Distance(0.34362823973381357, KM)],
						new Distance(0.34362823973381357, KM),
						new Venue(40L, "Starbucks", new Coordinates(51.5139, -0.11017), new Address("30-32 Fleet Street", "Eldon Chambers", "Unit 2 Eldon Chambers", "London", "EC4Y 1AA", "02075834163"),
								["Mobile payments", "Wifi"], new WeeklyOpeningTimes([
										(MONDAY): new DailyOpeningTimes([new OpenPeriod(6, 0, 19, 30)]),
										(TUESDAY): new DailyOpeningTimes([new OpenPeriod(6, 0, 19, 30)]),
										(WEDNESDAY): new DailyOpeningTimes([new OpenPeriod(6, 0, 19, 30)]),
										(THURSDAY): new DailyOpeningTimes([new OpenPeriod(6, 0, 19, 30)]),
										(FRIDAY): new DailyOpeningTimes([new OpenPeriod(6, 0, 19, 30)]),
										(SATURDAY): new DailyOpeningTimes([new OpenPeriod(8, 0, 18, 0)]),
										(SUNDAY): new DailyOpeningTimes([new OpenPeriod(8, 30, 18, 30)])])
								)),
				new VenueWithDistances([(new Coordinates(1.0, 0.1)): new Distance(1.783973264721356, KM)],
						new Distance(1.783973264721356, KM),
						new Venue(22L, "Starbucks", new Coordinates(51.51499, -0.09932), new Address("1 Paternoster House", "Unit 7", "", "London", "EC4M 7DX", "02072363014"),
								["Wifi"], new WeeklyOpeningTimes([
										(MONDAY): new DailyOpeningTimes([new OpenPeriod(6, 0, 20, 0)]),
										(TUESDAY): new DailyOpeningTimes([new OpenPeriod(6, 0, 20, 0)]),
										(WEDNESDAY): new DailyOpeningTimes([new OpenPeriod(6, 0, 20, 0)]),
										(THURSDAY): new DailyOpeningTimes([new OpenPeriod(6, 0, 20, 0)]),
										(FRIDAY): new DailyOpeningTimes([new OpenPeriod(6, 0, 20, 0)]),
										(SATURDAY): new DailyOpeningTimes([new OpenPeriod(8, 0, 18, 30)]),
										(SUNDAY): new DailyOpeningTimes([new OpenPeriod(8, 0, 18, 30)])])
								))]
		
		static final VENUES_JSON = '''[{"distances":[{"location":{"latitude":1.0,"longitude":0.1},"distance":{"value":0.34362823973381357,"unit":"km"}}],
				"averageDistance":{"value":0.34362823973381357,"unit":"km"},"venue":{"id":40,"name":"Starbucks","location":{"latitude":51.5139,"longitude":-0.11017},
				"address":{"addressLine1":"30-32 Fleet Street","addressLine2":"Eldon Chambers","addressLine3":"Unit 2 Eldon Chambers",
				"city":"London","postcode":"EC4Y 1AA","phoneNumber":"02075834163"},"openHours":{"monday":[{"openHour":6,"openMinute":0,
				"closeHour":19,"closeMinute":30}],"tuesday":[{"openHour":6,"openMinute":0,"closeHour":19,"closeMinute":30}],"wednesday":
				[{"openHour":6,"openMinute":0,"closeHour":19,"closeMinute":30}],"thursday":[{"openHour":6,"openMinute":0,"closeHour":19,
				"closeMinute":30}],"friday":[{"openHour":6,"openMinute":0,"closeHour":19,"closeMinute":30}],"saturday":[{"openHour":8,
				"openMinute":0,"closeHour":18,"closeMinute":0}],"sunday":[{"openHour":8,"openMinute":30,"closeHour":18,"closeMinute":30}]},
				"features":["Mobile payments","Wifi"]}},{"distances":[{"location":{"latitude":1.0,"longitude":0.1},"distance":{"value":1.783973264721356,"unit":"km"}}],
				"averageDistance":{"value":1.783973264721356,"unit":"km"},"venue":{"id":22,"name":"Starbucks","location":{"latitude":51.51499,"longitude":-0.09932},
				"address":{"addressLine1":"1 Paternoster House","addressLine2":"Unit 7","addressLine3":"","city":"London",
				"postcode":"EC4M 7DX","phoneNumber":"02072363014"},"openHours":{"monday":[{"openHour":6,"openMinute":0,"closeHour":20,
				"closeMinute":0}],"tuesday":[{"openHour":6,"openMinute":0,"closeHour":20,"closeMinute":0}],"wednesday":[{"openHour":6,
				"openMinute":0,"closeHour":20,"closeMinute":0}],"thursday":[{"openHour":6,"openMinute":0,"closeHour":20,"closeMinute":0}],
				"friday":[{"openHour":6,"openMinute":0,"closeHour":20,"closeMinute":0}],"saturday":[{"openHour":8,"openMinute":0,
				"closeHour":18,"closeMinute":30}],"sunday":[{"openHour":8,"openMinute":0,"closeHour":18,"closeMinute":30}]},"features":
				["Wifi"]}}]'''
	}
}
