package org.where2pair;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.inject.Inject;

import org.where2pair.presentation.UserLocationsObserver;
import org.where2pair.presentation.VenueFinderPresentationModel;
import org.where2pair.presentation.VenuesObserver;

import roboguice.RoboGuice;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class VenuesMapFragment extends MapFragment implements UserLocationsObserver, VenuesObserver {
    private static final LatLng LONDON = new LatLng(51.5072, 0.1275);
    @Inject VenueFinderPresentationModel venueFinderPresentationModel;
    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(this);

        venueFinderPresentationModel.setVenuesObserver(this);
        venueFinderPresentationModel.setUserLocationsObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        googleMap = getMap();
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                venueFinderPresentationModel.mapLongPressed(asCoordinates(latLng));
            }
        });

        if (venueFinderPresentationModel.hasMapMarkersToDisplay()) {
            final LatLngBounds.Builder boundsBuilder = ensureCameraBoundsAndAddMarkers();

            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 25));
                    googleMap.setOnCameraChangeListener(null);
                }
            });
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LONDON, 15));
        }
    }

    @Override
    public void notifyUserLocationAdded(Coordinates location) {
        addUserLocationMarker(location);
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(250);
    }

    private void addUserLocationMarker(Coordinates location) {
        googleMap.addMarker(new MarkerOptions()
                .position(asLatLng(location))
                .icon(defaultMarker(220)));
    }

    @Override
    public void notifyVenuesUpdated() {
        googleMap.clear();
        LatLngBounds.Builder boundsBuilder = ensureCameraBoundsAndAddMarkers();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 25));
    }

    private LatLngBounds.Builder ensureCameraBoundsAndAddMarkers() {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        ensureCameraBoundsAndAddMarkersForUserLocations(boundsBuilder);
        ensureCameraBoundsAndAddMarkersForVenues(boundsBuilder);
        return boundsBuilder;
    }

    private void ensureCameraBoundsAndAddMarkersForUserLocations(LatLngBounds.Builder boundsBuilder) {
        for (Coordinates userLocation : venueFinderPresentationModel.getUserLocations()) {
            addUserLocationMarker(userLocation);
            boundsBuilder.include(asLatLng(userLocation));
        }
    }

    private void ensureCameraBoundsAndAddMarkersForVenues(LatLngBounds.Builder boundsBuilder) {
        for (VenueWithDistance venueWithDistance : venueFinderPresentationModel.getVenues()) {
            Venue venue = venueWithDistance.venue;
            LatLng latLng = asLatLng(venue.getLocation());
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(venue.getName())
                    .snippet(venue.getAddress().addressLine1 + "\n" + String.format("%.2f", venueWithDistance.distance.get("location")) + "km"));
            boundsBuilder.include(latLng);
        }
    }

    private LatLng asLatLng(Coordinates location) {
        return new LatLng(location.latitude, location.longitude);
    }

    private Coordinates asCoordinates(LatLng location) {
        return new Coordinates(location.latitude, location.longitude);
    }
}
