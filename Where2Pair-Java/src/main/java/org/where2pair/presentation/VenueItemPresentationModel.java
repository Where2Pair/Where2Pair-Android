package org.where2pair.presentation;

import org.robobinding.itempresentationmodel.ItemPresentationModel;
import org.robobinding.presentationmodel.PresentationModel;
import org.where2pair.VenueWithDistance;

@PresentationModel
public class VenueItemPresentationModel implements ItemPresentationModel<VenueWithDistance> {

	private VenueWithDistance venueWithDistance;

	public String getName() {
		return venueWithDistance.venue.getName();
	}
	
	public String getAddress() {
		return venueWithDistance.venue.getAddress().addressLine1;
	}
	
	public String getDistance() {
		return String.format("%.2f", venueWithDistance.distance.get("location")) + "km";
	}
	
	@Override
	public void updateData(int index, VenueWithDistance venueWithDistance) {
		this.venueWithDistance = venueWithDistance;
	}

}
