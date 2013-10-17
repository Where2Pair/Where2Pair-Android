package org.where2pair;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import org.robobinding.binder.Binder;
import org.where2pair.presentation.UserLocationsObserver;
import org.where2pair.presentation.VenueFinderPresentationModel;
import org.where2pair.presentation.VenuesObserver;
import org.where2pair.presentation.VenuesViewTransitioner;
import org.where2pair.presentation.VenuesViewerPresentationModel;

import roboguice.RoboGuice;
import roboguice.activity.RoboFragmentActivity;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class VenuesActivity extends RoboFragmentActivity implements VenuesViewTransitioner {

    private VenuesMapFragment venuesMapFragment = new VenuesMapFragment();
    private VenuesListFragment venuesListFragment = new VenuesListFragment();
    @Inject VenueFinderPresentationModel venueFinderPresentationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binder.bind(this, R.layout.venues_activity, venueFinderPresentationModel);

        venueFinderPresentationModel.setVenuesViewTransitioner(this);

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.venues_container, venuesMapFragment)
                    .commit();
        }
    }

    @Override
    public void showMap() {
        getFragmentManager().beginTransaction()
                .replace(R.id.venues_container, venuesMapFragment)
                .commit();
    }

    @Override
    public void showList() {
        getFragmentManager().beginTransaction()
                .replace(R.id.venues_container, venuesListFragment)
                .commit();
    }

    public static class VenuesMapFragment extends MapFragment implements UserLocationsObserver, VenuesObserver {
        @Inject VenueFinderPresentationModel venueFinderPresentationModel;
        private GoogleMap googleMap;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(this);
            googleMap = getMap();
            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    venueFinderPresentationModel.addUserLocation(asCoordinates(latLng));
                }
            });

            venueFinderPresentationModel.setVenuesObserver(this);
            venueFinderPresentationModel.setUserLocationsObserver(this);
        }

        @Override
        public void onResume() {
            super.onResume();

            if (venueFinderPresentationModel.getUserLocations().isEmpty()) return;

            final LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            LatLng userLatLng = asLatLng(venueFinderPresentationModel.getUserLocations().get(0));
            googleMap.addMarker(new MarkerOptions()
                    .position(userLatLng)
                    .title("Me")
                    .icon(defaultMarker(220)));
            boundsBuilder.include(userLatLng);

            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 25));
                    googleMap.setOnCameraChangeListener(null);
                }
            });

        }

        @Override
        public void notifyLocationAdded(Coordinates location) {
            googleMap.addMarker(new MarkerOptions()
                    .position(asLatLng(location))
                    .icon(defaultMarker(220)));
        }

        @Override
        public void notifyVenuesUpdated() {
            if (venueFinderPresentationModel.getVenues().isEmpty()) return;

            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            ensureCameraBoundsForUserLocations(boundsBuilder);
            ensureCameraBoundsAndAddMarkersForVenues(boundsBuilder);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 25));
        }

        private void ensureCameraBoundsForUserLocations(LatLngBounds.Builder boundsBuilder) {
            for (Coordinates userLocation : venueFinderPresentationModel.getUserLocations()) {
                boundsBuilder.include(asLatLng(userLocation));
            }
        }

        private void ensureCameraBoundsAndAddMarkersForVenues(LatLngBounds.Builder boundsBuilder) {
            for (VenueWithDistance venueWithDistance : Lists.partition(venueFinderPresentationModel.getVenues(), 15).get(0)) {
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

    public static class VenuesListFragment extends Fragment {
        @Inject VenueFinderPresentationModel venuesFinderPresentationModel;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(this);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return Binder.bindView(getActivity(), R.layout.venues_list, venuesFinderPresentationModel);
        }
    }
}
