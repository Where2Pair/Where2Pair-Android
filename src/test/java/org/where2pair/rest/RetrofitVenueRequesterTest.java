package org.where2pair.rest;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.where2pair.*;
import retrofit.Callback;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.verify;
import static org.where2pair.SearchRequestBuilder.aSearchRequest;

public class RetrofitVenueRequesterTest {

    private static final SimpleTime CURRENT_TIME = new SimpleTime(12, 30);
    @Mock VenuesResultAction venuesResultAction;
    @Mock RetrofitVenueService venueService;
    @InjectMocks RetrofitVenueRequester venueRequester;


    @Test
    public void mapsTheSearchRequestToQueryParams() {
        SearchRequest searchRequest = aSearchRequest().openFrom(CURRENT_TIME).withWifi().withSeating().build();

        venueRequester.requestVenues(searchRequest, venuesResultAction);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(venueService).requestVenues("12.30", captor.capture(), any(Callback.class));
        String facilities = captor.getValue();

        assertThat(facilities, containsString("WIFI"));
        assertThat(facilities, containsString("SEATING"));
    }
}
