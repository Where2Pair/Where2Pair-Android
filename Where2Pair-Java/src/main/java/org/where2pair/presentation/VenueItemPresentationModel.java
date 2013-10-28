package org.where2pair.presentation;

import org.robobinding.itempresentationmodel.ItemPresentationModel;
import org.robobinding.presentationmodel.PresentationModel;
import org.where2pair.VenueWithDistances;

@PresentationModel
public class VenueItemPresentationModel implements ItemPresentationModel<VenueWithDistances> {

	private VenueWithDistances venueWithDistances;

	public String getName() {
		return venueWithDistances.venue.getName();
	}
	
	public String getAddress() {
		return venueWithDistances.venue.getAddress().addressLine1;
	}
	
	public String getDistance() {
		return venueWithDistances.averageDistance.toHumanReadableString();
	}
	
	@Override
	public void updateData(int index, VenueWithDistances venueWithDistances) {
		this.venueWithDistances = venueWithDistances;
	}
}
