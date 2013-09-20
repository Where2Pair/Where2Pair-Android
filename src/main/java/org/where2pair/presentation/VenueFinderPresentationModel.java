package org.where2pair.presentation;

import static org.where2pair.SearchRequestBuilder.aSearchRequest;
import static org.where2pair.presentation.Screen.VENUES_VIEW;

import java.util.List;

import org.where2pair.SearchRequest;
import org.where2pair.SimpleTime;
import org.where2pair.Venue;
import org.where2pair.VenueFinder;
import org.where2pair.VenuesResultAction;
import org.where2pair.VenuesResultActionHandler;

public class VenueFinderPresentationModel implements VenuesResultActionHandler {
	private VenueFinder venueFinder;
	private TimeProvider timeProvider;
	private VenuesViewerPresentationModel venuesViewerPresentationModel;
	private ScreenNavigator screenNavigator;

	public void pressSearchButton() {
		SimpleTime currentTime = timeProvider.getCurrentTime();
		SearchRequest searchRequest = aSearchRequest().openFrom(currentTime).withWifi().withSeating().build();
		venueFinder.findVenues(searchRequest, new VenuesResultAction(this));
	}

	@Override
	public void notifyVenuesFound(List<Venue> venues) {
		venuesViewerPresentationModel.setVenues(venues);
		screenNavigator.navigateTo(VENUES_VIEW);
	}

}
