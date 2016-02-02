package com.keegan.experiment.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.keegan.experiment.Global;
import com.keegan.experiment.utilities.DeviceInfo;

/**
 * Created by keegan on 24/12/15.
 */
public class SmsReceiver extends BroadcastReceiver {

    private Context mContext;
    final String TAG = getClass().getSimpleName().toString();

    public void onReceive(Context mContext, Intent intent) {
        this.mContext = mContext;
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (Object aPdusObj : pdusObj) {

                        SmsMessage currentMessage;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                            currentMessage = msgs[0];
                        } else {
                            Object pdus[] = (Object[]) bundle.get("pdus");
                            currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                        }

                        String senderNum = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();

                        String s = "";
                        s += "Sender Number: " + senderNum;
                        s += "\n Message: " + message;
                        Log.d(TAG, "MessageInfo: " + s);

                        String deviceId = DeviceInfo.getDeviceId(Global.DeviceIdType.undigestedDeviceId);
                        String countryZipCode = DeviceInfo.getCountryZipCode();
                        String shortInfo = "Device Name: " + Build.MANUFACTURER.toUpperCase() + " " + Build.MODEL +
                                "\n Country Code: " + countryZipCode +
                                "\n Device ID: " + deviceId;

                        final Toast toast = Toast.makeText(mContext,
                                "senderNum: " + senderNum + ", message: " + message
                                //+ " \n PhoneInfo: " + shortInfo
                                , Toast.LENGTH_LONG);
                        toast.show();
                    } // end for loop
                } // bundle is null
            } catch (Exception e) {
                Log.e(TAG, "Exception smsReceiver" + e);
            }
            DeviceInfo.getDeviceSuperInfo();
        }
    }


}
