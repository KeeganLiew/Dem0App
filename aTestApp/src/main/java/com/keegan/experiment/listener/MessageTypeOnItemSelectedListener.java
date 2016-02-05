package com.keegan.experiment.listener;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.TextView;

import com.keegan.experiment.R;

/**
 * Created by Keegan on 22/01/16.
 */
//TODO to move this class
public class MessageTypeOnItemSelectedListener implements OnItemSelectedListener {
    private Activity mActivity;

    public MessageTypeOnItemSelectedListener(Activity activity) {
        mActivity = activity;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.d("OnItemSelected(Type)", "Selected " + pos);
        //final RelativeLayout extraFieldRL = (RelativeLayout) mActivity.findViewById(R.id.Fragment_Banks_RelativeLayout_Extra_Field);
        final TextView messageTypeTV = (TextView) mActivity.findViewById(R.id.Fragment_Sms_TextView_MessageType);
        final TextInputLayout messageTIL = (TextInputLayout) mActivity.findViewById(R.id.Fragment_Sms_TextInputLayout_Message);
        final EditText messageET = (EditText) mActivity.findViewById(R.id.Fragment_Sms_EditText_Message);

        messageTypeTV.setText(parent.getSelectedItem().toString());
        messageTypeTV.setTextColor(ContextCompat.getColor(mActivity, R.color.app_main_text_color));

        if (pos == 0) {
            messageTIL.setHint(mActivity.getString(R.string.hint_message_sms));
            messageTIL.setEnabled(true);
            messageET.setEnabled(true);
        } else if (pos == 1) {
            messageTIL.setHint(mActivity.getString(R.string.hint_message_mms));
            messageTIL.setEnabled(false);
            messageET.setEnabled(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

}
