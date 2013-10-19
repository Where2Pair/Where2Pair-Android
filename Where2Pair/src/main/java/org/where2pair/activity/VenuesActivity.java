package org.where2pair.activity;

import android.os.Bundle;

import com.google.inject.Inject;

import org.robobinding.binder.Binder;
import org.where2pair.R;
import org.where2pair.presentation.VenueFinderPresentationModel;
import org.where2pair.presentation.VenuesViewTransitioner;

import roboguice.activity.RoboFragmentActivity;

public class VenuesActivity extends RoboFragmentActivity implements VenuesViewTransitioner {

    private VenuesMapFragment venuesMapFragment = new VenuesMapFragment();
    private VenuesListFragment venuesListFragment = new VenuesListFragment();
    private boolean mapShowing;
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
        mapShowing = true;
    }

    @Override
    public void showMap() {
        getFragmentManager().beginTransaction()
                .replace(R.id.venues_container, venuesMapFragment)
                .commit();
        mapShowing = true;
    }

    @Override
    public void showList() {
        getFragmentManager().beginTransaction()
                .replace(R.id.venues_container, venuesListFragment)
                .commit();
        mapShowing = false;
    }

    @Override
    public void resetDisplay() {
        if (!mapShowing) showMap();

        venuesMapFragment.resetDisplay();
    }
}
