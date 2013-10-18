package org.where2pair;

import android.app.Fragment;
import android.os.Bundle;

import com.google.inject.Inject;

import org.robobinding.binder.Binder;
import org.where2pair.presentation.VenueFinderPresentationModel;
import org.where2pair.presentation.VenuesViewTransitioner;

import roboguice.activity.RoboFragmentActivity;

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
}
