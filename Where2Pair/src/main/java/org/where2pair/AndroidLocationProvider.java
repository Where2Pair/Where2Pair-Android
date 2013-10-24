package org.where2pair;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import org.where2pair.presentation.CurrentLocationObserver;
import org.where2pair.presentation.LocationProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import static com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

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

    public void requestCurrentLocation(final CurrentLocationObserver locationObserver) {
        final Handler handler = new Handler();
        ExecutorService executorService = newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                if (locationClient == null) return;

                while (locationClient.isConnecting()) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {}
                }

                final Location lastLocation = locationClient.getLastLocation();

                if (lastLocation == null) return;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        locationObserver.notifyCurrentLocation(new Coordinates(lastLocation.getLatitude(), lastLocation.getLongitude()));
                    }
                });
            }
        });
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
