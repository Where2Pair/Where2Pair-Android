package org.where2pair.rest;

import static com.google.common.collect.Maps.newHashMap;

import java.lang.reflect.Type;
import java.util.Map;

import org.where2pair.Coordinates;
import org.where2pair.Distance;
import org.where2pair.Venue;
import org.where2pair.VenueWithDistances;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class VenueWithDistancesDeserializer implements JsonDeserializer<VenueWithDistances> {

	@Override
	public VenueWithDistances deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonArray distancesArray = json.getAsJsonObject().get("distances").getAsJsonArray();
		JsonObject averageDistanceElement = json.getAsJsonObject().get("averageDistance").getAsJsonObject();
		JsonObject venueElement = json.getAsJsonObject().get("venue").getAsJsonObject();
	
		Map<Coordinates, Distance> venueDistances = newHashMap();
		for (JsonElement distance : distancesArray) {
			VenueDistance venueDistance = context.deserialize(distance, new TypeToken<VenueDistance>() {}.getType());
			venueDistances.put(venueDistance.location, venueDistance.distance);
		}
		Distance averageDistance = context.deserialize(averageDistanceElement, new TypeToken<Distance>() {}.getType());
		Venue venue = context.deserialize(venueElement, new TypeToken<Venue>() {}.getType());
		return new VenueWithDistances(venueDistances, averageDistance, venue);
	}

	public static class VenueDistance {
		public final Coordinates location;
		public final Distance distance;
		
		public VenueDistance(Coordinates location, Distance distance) {
			this.location = location;
			this.distance = distance;
		}
	}
}
