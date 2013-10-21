package org.where2pair.rest;

import org.where2pair.DailyOpeningTimes;
import org.where2pair.DayOfWeek;
import org.where2pair.DistanceUnit;
import org.where2pair.VenueWithDistances;
import org.where2pair.WeeklyOpeningTimes;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import com.google.gson.GsonBuilder;

public class RetrofitVenueServiceAdapterFactory {

	private final String serverUrl;
	
	public RetrofitVenueServiceAdapterFactory(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public RetrofitVenueService createRetrofitVenueService() {
		RestAdapter restAdapter = createRestAdapterBuilder().build();
		return restAdapter.create(RetrofitVenueService.class);
	}
	
	RestAdapter.Builder createRestAdapterBuilder() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(VenueWithDistances.class, new VenueWithDistancesDeserializer());
		gsonBuilder.registerTypeAdapter(WeeklyOpeningTimes.class, new WeeklyOpeningTimesDeserializer());
		gsonBuilder.registerTypeAdapter(DailyOpeningTimes.class, new DailyOpeningTimesDeserializer());
		gsonBuilder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekDeserializer());
		gsonBuilder.registerTypeAdapter(DistanceUnit.class, new DistanceUnitSerializer());
		
		return new RestAdapter.Builder()
			.setConverter(new GsonConverter(gsonBuilder.create()))
			.setServer(serverUrl);
	}
	
}
