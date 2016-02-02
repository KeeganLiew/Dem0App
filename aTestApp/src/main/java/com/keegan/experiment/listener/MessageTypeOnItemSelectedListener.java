package com.keegan.experiment.listener;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.keegan.experiment.R;

/**
 * Created by Keegan on 22/01/16.
 */
public class MessageTypeOnItemSelectedListener implements OnItemSelectedListener {

    private Activity mActivity;

    public MessageTypeOnItemSelectedListener(Activity activity) {
        mActivity = activity;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.d("OnItemSelected(Type)", "Selected " + pos);
        //final RelativeLayout extraFieldRL = (RelativeLayout) mActivity.findViewById(R.id.Fragment_Banks_RelativeLayout_Extra_Field);
        final TextView selectBankType = (TextView) mActivity.findViewById(R.id.Fragment_Sms_TextView_MessageType);

        selectBankType.setText(parent.getSelectedItem().toString());
        selectBankType.setTextColor(ContextCompat.getColor(mActivity, R.color.app_main_text_color));

        if (pos == 0) {
            //extraFieldRL.setVisibility(View.GONE);
        } else if (pos == 1) {
            //extraFieldRL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

}
