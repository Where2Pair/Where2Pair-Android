package org.where2pair;

import java.util.List;

public interface VenuesResultActionHandler {

	void notifyVenuesFound(List<VenueWithDistance> venues);

    void notifyVenuesFindingFailed(String reason);
}
