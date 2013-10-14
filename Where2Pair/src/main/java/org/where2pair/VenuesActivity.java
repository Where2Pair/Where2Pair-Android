package org.where2pair;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import org.where2pair.presentation.LocationProvider;
import org.where2pair.presentation.VenueFinderPresentationModel;
import org.where2pair.presentation.VenuesViewerPresentationModel;

import roboguice.RoboGuice;
import roboguice.activity.RoboFragmentActivity;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class VenuesActivity extends RoboFragmentActivity {

    private Menu menu;
    private VenuesMapFragment venuesMapFragment;
    private VenuesListFragment venuesListFragment;
    @Inject VenueFinderPresentationModel venueFinderPresentationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venues_activity);

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.venues_container, new VenuesMapFragment())
                    .commit();
        }

        getActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.venues, menu);
        hideOption(R.id.show_map);
        hideOption(R.id.show_list);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_options:
                showSearchOptions();
                return true;
            case R.id.search:
                search();
                return true;
            case R.id.show_map:
                showMap();
                return true;
            case R.id.show_list:
                showList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void search() {
        venueFinderPresentationModel.pressSearchButton();
    }

    private void showSearchOptions() {
    }

    private void showMap() {
        hideOption(R.id.show_map);
        showOption(R.id.show_list);

        if (venuesMapFragment == null) venuesMapFragment = new VenuesMapFragment();

        getFragmentManager().beginTransaction()
                .replace(R.id.venues_container, venuesMapFragment)
                .commit();
    }

    private void showList() {
        hideOption(R.id.show_list);
        showOption(R.id.show_map);

        if (venuesListFragment == null) venuesListFragment = new VenuesListFragment();

        getFragmentManager().beginTransaction()
                .replace(R.id.venues_container, venuesListFragment)
                .commit();
    }

    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }

    public static class VenuesMapFragment extends MapFragment {
        @Inject VenuesViewerPresentationModel venuesViewerPresentationModel;
        @Inject LocationProvider locationProvider;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(this);
        }

        @Override
        public void onResume() {
            final GoogleMap googleMap = getMap();

            final LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            LatLng userLatLng = asLatLng(locationProvider.getCurrentLocation());
            googleMap.addMarker(new MarkerOptions()
                    .position(userLatLng)
                    .title("Me")
                    .icon(defaultMarker(220)));
            boundsBuilder.include(userLatLng);

            if (venuesViewerPresentationModel.getVenues().size() > 0) {
                for (VenueWithDistance venueWithDistance : Lists.partition(venuesViewerPresentationModel.getVenues(), 15).get(0)) {
                    Venue venue = venueWithDistance.venue;
                    LatLng latLng = asLatLng(venue.getLocation());
                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(venue.getName())
                            .snippet(venue.getAddress().addressLine1 + "\n" + String.format("%.2f", venueWithDistance.distance.get("location")) + "km"));
                    boundsBuilder.include(latLng);
                }
            }

            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 25));
                    googleMap.setOnCameraChangeListener(null);
                }
            });

            super.onResume();
        }

        private LatLng asLatLng(Coordinates location) {
            return new LatLng(location.latitude, location.longitude);
        }
    }

    public static class VenuesListFragment extends Fragment {
        @Inject VenuesViewerPresentationModel venuesViewerPresentationModel;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(this);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return Binder.bindView(getActivity(), R.layout.venues_list, venuesViewerPresentationModel);
        }
    }
}
