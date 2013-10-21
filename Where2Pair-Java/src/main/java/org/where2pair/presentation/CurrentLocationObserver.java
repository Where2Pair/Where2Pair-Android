package org.where2pair.presentation;

import org.where2pair.Coordinates;

public interface CurrentLocationObserver {

	void notifyCurrentLocation(Coordinates coordinates);
	
}
