package org.where2pair.presentation;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class SearchOptionsPresentationModel {

	private static final List<String> DAYS = ImmutableList.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
	
	public List<String> getDays() {
		return DAYS;
	}
	
}
