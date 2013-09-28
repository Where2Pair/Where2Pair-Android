package org.where2pair;

import org.where2pair.presentation.LocationProvider;

public class AndroidLocationProvider implements LocationProvider {

    @Override
    public Coordinates getCurrentLocation() {
        return new Coordinates(51.520547,-0.082103);
    }
}
