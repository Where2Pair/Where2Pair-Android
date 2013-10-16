package org.where2pair.presentation;

import static org.where2pair.SearchRequestBuilder.aSearchRequest;
import static org.where2pair.presentation.Screen.LOCATIONS_VIEW;
import static org.where2pair.presentation.Screen.VENUES_VIEW;

import java.util.List;

import org.where2pair.Coordinates;
import org.where2pair.SearchRequest;
import org.where2pair.SimpleTime;
import org.where2pair.VenueFinder;
import org.where2pair.VenueWithDistance;
import org.where2pair.VenuesResultAction;
import org.where2pair.VenuesResultActionHandler;

public class VenueFinderPresentationModelOld implements VenuesResultActionHandler {
	private VenueFinder venueFinder;
	private TimeProvider timeProvider;
	private LocationProvider locationProvider;
	private VenueFinderPresentationModel venuesViewerPresentationModel;
	private ScreenNavigator screenNavigator;

	public VenueFinderPresentationModelOld(VenueFinder venueFinder,
			TimeProvider timeProvider, LocationProvider locationProvider,
			VenueFinderPresentationModel venuesViewerPresentationModel,
			ScreenNavigator screenNavigator) {
		this.venueFinder = venueFinder;
		this.timeProvider = timeProvider;
		this.locationProvider = locationProvider;
		this.venuesViewerPresentationModel = venuesViewerPresentationModel;
		this.screenNavigator = screenNavigator;
	}

    //bind map onto presentation model?
    //create a custom adapter that listens to venue change actions?
    //how about building bounds?

    //This can all be done by adding MenuInflater support
    //screen navigator has showMap(), showList(), showSearchOptions() actions

    //public boolean showMapButtonVisible(){};

    //etc

	public void pressSearchButton() {
		Coordinates currentLocation = locationProvider.getCurrentLocation();
		
		if (currentLocation == null) 
			screenNavigator.navigateTo(LOCATIONS_VIEW);
		else 
			requestVenuesFromServer(currentLocation);
	}

	private void requestVenuesFromServer(Coordinates currentLocation) {
		SimpleTime currentTime = timeProvider.getCurrentTime();
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
