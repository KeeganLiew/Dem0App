package com.keegan.experiment.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.inject.Inject;
import com.keegan.experiment.R;
import com.keegan.experiment.utilities.DeviceInfo;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by Keegan on 10/01/16.
 */
public class DeviceInfoFragment extends RoboFragment implements OnClickListener {

    private final String TAG = UnderConstructionFragment.class.getSimpleName();

    @Inject
    Activity mActivity;

    @InjectView(R.id.Fragment_DeviceInfo_TextView_PhoneName)
    private TextView phoneNameTV;
    @InjectView(R.id.Fragment_DeviceInfo_TextView_PhoneInfo)
    private TextView phoneInfoTV;
    //findViewById injects
    /*private TextView phoneNameTV;
    private TextView phoneInfoTV;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_info, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        initializeViewObjects();
    }

    private void initializeViewObjects() {
        /*phoneNameTV = (TextView) rootView.findViewById(R.id.Fragment_DeviceInfo_TextView_PhoneName);
        phoneInfoTV = (TextView) rootView.findViewById(R.id.Fragment_DeviceInfo_TextView_PhoneInfo);*/
        phoneNameTV.setText(DeviceInfo.getDeviceName());
        phoneInfoTV.setText(Html.fromHtml(DeviceInfo.getHTMLFormatDeviceInfo()));
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
            ////TODO: 16/02/16 Copy all info to clipboard
        }
    }

}
