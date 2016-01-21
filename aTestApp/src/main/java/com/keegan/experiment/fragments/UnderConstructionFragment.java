package com.keegan.experiment.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keegan.experiment.R;


//import com.mobilis.android.nfc.util.TextModifier;

/**
 * Created by ahmed on 10/06/14.
 */
public class UnderConstructionFragment extends Fragment implements View.OnClickListener{

    private final String TAG = UnderConstructionFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.c_fragment_under_construction, container, false);
        return rootView;
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
    }
}
