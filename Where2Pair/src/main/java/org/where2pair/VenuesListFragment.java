package org.where2pair;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;

import org.robobinding.binder.Binder;
import org.where2pair.presentation.VenueFinderPresentationModel;

import roboguice.RoboGuice;

public class VenuesListFragment extends Fragment {
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
