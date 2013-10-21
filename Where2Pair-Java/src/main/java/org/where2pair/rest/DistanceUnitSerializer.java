package org.where2pair.rest;

import java.lang.reflect.Type;

import org.where2pair.DistanceUnit;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


public class DistanceUnitSerializer implements JsonDeserializer<DistanceUnit> {

	@Override
	public DistanceUnit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return DistanceUnit.valueOf(json.getAsString().toUpperCase());
	}

}
