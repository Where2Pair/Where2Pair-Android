package org.where2pair.rest;

import java.lang.reflect.Type;
import java.util.Map;

import org.where2pair.DailyOpeningTimes;
import org.where2pair.DayOfWeek;
import org.where2pair.WeeklyOpeningTimes;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class WeeklyOpeningTimesDeserializer implements JsonDeserializer<WeeklyOpeningTimes> {

	@Override
	public WeeklyOpeningTimes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Map<DayOfWeek, DailyOpeningTimes> weeklyOpeningTimes = context.deserialize(json, new TypeToken<Map<DayOfWeek, DailyOpeningTimes>>() {}.getType());
		return new WeeklyOpeningTimes(weeklyOpeningTimes);
	}

}
