package org.where2pair;

import static org.mockito.Mockito.verify;
import static org.where2pair.TestUtils.sampleVenues;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VenuesResultActionTest {

	@Mock VenuesResultActionHandler venuesResultActionHandler;
	
	@Test
	public void notifiesActionHandlerWhenVenuesSet() {
		List<Venue> venues = sampleVenues();
		VenuesResultAction venuesResultAction = new VenuesResultAction(venuesResultActionHandler);
		
		venuesResultAction.apply(venues);
		
		verify(venuesResultActionHandler).notifyVenuesFound(venues);
	}

    @Test
    public void notifiesActionHandlerWhenVenuesFindingFailed() {
        VenuesResultAction venuesResultAction = new VenuesResultAction(venuesResultActionHandler);
        String reason = "Network Error";

        venuesResultAction.failed(reason);

        verify(venuesResultActionHandler).notifyVenuesFindingFailed(reason);
    }
}
