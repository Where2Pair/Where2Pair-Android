package org.where2pair.presentation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.where2pair.SearchRequestBuilder.aSearchRequest;
import static org.where2pair.SearchRequestMatcher.equalTo;
import static org.where2pair.TestUtils.sampleVenuesWithDistance;
import static org.where2pair.presentation.Screen.LOCATIONS_VIEW;
import static org.where2pair.presentation.Screen.VENUES_VIEW;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.where2pair.Coordinates;
import org.where2pair.SearchRequest;
import org.where2pair.SimpleTime;
import org.where2pair.VenueFinder;
import org.where2pair.VenueWithDistance;
import org.where2pair.VenuesResultAction;

@RunWith(MockitoJUnitRunner.class)
public class VenueFinderPresentationModelTest {

	private static final SimpleTime CURRENT_TIME = new SimpleTime(12, 30);
	private static final Coordinates CURRENT_LOCATION = new Coordinates(1.0, 0.1);
	@Mock VenueFinder venueFinder;
	@Mock TimeProvider timeProvider;
	@Mock LocationProvider locationProvider;
	@Mock VenuesViewerPresentationModel venuesViewerPresentationModel;
	@Mock ScreenNavigator screenNavigator;
	@InjectMocks VenueFinderPresentationModel venueFinderPresentationModel;
	
	@Test
	public void findsOpenVenuesNearCurrentLocationWithWifiAndSeatingWhenSearchButtonIsPressed() {
		given(timeProvider.getCurrentTime()).willReturn(CURRENT_TIME);
		given(locationProvider.getCurrentLocation()).willReturn(CURRENT_LOCATION);
		SearchRequest expectedSearchRequest = aSearchRequest().openFrom(CURRENT_TIME).near(CURRENT_LOCATION).withWifi().withSeating().build();
		
		venueFinderPresentationModel.pressSearchButton();
		
		assertThat(actualSearchRequest(), is(equalTo(expectedSearchRequest)));
	}
	
	@Test
	public void passesFoundVenuesOnToVenuesViewerPresentationModelAndNavigatesToScreen() {
		List<VenueWithDistance> venues = sampleVenuesWithDistance();
		
		venueFinderPresentationModel.notifyVenuesFound(venues);
		
		verify(venuesViewerPresentationModel).setVenues(venues);
		verify(screenNavigator).navigateTo(VENUES_VIEW);
	}
	
	@Test
	public void showsLocationsScreenWhenNoLocationCouldBeFound() {
		given(locationProvider.getCurrentLocation()).willReturn(null);
		
		venueFinderPresentationModel.pressSearchButton();
		
		verify(screenNavigator).navigateTo(LOCATIONS_VIEW);
	}
	
	private SearchRequest actualSearchRequest() {
		ArgumentCaptor<SearchRequest> captor = ArgumentCaptor.forClass(SearchRequest.class);
		verify(venueFinder).findVenues(captor.capture(), any(VenuesResultAction.class));
		return captor.getValue();
	}
}
