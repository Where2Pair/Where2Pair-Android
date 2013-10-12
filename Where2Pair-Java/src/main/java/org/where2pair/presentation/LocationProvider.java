package org.where2pair.presentation;

import javax.annotation.Nullable;

import org.where2pair.Coordinates;


public interface LocationProvider {

	@Nullable
	Coordinates getCurrentLocation();

}
