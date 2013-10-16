package org.where2pair.presentation;

import static com.google.common.collect.Lists.newArrayList;
import static org.where2pair.SearchRequestBuilder.aSearchRequest;

import java.util.List;

import org.robobinding.presentationmodel.AbstractPresentationModel;
import org.robobinding.presentationmodel.ItemPresentationModel;
import org.where2pair.Coordinates;
import org.where2pair.SearchRequest;
import org.where2pair.SimpleTime;
import org.where2pair.VenueFinder;
import org.where2pair.VenueWithDistance;
import org.where2pair.VenuesResultAction;
import org.where2pair.VenuesResultActionHandler;

import com.google.common.collect.ImmutableList;

public class VenueFinderPresentationModel extends AbstractPresentationModel implements VenuesResultActionHandler {

	private VenueFinder venueFinder;
	private LocationProvider locationProvider;
	private TimeProvider timeProvider;
	private UserLocationsObserver userLocationsObserver;
	private VenuesObserver venuesObserver;
	private List<VenueWithDistance> venues;
	private List<Coordinates> userLocations;
	private boolean searchButtonVisible;
	private boolean searchOptionsButtonVisible;
	private boolean mapButtonVisible;
	private boolean listButtonVisible;
	private boolean loadingIconVisible;

	public VenueFinderPresentationModel(VenueFinder venueFinder, LocationProvider locationProvider, 
			TimeProvider timeProvider, UserLocationsObserver userLocationsObserver, VenuesObserver venuesObserver) {
		this.venueFinder = venueFinder;
		this.locationProvider = locationProvider;
		this.timeProvider = timeProvider;
		this.userLocationsObserver = userLocationsObserver;
		this.venuesObserver = venuesObserver;
		setDefaults();
	}

	private void setDefaults() {
		venues = newArrayList();
		userLocations = newArrayList();
		searchButtonVisible = true;
		searchOptionsButtonVisible = true;
		
		Coordinates currentLocation = locationProvider.getCurrentLocation();
		if (currentLocation != null) userLocations.add(currentLocation);
	}
	
	@ItemPresentationModel(VenueItemPresentationModel.class)
	public List<VenueWithDistance> getVenues() {
		return ImmutableList.copyOf(venues);
	}

	public void setVenues(List<VenueWithDistance> venues) {
		this.venues = newArrayList(venues);
	}

	public List<Coordinates> getUserLocations() {
		return ImmutableList.copyOf(userLocations);
	}
	
	public void addUserLocation(Coordinates location) {
		userLocations.add(location);
		userLocationsObserver.notifyLocationAdded(location);
	}
	
	public void searchButtonPressed() {
		setSearchButtonVisible(false);
		setSearchOptionsButtonVisible(false);
		setLoadingIconVisible(true);
		
		if (userLocations.size() > 0) requestVenuesFromServer(userLocations.get(0));
	}

	private void requestVenuesFromServer(Coordinates currentLocation) {
		SimpleTime currentTime = timeProvider.getCurrentTime();
		SearchRequest searchRequest = aSearchRequest().openFrom(currentTime).near(currentLocation).withWifi().withSeating().build();
		venueFinder.findVenues(searchRequest, new VenuesResultAction(this));
	}
	
	@Override
	public void notifyVenuesFound(List<VenueWithDistance> venues) {
		setSearchButtonVisible(false);
		setSearchOptionsButtonVisible(false);
		setLoadingIconVisible(false);
		setListButtonVisible(true);
		venuesObserver.notifyVenuesUpdated();
	}

	@Override
	public void notifyVenuesFindingFailed(String reason) {
		// TODO Auto-generated method stub
		
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
