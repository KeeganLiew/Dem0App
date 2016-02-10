package com.keegan.experiment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.keegan.experiment.services.SmsReceiver;
import com.keegan.experiment.utilities.DisplayPictureUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keegan on 22/01/16.
 */
public class Global {
    public static final String KEEGAN_LINKEDIN_URL = "http://www.linkedin.com/in/keeganliew";
    public static final String EMPTY_STRING = "";
    public static final String profileImageDirectoryName = "imageDir";
    public static final String SharedPref_Username = "Username";
    public static final String SharedPref_SmsReceiverToggle = "SmsReceiverToggle";
    public static final Boolean SMS_SWITCH_DEFAULT = false;

    public static final int hide_keyboard_login_drawer_percentage = 50;
    public static final int CONTACT_PICKER_RESULT = 1001;
    public static final int GALLERY_ACTIVITY_CODE = 2002;
    public static final int RESULT_CROP = 3003;
    public static final int SMS_TEXT_LIMIT = 160;

    public static List<String> toCreateList = new ArrayList<String>();
    public static List<String> toImproveList = new ArrayList<String>();
    public static List<String> toFixList = new ArrayList<String>();
    public static List<String> doneList = new ArrayList<String>();
    public static List<List<String>> toDoList = new ArrayList<List<String>>();

    public enum DeviceIdType {
        undigestedDeviceId, digestedDeviceId
    }

    public static final Integer[] numpadKeysData = {R.id.custom_numeric_keyboard_key_0,
            R.id.custom_numeric_keyboard_key_1, R.id.custom_numeric_keyboard_key_2, R.id.custom_numeric_keyboard_key_3,
            R.id.custom_numeric_keyboard_key_4, R.id.custom_numeric_keyboard_key_5, R.id.custom_numeric_keyboard_key_6,
            R.id.custom_numeric_keyboard_key_7, R.id.custom_numeric_keyboard_key_8, R.id.custom_numeric_keyboard_key_9,
            R.id.custom_numeric_keyboard_key_menu, R.id.custom_numeric_keyboard_key_backspace
    };

    public static void checkComponent(Activity mActivity, String TAG, Class className) {
        ComponentName componentToCheck = new ComponentName(mActivity, className);
        int status = mActivity.getPackageManager().getComponentEnabledSetting(componentToCheck);
        if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            Log.d(TAG, componentToCheck.getShortClassName() + " is enabled");
        } else if (status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            Log.d(TAG, componentToCheck.getShortClassName() + " is disabled");
        }
    }

    public static void setComponent(Activity mActivity, Class className, Boolean bool){
        ComponentName smsReceiverComponent = new ComponentName(mActivity, className);
        if(bool) {
            mActivity.getPackageManager().setComponentEnabledSetting(smsReceiverComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        }else{
            mActivity.getPackageManager().setComponentEnabledSetting(smsReceiverComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }

    public static void loadImage(Context mContext, ImageView pictureIV) {
        File profileImageDirectory = mContext.getDir(profileImageDirectoryName, Context.MODE_PRIVATE);
        DisplayPictureUtil.loadImageFromStorage(pictureIV, profileImageDirectory.getPath());
    }

    public static void deleteImage(Context mContext) {
        File profileImageDirectory = mContext.getDir(profileImageDirectoryName, Context.MODE_PRIVATE);
        DisplayPictureUtil.deleteImageFromStorage(profileImageDirectory.getPath());
    }

    public static void savePreferences(Activity mActivity, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void savePreferences(Activity mActivity, String key, Boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String loadSavedPreferences(Activity mActivity, String key, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        return sharedPreferences.getString(key, defaultValue);
    }


    public static Boolean loadSavedPreferences(Activity mActivity, String key, Boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void clearSharedPreferences(Activity mActivity) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
