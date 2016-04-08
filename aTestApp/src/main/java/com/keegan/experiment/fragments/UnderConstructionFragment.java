package com.keegan.experiment.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.inject.Inject;
import com.keegan.experiment.R;

import roboguice.fragment.RoboFragment;

/**
 * Created by Keegan on 10/01/16.
 */
public class UnderConstructionFragment extends RoboFragment implements OnClickListener {

    private final String TAG = UnderConstructionFragment.class.getSimpleName();

    @Inject
    Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.c_fragment_under_construction, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        initializeViewObjects();
    }

    private void initializeViewObjects() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
