package com.keegan.experiment.utilities;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.keegan.experiment.GlobalVariables;
import com.keegan.experiment.R;
import com.keegan.experiment.activities.MainActivity;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by keegan on 22/01/16.
 */
public class DeviceInfo {
    private static final String TAG = "DeviceInfo";


    public static String getDeviceSuperInfo() {
        Log.d(TAG, "getDeviceSuperInfo()");
        String info = "";
        try {
            Context mContext = MainActivity.getAppContext();
            String deviceId = getDeviceId(GlobalVariables.DeviceIdType.undigestedDeviceId);
            String deviceId2 = getDeviceId(GlobalVariables.DeviceIdType.digestedDeviceId);

            String country = mContext.getResources().getConfiguration().locale.getCountry();
            String countryZipCode = getCountryZipCode();

            String shortInfo = "Device Name: " + Build.MANUFACTURER.toUpperCase() + " " + Build.MODEL +
                    "\n Country Code: " + countryZipCode +
                    "\n Device ID: " + deviceId;
            Log.i(TAG + " | Short Info > ", shortInfo);

            String s = "Device infos:";
            s += "\n Device ID: " + deviceId;
            s += "\n Device ID(digested): " + deviceId2;
            s += "\n Country Code(alphabet): " + country;
            s += "\n Country Code(number): " + countryZipCode;

            s += "\n OS Version: " + System.getProperty("os.version") + "(" + Build.VERSION.INCREMENTAL + ")";
            s += "\n OS API Level: " + Build.VERSION.SDK_INT;
            s += "\n Device: " + Build.DEVICE;
            s += "\n Model (and Product): " + Build.MODEL + " (" + Build.PRODUCT + ")";
            s += "\n RELEASE: " + Build.VERSION.RELEASE;
            s += "\n BRAND: " + Build.BRAND;
            s += "\n DISPLAY: " + Build.DISPLAY;
            s += "\n CPU_ABI: " + Build.CPU_ABI;
            s += "\n CPU_ABI2: " + Build.CPU_ABI2;
            s += "\n UNKNOWN: " + Build.UNKNOWN;
            s += "\n HARDWARE: " + Build.HARDWARE;
            s += "\n Build ID: " + Build.ID;
            s += "\n MANUFACTURER: " + Build.MANUFACTURER;
            s += "\n SERIAL: " + Build.SERIAL;
            s += "\n USER: " + Build.USER;
            s += "\n HOST: " + Build.HOST;
            Log.i(TAG + " | Device Info > ", s);
            info = s;
        } catch (Exception e) {
            Log.e(TAG, "Error getting Device INFO");
        }
        return info;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        manufacturer = manufacturer.substring(0, 1).toUpperCase() + manufacturer.substring(1);
        String deviceName = manufacturer + " " + Build.MODEL;
        return deviceName;
    }

    public static String getDeviceId(Enum idType) {
        Context mContext = MainActivity.getAppContext();

        String device_uuid = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (idType == GlobalVariables.DeviceIdType.undigestedDeviceId) {
            return device_uuid;
        } else {
            if (device_uuid == null) {
                device_uuid = "12356789"; // for emulator testing
            } else {
                try {
                    byte[] _data = device_uuid.getBytes();
                    MessageDigest _digest = java.security.MessageDigest.getInstance("MD5");
                    _digest.update(_data);
                    _data = _digest.digest();
                    BigInteger _bi = new BigInteger(_data).abs();
                    device_uuid = _bi.toString(36);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return device_uuid;
        }
    }

    public static String getCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";
        Context mContext = MainActivity.getAppContext();

        TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = mContext.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return "+" + CountryZipCode;
    }
}
