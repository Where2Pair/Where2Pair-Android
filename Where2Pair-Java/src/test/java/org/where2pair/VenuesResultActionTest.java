package org.where2pair;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.verify;

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
		List<Venue> venues = newArrayList(new Venue(), new Venue());
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
