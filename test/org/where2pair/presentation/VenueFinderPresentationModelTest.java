package org.where2pair.presentation;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.where2pair.SearchRequestBuilder.aSearchRequest;
import static org.where2pair.presentation.Screen.VENUES_VIEW;
import static org.where2pair.presentation.VenueFinderPresentationModelTest.SearchRequestMatcher.equalTo;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.where2pair.SearchRequest;
import org.where2pair.SimpleTime;
import org.where2pair.Venue;
import org.where2pair.VenueFinder;
import org.where2pair.VenuesResultAction;

@RunWith(MockitoJUnitRunner.class)
public class VenueFinderPresentationModelTest {

	private static final SimpleTime CURRENT_TIME = new SimpleTime(12, 30);
	@Mock VenueFinder venueFinder;
	@Mock TimeProvider timeProvider;
	@Mock VenuesViewerPresentationModel venuesViewerPresentationModel;
	@Mock ScreenNavigator screenNavigator;
	@InjectMocks VenueFinderPresentationModel venueFinderPresentationModel;
	
	@Test
	public void findsOpenVenuesWithWifiAndSeatingWhenSearchButtonIsPressed() {
		given(timeProvider.getCurrentTime()).willReturn(CURRENT_TIME);
		SearchRequest expectedSearchRequest = aSearchRequest().openFrom(CURRENT_TIME).withWifi().withSeating().build();
		
		venueFinderPresentationModel.pressSearchButton();
		
		assertThat(actualSearchRequest(), is(equalTo(expectedSearchRequest)));
	}
	
	@Test
	public void passesFoundVenuesOnToVenuesViewerPresentationModelAndNavigatesToScreen() {
		List<Venue> venues = newArrayList(new Venue(), new Venue());
		
		venueFinderPresentationModel.notifyVenuesFound(venues);
		
		verify(venuesViewerPresentationModel).setVenues(venues);
		verify(screenNavigator).navigateTo(VENUES_VIEW);
	}
	
	private SearchRequest actualSearchRequest() {
		ArgumentCaptor<SearchRequest> captor = ArgumentCaptor.forClass(SearchRequest.class);
		verify(venueFinder).fetchVenues(captor.capture(), any(VenuesResultAction.class));
		return captor.getValue();
	}
	
	static class SearchRequestMatcher extends TypeSafeMatcher<SearchRequest> {
		private SearchRequest expectedSearchRequest;

		SearchRequestMatcher(SearchRequest expectedSearchRequest) {
			this.expectedSearchRequest = expectedSearchRequest;
		}

		@Override
		public void describeTo(Description description) {
			description.appendText(expectedSearchRequest.toString());
		}

		@Override
		protected boolean matchesSafely(SearchRequest actualSearchRequest) {
			SimpleTime expectedOpen = expectedSearchRequest.getOpenFrom();
			SimpleTime actualOpen = actualSearchRequest.getOpenFrom();
			return expectedOpen.hour == actualOpen.hour && expectedOpen.minute == actualOpen.minute 
					&& expectedSearchRequest.getFacilities().equals(actualSearchRequest.getFacilities());
		}
	
		public static SearchRequestMatcher equalTo(SearchRequest expectedSearchRequest) {
			return new SearchRequestMatcher(expectedSearchRequest); 
		}
		
	}
}
