package org.where2pair.activity;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.inject.Inject;

import org.where2pair.Coordinates;
import org.where2pair.Venue;
import org.where2pair.VenueWithDistances;
import org.where2pair.presentation.MapViewportState;
import org.where2pair.presentation.UserLocationsObserver;
import org.where2pair.presentation.VenueFinderPresentationModel;
import org.where2pair.presentation.VenuesObserver;

import roboguice.RoboGuice;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds;
import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class VenuesMapFragment extends MapFragment implements UserLocationsObserver, VenuesObserver {
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
        resetDisplay();
    }


    public void resetDisplay() {
        googleMap.clear();
        addUserLocationMarkers();
        addVenueMarkers();

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                animateCameraToViewportBounds();
                googleMap.setOnCameraChangeListener(null);
            }
        });
    }

    private void addUserLocationMarkers() {
        for (Coordinates userLocation : venueFinderPresentationModel.getUserLocations()) {
            addUserLocationMarker(userLocation);
        }
    }

    private void addUserLocationMarker(Coordinates location) {
        googleMap.addMarker(new MarkerOptions()
                .position(asLatLng(location))
                .icon(defaultMarker(220)));
    }

    private void addVenueMarkers() {
        for (VenueWithDistances venueWithDistances : venueFinderPresentationModel.getVenues()) {
            Venue venue = venueWithDistances.venue;
            LatLng latLng = asLatLng(venue.getLocation());
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(venue.getName())
                    .snippet(venue.getAddress().addressLine1 + "\n" + String.format("%.2f", venueWithDistances.averageDistance.distance + "km")));
        }
    }

    private void animateCameraToViewportBounds() {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        MapViewportState viewportState = venueFinderPresentationModel.getMapViewPortState();
        for (Coordinates c : viewportState.coordinateBounds) {
            boundsBuilder.include(asLatLng(c));
        }
        googleMap.animateCamera(newLatLngBounds(boundsBuilder.build(), 25));
    }

    @Override
    public void notifyUserLocationAdded(Coordinates location) {
        addUserLocationMarker(location);
    }

    @Override
    public void notifyVenuesUpdated() {
        addVenueMarkers();
        animateCameraToViewportBounds();
    }

    private LatLng asLatLng(Coordinates location) {
        return new LatLng(location.latitude, location.longitude);
    }

    private Coordinates asCoordinates(LatLng location) {
        return new Coordinates(location.latitude, location.longitude);
    }
}
