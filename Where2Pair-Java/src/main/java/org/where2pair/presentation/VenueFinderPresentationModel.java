package org.where2pair.presentation;

import static com.google.common.collect.Lists.newArrayList;
import static org.where2pair.SearchRequestBuilder.aSearchRequest;

import java.util.List;

import org.robobinding.presentationmodel.ItemPresentationModel;
import org.robobinding.presentationmodel.PresentationModel;
import org.where2pair.Coordinates;
import org.where2pair.SearchRequest;
import org.where2pair.SimpleTime;
import org.where2pair.VenueFinder;
import org.where2pair.VenueWithDistance;
import org.where2pair.VenuesResultAction;
import org.where2pair.VenuesResultActionHandler;

import com.google.common.collect.ImmutableList;

@PresentationModel
public class VenueFinderPresentationModel implements VenuesResultActionHandler {

	private VenueFinder venueFinder;
	private LocationProvider locationProvider;
	private TimeProvider timeProvider;
	private DeviceVibrator deviceVibrator;
	private VenuesViewTransitioner venuesViewTransitioner;
	private UserLocationsObserver userLocationsObserver;
	private VenuesObserver venuesObserver;
	private List<VenueWithDistance> venues;
	private List<Coordinates> userLocations;
	private boolean searchButtonVisible;
	private boolean searchOptionsButtonVisible;
	private boolean mapButtonVisible;
	private boolean listButtonVisible;
	private boolean loadingIconVisible;
	private boolean viewingVenueSearchResults;

	public VenueFinderPresentationModel(VenueFinder venueFinder, LocationProvider locationProvider, 
			TimeProvider timeProvider, DeviceVibrator deviceVibrator) {
		this.venueFinder = venueFinder;
		this.locationProvider = locationProvider;
		this.timeProvider = timeProvider;
		this.deviceVibrator = deviceVibrator;
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
	
	public void mapLongPressed(Coordinates coordinates) {
		if (viewingVenueSearchResults) return;
		
		userLocations.add(coordinates);
		userLocationsObserver.notifyUserLocationAdded(coordinates);
		deviceVibrator.vibrate(100);
	}
	
	public void searchButtonPressed() {
		if (userLocations.isEmpty()) return;
		
		setSearchButtonVisible(false);
		setSearchOptionsButtonVisible(false);
		setLoadingIconVisible(true);
		
		requestVenuesFromServer(userLocations.get(0));
	}

	private void requestVenuesFromServer(Coordinates currentLocation) {
		SimpleTime currentTime = timeProvider.getCurrentTime();
		SearchRequest searchRequest = aSearchRequest().openFrom(currentTime).near(currentLocation).withWifi().withSeating().build();
		venueFinder.findVenues(searchRequest, new VenuesResultAction(this));
	}
	
	@Override
	public void notifyVenuesFound(List<VenueWithDistance> venues) {
		viewingVenueSearchResults = true;
		setVenues(venues);
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
	
	public boolean hasMapMarkersToDisplay() {
		return !venues.isEmpty() || !userLocations.isEmpty();
	}
	
	public void mapButtonPressed() {
		setSearchButtonVisible(false);
		setSearchOptionsButtonVisible(false);
		setLoadingIconVisible(false);
		setListButtonVisible(true);
		setMapButtonVisible(false);
		venuesViewTransitioner.showMap();
	}
	
	public void listButtonPressed() {
		setListButtonVisible(false);
		setMapButtonVisible(true);
		venuesViewTransitioner.showList();
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

	public void setVenuesViewTransitioner(VenuesViewTransitioner venuesViewTransitioner) {
		this.venuesViewTransitioner = venuesViewTransitioner;
	}

	public void setUserLocationsObserver(UserLocationsObserver userLocationsObserver) {
		this.userLocationsObserver = userLocationsObserver;
	}

	public void setVenuesObserver(VenuesObserver venuesObserver) {
		this.venuesObserver = venuesObserver;
	}

}
