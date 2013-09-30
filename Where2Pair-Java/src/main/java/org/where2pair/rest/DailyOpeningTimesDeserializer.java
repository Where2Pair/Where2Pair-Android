package org.where2pair.rest;

import java.lang.reflect.Type;
import java.util.List;

import org.where2pair.DailyOpeningTimes;
import org.where2pair.OpenPeriod;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class DailyOpeningTimesDeserializer implements JsonDeserializer<DailyOpeningTimes> {

	@Override
	public DailyOpeningTimes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		List<OpenPeriod> dailyOpeningTimes = context.deserialize(json, new TypeToken<List<OpenPeriod>>() {}.getType());
		return new DailyOpeningTimes(dailyOpeningTimes);
	}

}
