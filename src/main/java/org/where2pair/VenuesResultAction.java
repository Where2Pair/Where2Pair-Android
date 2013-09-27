package org.where2pair;

import java.util.List;

public class VenuesResultAction {

	private final VenuesResultActionHandler venuesResultActionHandler;

	public VenuesResultAction(VenuesResultActionHandler venuesResultActionHandler) {
		this.venuesResultActionHandler = venuesResultActionHandler;
	}

	public void apply(List<Venue> venues) {
		venuesResultActionHandler.notifyVenuesFound(venues);
	}

    public void failed(String reason) {
        venuesResultActionHandler.notifyVenuesFindingFailed(reason);
    }
}
