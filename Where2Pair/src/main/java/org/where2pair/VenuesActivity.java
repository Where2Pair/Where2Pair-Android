package org.where2pair;

import android.os.Bundle;
import android.util.Log;

import com.google.inject.Inject;

import org.robobinding.binder.Binder;
import org.where2pair.presentation.VenuesViewerPresentationModel;

import roboguice.activity.RoboActivity;

public class VenuesActivity extends RoboActivity {

    @Inject VenuesViewerPresentationModel venuesViewerPresentationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("*******", "Found " + venuesViewerPresentationModel.getVenues().size() + " venues");
        Binder.bind(this, R.layout.venues_activity, venuesViewerPresentationModel);
    }
    
}
