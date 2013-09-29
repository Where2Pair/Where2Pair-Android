package org.where2pair.presentation;

import org.robobinding.itempresentationmodel.ItemPresentationModel;
import org.robobinding.presentationmodel.AbstractPresentationModel;
import org.where2pair.VenueWithDistance;

public class VenueItemPresentationModel extends AbstractPresentationModel implements ItemPresentationModel<VenueWithDistance> {

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
		this.presentationModelChangeSupport.firePropertyChange("name");
		this.presentationModelChangeSupport.firePropertyChange("address");
		this.presentationModelChangeSupport.firePropertyChange("distance");
	}

}
