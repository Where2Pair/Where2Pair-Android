package org.where2pair.presentation;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.partition;
import static org.where2pair.SearchRequestBuilder.aSearchRequest;

import java.util.List;

import org.robobinding.presentationmodel.ItemPresentationModel;
import org.robobinding.presentationmodel.PresentationModel;
import org.where2pair.Coordinates;
import org.where2pair.SearchRequestBuilder;
import org.where2pair.SimpleTime;
import org.where2pair.VenueFinder;
import org.where2pair.VenueWithDistances;
import org.where2pair.VenuesResultAction;
import org.where2pair.VenuesResultActionHandler;

import com.google.common.collect.ImmutableList;

@PresentationModel
public class VenueFinderPresentationModel implements VenuesResultActionHandler, CurrentLocationObserver {

	static final Coordinates LONDON = new Coordinates(51.5085, 0.1257);
	private VenueFinder venueFinder;
	private LocationProvider locationProvider;
	private TimeProvider timeProvider;
	private DeviceVibrator deviceVibrator;
	private VenuesViewTransitioner venuesViewTransitioner;
	private UserLocationsObserver userLocationsObserver;
	private VenuesObserver venuesObserver;
	private List<VenueWithDistances> venues;
	private List<Coordinates> userLocations;
	private Coordinates currentLocation;
	private boolean searchButtonVisible;
	private boolean searchOptionsButtonVisible;
	private boolean mapButtonVisible;
	private boolean listButtonVisible;
	private boolean loadingIconVisible;
	private boolean searchingForVenues;
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
		setSearchButtonVisible(true);
		setSearchOptionsButtonVisible(true);
		setMapButtonVisible(false);
		setListButtonVisible(false);
		viewingVenueSearchResults = false;
		
		if (currentLocation == null) 
			locationProvider.requestCurrentLocation(this);
		else 
			userLocations.add(currentLocation);
	}
	
	@ItemPresentationModel(VenueItemPresentationModel.class)
	public List<VenueWithDistances> getVenues() {
		return ImmutableList.copyOf(venues);
	}

	public void setVenues(List<VenueWithDistances> venues) {
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
	
	public void userLocationPressed(Coordinates coordinates) {
		if (searchingForVenues || viewingVenueSearchResults) return;
			
		userLocations.remove(coordinates);
		userLocationsObserver.notifyUserLocationRemoved(coordinates);
	}
	
	public void searchButtonPressed() {
		if (userLocations.isEmpty()) return;
		
		setSearchButtonVisible(false);
		setSearchOptionsButtonVisible(false);
		setLoadingIconVisible(true);
		
		searchingForVenues = true;
		requestVenuesFromServer(userLocations);
	}

	private void requestVenuesFromServer(List<Coordinates> locations) {
		SimpleTime currentTime = timeProvider.getCurrentTime();
		SearchRequestBuilder searchRequest = aSearchRequest().openFrom(currentTime).near(locations).withWifi().withSeating();
		venueFinder.findVenues(searchRequest.build(), new VenuesResultAction(this));
	}
	
	@Override
	public void notifyVenuesFound(List<VenueWithDistances> venues) {
		searchingForVenuesFinished();
		setSearchButtonVisible(false);
		setSearchOptionsButtonVisible(false);
		setListButtonVisible(true);
		viewingVenueSearchResults = true;
		setVenues(venues);
		venuesObserver.notifyVenuesUpdated();
	}

	private void searchingForVenuesFinished() {
		searchingForVenues = false;
		setLoadingIconVisible(false);
	}
	
	@Override
	public void notifyVenuesFindingFailed(String reason) {
		searchingForVenuesFinished();
		setSearchButtonVisible(true);
		setSearchOptionsButtonVisible(true);
	}
	
	@Override
	public void notifyCurrentLocationEstablished(Coordinates coordinates) {
		currentLocation = coordinates;
		if (!userLocations.isEmpty()) return;
		
		userLocations.add(coordinates);
		userLocationsObserver.notifyUserLocationAddedAndZoomCamera(coordinates);
	}
	
	public List<Coordinates> getMapViewportBounds() {
		List<Coordinates> coordinatesBounds = newArrayList(userLocations);
		if (coordinatesBounds.isEmpty()) coordinatesBounds.add(LONDON);
		if (!venues.isEmpty()) {
			List<VenueWithDistances> closestVenues = partition(venues, 5).get(0);
			for (VenueWithDistances venueWithDistances : closestVenues) {
				coordinatesBounds.add(venueWithDistances.venue.getLocation());
			}
		}
		return coordinatesBounds;
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
	
	public boolean backButtonPressed() {
		if (viewingVenueSearchResults) {
			setDefaults();
			venuesViewTransitioner.resetDisplay();
			return true;
		}
		return false;
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
