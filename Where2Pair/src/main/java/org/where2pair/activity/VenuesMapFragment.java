package org.where2pair.activity;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.inject.Inject;

import org.where2pair.Coordinates;
import org.where2pair.Venue;
import org.where2pair.VenueWithDistances;
import org.where2pair.presentation.UserLocationsObserver;
import org.where2pair.presentation.VenueFinderPresentationModel;
import org.where2pair.presentation.VenuesObserver;

import java.util.List;
import java.util.Map;

import roboguice.RoboGuice;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds;
import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;
import static com.google.common.collect.Maps.newHashMap;

public class VenuesMapFragment extends MapFragment implements UserLocationsObserver, VenuesObserver {
    @Inject VenueFinderPresentationModel venueFinderPresentationModel;
    private GoogleMap googleMap;
    private Map<LatLng, Marker> mapMarkers = newHashMap();
    private Map<LatLng, Coordinates> mapCoordinates = newHashMap();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(this);

        venueFinderPresentationModel.setVenuesObserver(this);
        venueFinderPresentationModel.setUserLocationsObserver(this);
        Log.d("*****OnCreate*******", "Creating!!!!!!!!!!!!!!!!");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("*****OnResume*******", "Resuming!!!!!!!!!!!!!!!!");

        googleMap = getMap();
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                venueFinderPresentationModel.mapLongPressed(asCoordinates(latLng));
            }
        });
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Coordinates markerCoordinates = mapCoordinates.get(marker.getPosition());

                if (isUserLocation(markerCoordinates)) {
                    venueFinderPresentationModel.userLocationPressed(markerCoordinates);
                    return true;
                }

                return false;
            }

            private boolean isUserLocation(Coordinates markerCoordinates) {
                for (Coordinates c : venueFinderPresentationModel.getUserLocations()) {
                    if (c.equals(markerCoordinates))
                        return true;
                }

                return false;
            }
        });
        resetDisplay();

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                animateCameraToViewportBounds();
                googleMap.setOnCameraChangeListener(null);
            }
        });
    }

    public void resetDisplay() {
        googleMap.clear();
        mapMarkers.clear();
        mapCoordinates.clear();
        addUserLocationMarkers();
        addVenueMarkers();
    }

    private void addUserLocationMarkers() {
        for (Coordinates userLocation : venueFinderPresentationModel.getUserLocations()) {
            addUserLocationMarker(userLocation);
        }
    }

    private void addUserLocationMarker(Coordinates location) {
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(asLatLng(location))
                .icon(defaultMarker(220)));

        mapMarkers.put(marker.getPosition(), marker);
        mapCoordinates.put(marker.getPosition(), location);
    }

    private void addVenueMarkers() {
        int venueIndex = 0;
        for (VenueWithDistances venueWithDistances : venueFinderPresentationModel.getVenues()) {
            Venue venue = venueWithDistances.venue;
            LatLng latLng = asLatLng(venue.getLocation());
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(venue.getName())
                    .snippet(venue.getAddress().addressLine1 + " " + venueWithDistances.averageDistance.toHumanReadableString()));

            if (venueIndex++ == 0) {
                marker.showInfoWindow();
            }
        }
    }

    private void animateCameraToViewportBounds() {
        List<Coordinates> mapViewportBounds = venueFinderPresentationModel.getMapViewportBounds();

        if (mapViewportBounds.size() == 1) {
            googleMap.animateCamera(newLatLngZoom(asLatLng(mapViewportBounds.get(0)), 15));
        }
        else {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (Coordinates c : mapViewportBounds) {
                boundsBuilder.include(asLatLng(c));
            }

            googleMap.animateCamera(newLatLngBounds(boundsBuilder.build(), mapMarkerPadding()));
        }
    }

    private int mapMarkerPadding() {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        return Math.min(width, height)/6;
    }

    @Override
    public void notifyUserLocationAdded(Coordinates location) {
        addUserLocationMarker(location);
    }

    @Override
    public void notifyUserLocationAddedAndZoomCamera(Coordinates location) {
        addUserLocationMarker(location);
        animateCameraToViewportBounds();
    }

    @Override
    public void notifyUserLocationRemoved(Coordinates coordinates) {
        LatLng position = null;

        for (Map.Entry<LatLng, Coordinates> entry : mapCoordinates.entrySet()) {
            if (entry.getValue().equals(coordinates)) {
                position = entry.getKey();
                break;
            }
        }

        mapMarkers.get(position).remove();
        mapMarkers.remove(position);
        mapCoordinates.remove(position);
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
