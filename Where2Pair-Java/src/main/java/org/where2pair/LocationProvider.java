package org.where2pair;

import org.where2pair.presentation.CurrentLocationObserver;

public interface LocationProvider {

	void requestCurrentLocation(CurrentLocationObserver locationObserver);

}
