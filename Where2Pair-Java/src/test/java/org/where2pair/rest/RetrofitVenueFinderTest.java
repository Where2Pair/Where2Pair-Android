package org.where2pair.rest;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.where2pair.SearchRequestBuilder.aSearchRequest;

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
import org.where2pair.VenuesResultAction;

import retrofit.Callback;

@RunWith(MockitoJUnitRunner.class)
public class RetrofitVenueFinderTest {

    private static final SimpleTime CURRENT_TIME = new SimpleTime(12, 30);
    private static final Coordinates CURRENT_LOCATION = new Coordinates(51.520547, -0.082103);
    private static final Coordinates ANOTHER_LOCATION = new Coordinates(52.520547, -0.092103);
    @Mock VenuesResultAction venuesResultAction;
    @Mock RetrofitVenueService venueService;
    @InjectMocks RetrofitVenueFinder venueRequester;

    @SuppressWarnings("unchecked")
	@Test
    public void mapsTheSearchRequestToQueryParams() {
        SearchRequest searchRequest = aSearchRequest().openFrom(CURRENT_TIME)
        		.near(CURRENT_LOCATION)
        		.near(ANOTHER_LOCATION)
        		.withWifi()
        		.withSeating().build();
        List<String> expectedCoordinates = newArrayList("51.520547,-0.082103", "52.520547,-0.092103");
        
        venueRequester.findVenues(searchRequest, venuesResultAction);

        ArgumentCaptor<String> facilitiesCaptor = ArgumentCaptor.forClass(String.class);
        verify(venueService).requestVenues(eq(expectedCoordinates), eq("12.30"), facilitiesCaptor.capture(), any(Callback.class));
        assertThat(facilitiesCaptor.getValue(), containsString("WIFI"));
        assertThat(facilitiesCaptor.getValue(), containsString("SEATING"));
    }
}
