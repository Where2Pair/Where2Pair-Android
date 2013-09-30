package org.where2pair.rest;

import java.lang.reflect.Type;

import org.where2pair.DayOfWeek;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DayOfWeekDeserializer implements JsonDeserializer<DayOfWeek> {

	@Override
	public DayOfWeek deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return DayOfWeek.valueOf(json.getAsString().toUpperCase());
	}

}
