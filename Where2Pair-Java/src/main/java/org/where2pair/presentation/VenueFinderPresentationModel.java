package org.where2pair.presentation;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.robobinding.presentationmodel.AbstractPresentationModel;
import org.robobinding.presentationmodel.ItemPresentationModel;
import org.where2pair.Coordinates;
import org.where2pair.VenueWithDistance;

public class VenueFinderPresentationModel extends AbstractPresentationModel {

	private LocationProvider locationProvider;
	private UserLocationsObserver userLocationsObserver;
	private List<VenueWithDistance> venues;
	private boolean searchButtonVisible;
	private boolean searchOptionsButtonVisible;
	private boolean mapButtonVisible;
	private boolean listButtonVisible;
	private boolean loadingIconVisible;

	public VenueFinderPresentationModel(LocationProvider locationProvider, UserLocationsObserver userLocationsObserver) {
		this.locationProvider = locationProvider;
		this.userLocationsObserver = userLocationsObserver;
		searchButtonVisible = true;
		searchOptionsButtonVisible = true;
	}
	
	@ItemPresentationModel(VenueItemPresentationModel.class)
	public List<VenueWithDistance> getVenues() {
		return venues;
	}

	public void setVenues(List<VenueWithDistance> venues) {
		this.venues = venues;
	}

	public List<Coordinates> getUserLocations() {
		Coordinates currentLocation = locationProvider.getCurrentLocation();
		
		if (currentLocation == null) return newArrayList();
		
		return newArrayList(currentLocation);
	}
	
	public void searchButtonPressed() {
		setSearchButtonVisible(false);
		setSearchOptionsButtonVisible(false);
		setLoadingIconVisible(true);
	}

	public void notifyVenuesFound(List<VenueWithDistance> venues) {
		setSearchButtonVisible(false);
		setSearchOptionsButtonVisible(false);
		setLoadingIconVisible(false);
		setListButtonVisible(true);
	}
	
	public void addUserLocation(Coordinates location) {
		userLocationsObserver.notifyLocationAdded(location);
	}
	
	public boolean isSearchButtonVisible() {
		return searchButtonVisible;
	}

	public void setSearchButtonVisible(boolean searchButtonVisible) {
		this.searchButtonVisible = searchButtonVisible;
	}

	public boolean isSearchOptionsButtonVisible() {
		return searchOptionsButtonVisible;
	}

	public void setSearchOptionsButtonVisible(boolean searchOptionsButtonVisible) {
		this.searchOptionsButtonVisible = searchOptionsButtonVisible;
	}

	public boolean isMapButtonVisible() {
		return mapButtonVisible;
	}

	public void setMapButtonVisible(boolean mapButtonVisible) {
		this.mapButtonVisible = mapButtonVisible;
	}

	public boolean isListButtonVisible() {
		return listButtonVisible;
	}

	public void setListButtonVisible(boolean listButtonVisible) {
		this.listButtonVisible = listButtonVisible;
	}

	public boolean isLoadingIconVisible() {
		return loadingIconVisible;
	}

	public void setLoadingIconVisible(boolean loadingIconVisible) {
		this.loadingIconVisible = loadingIconVisible;
	}
}
