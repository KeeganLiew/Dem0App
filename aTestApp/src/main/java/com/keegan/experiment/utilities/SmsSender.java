package com.keegan.experiment.utilities;

import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.keegan.experiment.activities.MainActivity;

/**
 * Created by keegan on 27/01/16.
 */
public class SmsSender {
    private static final String TAG = SmsSender.class.getSimpleName();

    //---sends an SMS message to ANOTHER device---
    public static void sendSms(String phoneNumber, String message) {
        Log.d(TAG, "To: " + phoneNumber + ", Message: " + message);
        try {
            SmsManager smsManager = SmsManager.getDefault();
            if (message.length() > 159) {
                message = message.substring(0, 159);
            }
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(MainActivity.mContext, "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.mContext, "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static void sendSms_openDefault(String phoneNumber, String message) {
        Log.d(TAG, "To: " + phoneNumber + ", Message: " + message);

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", phoneNumber);
        smsIntent.putExtra("sms_body", message);

        try {
            MainActivity.mContext.startActivity(smsIntent);
            //MainActivity.mContext.finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.mContext,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }


}
