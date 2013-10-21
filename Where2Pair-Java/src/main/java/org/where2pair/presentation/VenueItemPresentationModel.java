package org.where2pair.presentation;

import org.robobinding.itempresentationmodel.ItemPresentationModel;
import org.robobinding.presentationmodel.PresentationModel;
import org.where2pair.VenueWithDistances;

@PresentationModel
public class VenueItemPresentationModel implements ItemPresentationModel<VenueWithDistances> {

	private VenueWithDistances venueWithDistance;

	public String getName() {
		return venueWithDistance.venue.getName();
	}
	
	public String getAddress() {
		return venueWithDistance.venue.getAddress().addressLine1;
	}
	
	public String getDistance() {
		return String.format("%.2f", venueWithDistance.distances.get("location")) + "km";
	}
	
	@Override
	public void updateData(int index, VenueWithDistances venueWithDistance) {
		this.venueWithDistance = venueWithDistance;
	}
}
