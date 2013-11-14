package org.where2pair.presentation;

import static com.google.common.collect.Lists.newArrayList;
import static org.where2pair.Facility.BABY_CHANGING;
import static org.where2pair.Facility.SEATING;
import static org.where2pair.Facility.WIFI;

import java.util.List;

import org.robobinding.presentationmodel.PresentationModel;
import org.where2pair.Facility;
import org.where2pair.SearchOptionsRepository;

import com.google.common.collect.ImmutableList;

@PresentationModel
public class SearchOptionsPresentationModel {

	private static final List<String> DAYS = ImmutableList.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
	private static final List<Facility> FACILITIES = ImmutableList.of(WIFI, SEATING, BABY_CHANGING);
	private SearchOptionsRepository searchOptionsRepository;
	
	public SearchOptionsPresentationModel(SearchOptionsRepository searchOptionsRepository) {
		this.searchOptionsRepository = searchOptionsRepository;
	}

	public List<String> getDays() {
		return DAYS;
	}
	
	public List<String> getFacilities() {
		List<String> facilities = newArrayList();
		for (Facility facility : FACILITIES) {
			facilities.add(facility.toString());
		}
		return facilities;
	}
	
	public List<Integer> getSelectedFacilityPositions() {
		List<Integer> positions = newArrayList();
		for (Facility facility : searchOptionsRepository.getSelectedFacilities()) {
			positions.add(FACILITIES.indexOf(facility));
		}
		return positions;
	}
	
	public void setSelectedFacilityPositions(List<Integer> positions) {
		List<Facility> facilities = newArrayList();
		for (int position : positions) {
			facilities.add(FACILITIES.get(position));
		}
		searchOptionsRepository.setSelectedFacilities(facilities);
	}
	
	public String getOpenFromLabel() {
		return "";
	}
	
	public String getOpenUntilLabel() {
		return "";
	}
	
	public int getTimeSliderIncrementCount() {
		return 10;
	}
	
	public int getOpenFromProgress() {
		return 0;
	}
	
	public void setOpenFromProgress(int openFromProgress) {
		
	}
	
	public int getOpenUntilProgress() {
		return 5;
	}
	
	public void setOpenUntilProgress(int openUntilProgress) {
		
	}
}
