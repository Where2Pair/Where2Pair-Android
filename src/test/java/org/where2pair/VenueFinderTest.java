package org.where2pair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import retrofit.Callback;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.where2pair.SearchRequestBuilder.aSearchRequest;

@RunWith(MockitoJUnitRunner.class)
public class VenueFinderTest {

	private static final SimpleTime CURRENT_TIME = new SimpleTime(12, 30);
	@Mock VenuesResultAction venuesResultAction;
	@Mock
    VenueRequester venueRequester;
    @Mock Callback<List<Venue>> venuesCallback;
	@InjectMocks VenueFinder venueFinder;
	
	@SuppressWarnings("unchecked")
    @Test
	public void mapsASearchRequestIntoAWebRequestWithAnAsyncCallback() {
		SearchRequest searchRequest = aSearchRequest().openFrom(CURRENT_TIME).withWifi().withSeating().build();

		venueFinder.findVenues(searchRequest, venuesResultAction);

        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        verify(venueRequester).requestVenues(captor.capture(), any(VenuesResultAction.class));
        Map<String, String> searchCriteria = (Map<String, String>)captor.getValue();
        assertThat(searchCriteria.get("openFrom"), equalTo("12.30"));
	    assertThat(searchCriteria.get("withFacilities"), containsString("WIFI"));
	    assertThat(searchCriteria.get("withFacilities"), containsString("SEATING"));
    }
	
}
