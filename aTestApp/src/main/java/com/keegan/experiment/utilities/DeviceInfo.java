package com.keegan.experiment.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.keegan.experiment.Global;
import com.keegan.experiment.R;
import com.keegan.experiment.activities.MainActivity;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by keegan on 22/01/16.
 */
public class DeviceInfo {
    private static final String TAG = "DeviceInfo";

    public static String getHTMLFormatDeviceInfo() {
        String resultString = "";
        try {
            Activity mActivity = MainActivity.getmActivity();

            String deviceId = getDeviceId(Global.DeviceIdType.undigestedDeviceId);
            String deviceId2 = getDeviceId(Global.DeviceIdType.digestedDeviceId);
            String country = mActivity.getResources().getConfiguration().locale.getCountry();
            String countryZipCode = getCountryZipCode();

            String deviceInfoString = "";
            //deviceInfoString += "<u>Device infos</u>";

            deviceInfoString += "<u>Device ID</u><br/><i>" + deviceId + "</i><br/>";
            deviceInfoString += "<i>(" + deviceId2 + ")</i><br/>";

            deviceInfoString += "<u>Country Code</u><br/><i>" + country + "<br/>(" + countryZipCode + ")</i><br/>";

            deviceInfoString += "<u>Kernel Version</u><br/><i>" + System.getProperty("os.version") + "<br/>(" + Build.VERSION.INCREMENTAL + ")" + "</i><br/>";
            deviceInfoString += "<u>Android Version</u><br/><i>" + Build.VERSION.RELEASE + "</i><br/>";
            deviceInfoString += "<i>(API " + Build.VERSION.SDK_INT + ")</i><br/>";


            deviceInfoString += "<u>Brand</u><br/><i>" + Build.BRAND.toUpperCase() + "</i><br/>";
            deviceInfoString += "<u>Model</u><br/><i>" + Build.MODEL + "</i><br/>";
            deviceInfoString += "<u>Product & Device</u><br/><i>" + Build.PRODUCT + "</i><br/>";
            deviceInfoString += "<i>" + Build.DEVICE + "</i><br/>";

            deviceInfoString += "<u>Build number</u><br/><i>" + Build.DISPLAY + "</i><br/>";
            deviceInfoString += "<u>CPU_ABI</u><br/><i>" + Build.CPU_ABI + "</i><br/>";
            deviceInfoString += "<i>" + Build.CPU_ABI2 + "</i><br/>";
            //deviceInfoString += "<u>UNKNOWN</u><br/><i>" + Build.UNKNOWN + "</i><br/>";
            deviceInfoString += "<u>Hardware</u><br/><i>" + Build.HARDWARE + "</i><br/>";
            deviceInfoString += "<u>Build ID</u><br/><i>" + Build.ID + "</i><br/>";
            deviceInfoString += "<u>Manufacturer</u><br/><i>" + Build.MANUFACTURER.toUpperCase() + "</i><br/>";
            deviceInfoString += "<u>Serial</u><br/><i>" + Build.SERIAL + "</i><br/>";
            deviceInfoString += "<u>User</u><br/><i>" + Build.USER + "</i><br/>";
            deviceInfoString += "<u>Host</u><br/><i>" + Build.HOST + "</i><br/>";
            //Log.i(TAG + " | Device Info > ", s);
            resultString = deviceInfoString;
        } catch (Exception e) {
            Log.e(TAG, "Error getting Device INFO");
        }
        return resultString;
    }


    public static String getDeviceSuperInfo() {
        Log.d(TAG, "getDeviceSuperInfo()");
        String info = "";
        try {
            Activity mActivity = MainActivity.getmActivity();
            String deviceId = getDeviceId(Global.DeviceIdType.undigestedDeviceId);
            String deviceId2 = getDeviceId(Global.DeviceIdType.digestedDeviceId);

            String country = mActivity.getResources().getConfiguration().locale.getCountry();
            String countryZipCode = getCountryZipCode();

            String shortInfo = "Device Name " + Build.MANUFACTURER.toUpperCase() + " " + Build.MODEL +
                    "\n Country Code: " + countryZipCode +
                    "\n Device ID: " + deviceId;
            //Log.i(TAG + " | Short Info > ", shortInfo);

            String s = "Device infos";
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
            //Log.i(TAG + " | Device Info > ", s);
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
        Activity mActivity = MainActivity.getmActivity();

        String device_uuid = Secure.getString(mActivity.getContentResolver(), Secure.ANDROID_ID);
        if (idType == Global.DeviceIdType.undigestedDeviceId) {
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
        Activity mActivity = MainActivity.getmActivity();

        TelephonyManager manager = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = mActivity.getResources().getStringArray(R.array.CountryCodes);
        for (String aRl : rl) {
            String[] g = aRl.split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return "+" + CountryZipCode;
    }
}
