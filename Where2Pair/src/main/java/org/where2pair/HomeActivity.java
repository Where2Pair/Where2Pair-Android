package org.where2pair;

import android.os.Bundle;

import com.google.inject.Inject;

import org.robobinding.binder.Binder;
import org.where2pair.presentation.VenueFinderPresentationModel;

import roboguice.activity.RoboActivity;

public class HomeActivity extends RoboActivity {

    @Inject VenueFinderPresentationModel venueFinderPresentationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binder.bind(this, R.layout.home_activity, venueFinderPresentationModel);
    }

}
