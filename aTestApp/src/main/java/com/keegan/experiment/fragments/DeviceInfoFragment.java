package com.keegan.experiment.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.keegan.experiment.R;
import com.keegan.experiment.utilities.DeviceInfo;

/**
 * Created by Keegan on 10/01/16.
 */
public class DeviceInfoFragment extends Fragment implements View.OnClickListener {

    TextView phoneNameTV;
    TextView phoneInfoTV;

    private final String TAG = UnderConstructionFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_info, container, false);
        phoneNameTV = (TextView) rootView.findViewById(R.id.Fragment_DeviceInfo_TextView_PhoneName);
        phoneInfoTV = (TextView) rootView.findViewById(R.id.Fragment_DeviceInfo_TextView_PhoneInfo);

        phoneNameTV.setText(DeviceInfo.getDeviceName());
        phoneInfoTV.setText(Html.fromHtml(DeviceInfo.getHTMLFormatDeviceInfo()));
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
