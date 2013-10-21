package org.where2pair.presentation;

import org.where2pair.Coordinates;

public interface UserLocationsObserver {

	void notifyUserLocationAdded(Coordinates location);

	void notifyUserLocationAddedAndZoomCamera(Coordinates location);
	
}
