package org.where2pair;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import org.robobinding.binder.Binder;
import org.where2pair.presentation.LocationProvider;
import org.where2pair.presentation.VenuesViewerPresentationModel;

import roboguice.RoboGuice;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class VenuesActivity extends RoboFragmentActivity {

    @InjectView(R.id.venues_pager) ViewPager venuesPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venues_activity);

        venuesPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
    }

    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new VenuesMapFragment();
            }

            return new VenuesListFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public static class VenuesMapFragment extends SupportMapFragment {
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

            //TODO Zoom, Show current location, Show more venue details
            final LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            LatLng userLatLng = asLatLng(locationProvider.getCurrentLocation());
            googleMap.addMarker(new MarkerOptions()
                    .position(userLatLng)
                    .icon(defaultMarker(220)));
            boundsBuilder.include(userLatLng);

            for (VenueWithDistance venueWithDistance : Lists.partition(venuesViewerPresentationModel.getVenues(), 15).get(0)) {
                Venue venue = venueWithDistance.venue;
                LatLng latLng = asLatLng(venue.getLocation());
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(venue.getName())
                        .snippet(venue.getAddress().addressLine1));
                boundsBuilder.include(latLng);
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
