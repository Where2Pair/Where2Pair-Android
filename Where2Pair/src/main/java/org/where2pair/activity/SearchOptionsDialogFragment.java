package org.where2pair.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;

import org.robobinding.binder.Binder;
import org.where2pair.R;
import org.where2pair.presentation.SearchOptionsPresentationModel;

import roboguice.RoboGuice;

public class SearchOptionsDialogFragment extends DialogFragment {

    @Inject
    private SearchOptionsPresentationModel searchOptionsPresentationModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return Binder.bindView(getActivity(), R.layout.search_options, searchOptionsPresentationModel);
    }
}
