package org.where2pair.presentation;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.partition;
import static org.where2pair.SearchRequestBuilder.aSearchRequest;
import static org.where2pair.presentation.MapZoomType.CLOSE;
import static org.where2pair.presentation.MapZoomType.WIDE;

import java.util.List;

import org.robobinding.presentationmodel.ItemPresentationModel;
import org.robobinding.presentationmodel.PresentationModel;
import org.where2pair.Coordinates;
import org.where2pair.SearchRequest;
import org.where2pair.SimpleTime;
import org.where2pair.VenueFinder;
import org.where2pair.VenueWithDistances;
import org.where2pair.VenuesResultAction;
import org.where2pair.VenuesResultActionHandler;

import com.google.common.collect.ImmutableList;

@PresentationModel
public class VenueFinderPresentationModel implements VenuesResultActionHandler {

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
	private boolean searchButtonVisible;
	private boolean searchOptionsButtonVisible;
	private boolean mapButtonVisible;
	private boolean listButtonVisible;
	private boolean loadingIconVisible;
	private boolean resetButtonVisible;
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
		setResetButtonVisible(false);
		viewingVenueSearchResults = false;
		
		Coordinates currentLocation = locationProvider.getCurrentLocation();
		if (currentLocation != null) userLocations.add(currentLocation);
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
	public void notifyVenuesFound(List<VenueWithDistances> venues) {
		viewingVenueSearchResults = true;
		setVenues(venues);
		setSearchButtonVisible(false);
		setSearchOptionsButtonVisible(false);
		setLoadingIconVisible(false);
		setListButtonVisible(true);
		setResetButtonVisible(true);
		venuesObserver.notifyVenuesUpdated();
	}

	@Override
	public void notifyVenuesFindingFailed(String reason) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean hasMapMarkersToDisplay() {
		return !venues.isEmpty() || !userLocations.isEmpty();
	}
	
	public MapViewportState getMapViewPortState() {
		List<Coordinates> coordinatesBounds = newArrayList(userLocations);
		if (coordinatesBounds.isEmpty()) coordinatesBounds.add(LONDON);
		MapZoomType mapZoomType = WIDE;
		if (!venues.isEmpty()) {
			List<VenueWithDistances> closestVenues = partition(venues, 5).get(0);
			for (VenueWithDistances venueWithDistances : closestVenues) {
				coordinatesBounds.add(venueWithDistances.venue.getLocation());
			}
			mapZoomType = CLOSE;
		}
		return new MapViewportState(mapZoomType, coordinatesBounds);
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
	
	public void resetButtonPressed() {
		setDefaults();
		venuesViewTransitioner.resetDisplay();
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

	public boolean isResetButtonVisible() {
		return resetButtonVisible;
	}

	public void setResetButtonVisible(boolean resetButtonVisible) {
		this.resetButtonVisible = resetButtonVisible;
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
