package com.keegan.experiment.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.keegan.experiment.R;

/**
 * Created by Keegan on 10/01/16.
 */
public class UnderConstructionFragment extends Fragment implements OnClickListener {

    private final String TAG = UnderConstructionFragment.class.getSimpleName();
    private Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.c_fragment_under_construction, container, false);
        mActivity = getActivity();
        initializeViewObjects(rootView);
        return rootView;
    }

    private void initializeViewObjects(View rootView) {
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
