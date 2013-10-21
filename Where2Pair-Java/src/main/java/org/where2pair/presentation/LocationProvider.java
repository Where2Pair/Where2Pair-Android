package org.where2pair.presentation;

public interface LocationProvider {

	void requestCurrentLocation(CurrentLocationObserver locationObserver);

}
