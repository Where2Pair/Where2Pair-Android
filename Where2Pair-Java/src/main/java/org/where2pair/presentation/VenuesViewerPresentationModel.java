package org.where2pair.presentation;

import java.util.List;

import org.robobinding.presentationmodel.AbstractPresentationModel;
import org.robobinding.presentationmodel.ItemPresentationModel;
import org.where2pair.VenueWithDistance;

public class VenuesViewerPresentationModel extends AbstractPresentationModel {

	private List<VenueWithDistance> venues;

	@ItemPresentationModel(VenueItemPresentationModel.class)
	public List<VenueWithDistance> getVenues() {
		return venues;
	}

	public void setVenues(List<VenueWithDistance> venues) {
		this.venues = venues;
	}

}
