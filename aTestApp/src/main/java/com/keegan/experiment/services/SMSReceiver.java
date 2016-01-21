package com.keegan.experiment.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.keegan.experiment.R;
import com.keegan.experiment.GlobalVariables;
import com.keegan.experiment.utilities.DeviceInfo;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by keegan on 24/12/15.
 */
public class SMSReceiver extends BroadcastReceiver {

    // Get the object of SmsManager
    //final SmsManager sms = SmsManager.getDefault();
    Context mContext;
    final String TAG = getClass().getSimpleName().toString();

    public void onReceive(Context mContext, Intent intent) {
        this.mContext = mContext;
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                        String senderNum = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();

                        String s = "";
                        s += "Sender Number: " + senderNum;
                        s += "\n Message: " + message;
                        Log.d(TAG, "MessageInfo: " + s);

                        String deviceId = DeviceInfo.getDeviceId(GlobalVariables.DeviceIdType.undigestedDeviceId);
                        String countryZipCode = DeviceInfo.getCountryZipCode();
                        String shortInfo = "Device Name: " + Build.MANUFACTURER.toUpperCase() + " " + Build.MODEL +
                                "\n Country Code: " + countryZipCode +
                                "\n Device ID: " + deviceId;

                        final Toast toast = Toast.makeText(mContext,
                                "senderNum: " + senderNum + ", message: " + message + " \n PhoneInfo: " + shortInfo, Toast.LENGTH_LONG);
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
