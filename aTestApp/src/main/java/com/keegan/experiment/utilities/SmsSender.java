package com.keegan.experiment.utilities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.keegan.experiment.Global;
import com.keegan.experiment.R;
import com.keegan.experiment.activities.MainActivity;

/**
 * Created by keegan on 27/01/16.
 */
public class SmsSender {
    private static final String TAG = SmsSender.class.getSimpleName();

    //---sends an SMS message to ANOTHER device---
    public static void sendSms(String phoneNumber, String message) {
        Log.d(TAG, "To: " + phoneNumber + ", Message: " + message);
        Activity mActivity = MainActivity.getmActivity();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            if (message.length() >= Global.SMS_TEXT_LIMIT) {
                message = message.substring(0, Global.SMS_TEXT_LIMIT - 1);
                //Toast.makeText(MainActivity.getmActivity(), "Not sending full message", Toast.LENGTH_LONG).show();
                Global.createAndShowToast(mActivity, mActivity.getString(R.string.toast_not_sending_full_message), Toast.LENGTH_LONG);
            }
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            //Toast.makeText(MainActivity.getmActivity(), "SMS sent.", Toast.LENGTH_LONG).show();
            Global.createAndShowToast(mActivity, mActivity.getString(R.string.toast_sms_sent_success), Toast.LENGTH_LONG);
        } catch (Exception e) {
            //Toast.makeText(MainActivity.getmActivity(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            Global.createAndShowToast(mActivity, mActivity.getString(R.string.toast_sms_sent_fail), Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }

    public static void sendSms_openDefault(String phoneNumber, String message) {
        Log.d(TAG, "To: " + phoneNumber + ", Message: " + message);
        Activity mActivity = MainActivity.getmActivity();

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", phoneNumber);
        smsIntent.putExtra("sms_body", message);

        try {
            mActivity.startActivity(smsIntent);
            //MainActivity.getmContext().finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Global.createAndShowToast(mActivity, mActivity.getString(R.string.toast_sms_sent_fail), Toast.LENGTH_LONG);
            //Toast.makeText(MainActivity.getmActivity(), "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }


}
