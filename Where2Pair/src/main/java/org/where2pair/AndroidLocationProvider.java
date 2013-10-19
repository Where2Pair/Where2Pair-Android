package org.where2pair;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import org.where2pair.presentation.LocationProvider;

import static com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import static com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;

class AndroidLocationProvider implements LocationProvider, ConnectionCallbacks, OnConnectionFailedListener {

    private LocationClient locationClient;
    private int googlePlayServicesAvailable;
    private ConnectionResult connectionResult;

    AndroidLocationProvider(Context context) {
        googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        if (googlePlayServicesAvailable == ConnectionResult.SUCCESS) {
            locationClient = new LocationClient(context, this, this);
            locationClient.connect();
        }
    }

    @Override
    public Coordinates getCurrentLocation() {
        Log.d("*****", "Establishing connection: " + locationClient.isConnecting());
        if (locationClient == null || locationClient.isConnecting()) return null;

        Location lastLocation = locationClient.getLastLocation();

        if (lastLocation == null) return null;

        return new Coordinates(lastLocation.getLatitude(), lastLocation.getLongitude());
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {
        locationClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        this.connectionResult = connectionResult;
    }
}
