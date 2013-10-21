package org.where2pair;

import java.util.List;

public interface VenuesResultActionHandler {

	void notifyVenuesFound(List<VenueWithDistances> venues);

    void notifyVenuesFindingFailed(String reason);
}
