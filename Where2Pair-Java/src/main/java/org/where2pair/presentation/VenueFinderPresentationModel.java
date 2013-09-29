package org.where2pair.presentation;

import static org.where2pair.SearchRequestBuilder.aSearchRequest;
import static org.where2pair.presentation.Screen.VENUES_VIEW;

import java.util.List;

import org.where2pair.Coordinates;
import org.where2pair.SearchRequest;
import org.where2pair.SimpleTime;
import org.where2pair.VenueFinder;
import org.where2pair.VenueWithDistance;
import org.where2pair.VenuesResultAction;
import org.where2pair.VenuesResultActionHandler;

public class VenueFinderPresentationModel implements VenuesResultActionHandler {
	private VenueFinder venueFinder;
	private TimeProvider timeProvider;
	private LocationProvider locationProvider;
	private VenuesViewerPresentationModel venuesViewerPresentationModel;
	private ScreenNavigator screenNavigator;

	public VenueFinderPresentationModel(VenueFinder venueFinder,
			TimeProvider timeProvider, LocationProvider locationProvider,
			VenuesViewerPresentationModel venuesViewerPresentationModel,
			ScreenNavigator screenNavigator) {
		this.venueFinder = venueFinder;
		this.timeProvider = timeProvider;
		this.locationProvider = locationProvider;
		this.venuesViewerPresentationModel = venuesViewerPresentationModel;
		this.screenNavigator = screenNavigator;
	}

	public void pressSearchButton() {
		SimpleTime currentTime = timeProvider.getCurrentTime();
		Coordinates currentLocation = locationProvider.getCurrentLocation();
		SearchRequest searchRequest = aSearchRequest().openFrom(currentTime).near(currentLocation).withWifi().withSeating().build();
		venueFinder.findVenues(searchRequest, new VenuesResultAction(this));
	}

	@Override
	public void notifyVenuesFound(List<VenueWithDistance> venues) {
		venuesViewerPresentationModel.setVenues(venues);
		screenNavigator.navigateTo(VENUES_VIEW);
	}

    @Override
    public void notifyVenuesFindingFailed(String reason) {

    }

}
