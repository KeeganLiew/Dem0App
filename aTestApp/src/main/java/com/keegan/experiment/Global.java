package com.keegan.experiment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.keegan.experiment.utilities.DisplayPictureUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by keegan on 22/01/16.
 */
public class Global {
    private static final String TAG = Global.class.getSimpleName();

    //file names
    public static final String prevProfileImgName = "prev_profile.jpg";
    public static final String profilePicImgName = "profile.jpg";
    public static final String profileBgPicImgName = "profile_background.jpg";
    public static final String profileImgDirName = "imageDir";
    //shared preference key names
    public static final String sharedPref_Username = "Username";
    public static final String sharedPref_Pin = "Pin";
    public static final String sharedPref_Password = "Password";
    public static final String sharedPref_AuthOption = "AuthOption";
    public static final String sharedPref_ShowAuthOptions = "ShowAuthOptions";
    public static final String sharedPref_SmsReceiverToggle = "SmsReceiverToggle";
    public static final String sharedPref_GestureVisibleToggle = "GestureVisibleToggle";
    public static final String sharedPref_LoginFirstRun = "LoginFirstRun";
    //default values
    public static final String username_default = "Guest";
    public static final String pin_default = "1234";
    public static final String password_default = "password";
    public static final boolean showAuthOptions_default = true;
    public static final boolean gestureVisibleToggle_default = true;
    public static final boolean loginFirstRun = true;
    public static final LoginInputMethod authOption_default = LoginInputMethod.PIN_INPUT;
    //request codes
    public static final int CONTACT_PICKER_RESULT = 1001;
    public static final int GALLERY_ACTIVITY_CODE = 2002;
    public static final int RESULT_CROP = 3003;
    //value limits
    public static final int sms_text_limit = 160;
    public static final int pin_text_limit = 4;
    //times
    public static final double totalSplashScreenSeconds = 2.5;
    public static final double animationPropotion = 0.75;
    ////TODO: 23/02/16 Move following lines
    public static final int animationTime = (int) (totalSplashScreenSeconds * 1000 * animationPropotion);
    public static final int afterAnimationWaitTime = (int) (totalSplashScreenSeconds * 1000 * (1 - animationPropotion));
    public static final int actualTotalSplashScreenTime = animationTime + afterAnimationWaitTime;
    //others
    public static final String KEEGAN_LINKEDIN_URL = "http://www.linkedin.com/in/KeeganLiew";
    public static final String KEEGAN_GOOGLEPLAY_URL = "https://github.com/KeeganLiew";
    public static final String KEEGAN_GITHUB_URL = "https://github.com/KeeganLiew/Dem0App";
    public static final String EMPTY_STRING = "";
    public static final String ERROR_RETRIEVING = "-- Error retrieving --";
    public static final Boolean SMS_SWITCH_DEFAULT = false;
    public static final int hide_keyboard_login_drawer_percentage = 50;
    public static final int display_picture_crop_size = 280;
    public static final double min_gesture_score = 5.0;
    public static final SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM hh:mm a");

    //devlog list
    public static List<String> toCreateList = new ArrayList<String>();
    public static List<String> toImproveList = new ArrayList<String>();
    public static List<String> toFixList = new ArrayList<String>();
    public static List<String> doneList = new ArrayList<String>();
    public static List<List<String>> toDoList = new ArrayList<List<String>>();

    //custom numpad keys
    public static final Integer[] numpadKeysData = {R.id.custom_numeric_keyboard_key_0,
            R.id.custom_numeric_keyboard_key_1, R.id.custom_numeric_keyboard_key_2, R.id.custom_numeric_keyboard_key_3,
            R.id.custom_numeric_keyboard_key_4, R.id.custom_numeric_keyboard_key_5, R.id.custom_numeric_keyboard_key_6,
            R.id.custom_numeric_keyboard_key_7, R.id.custom_numeric_keyboard_key_8, R.id.custom_numeric_keyboard_key_9,
            R.id.custom_numeric_keyboard_key_menu, R.id.custom_numeric_keyboard_key_backspace
    };

    //global enums
    public enum DeviceIdType {
        undigestedDeviceId, digestedDeviceId
    }

    public enum LoginInputMethod {
        PIN_INPUT("PIN"),
        PASSWORD_INPUT("PASSWORD"),
        GESTURE_INPUT("GESTURE");

        private final String code;
        private static final Map<String, LoginInputMethod> valuesByCode;

        static {
            valuesByCode = new HashMap<String, LoginInputMethod>();
            for (LoginInputMethod vehicleType : LoginInputMethod.values()) {
                valuesByCode.put(vehicleType.code, vehicleType);
            }
        }

        LoginInputMethod(String code) {
            this.code = code;
        }

        public static LoginInputMethod lookupByCode(String code) {
            return valuesByCode.get(code);
        }

        public String getCode() {
            return code;
        }
    }

    //component methods
    public static void checkComponent(Activity mActivity, String TAG, Class className) {
        ComponentName componentToCheck = new ComponentName(mActivity, className);
        int status = mActivity.getPackageManager().getComponentEnabledSetting(componentToCheck);
        if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            Log.d(TAG, componentToCheck.getShortClassName() + " is enabled");
        } else if (status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            Log.d(TAG, componentToCheck.getShortClassName() + " is disabled");
        }
    }

    public static void setComponent(Activity mActivity, Class className, Boolean bool) {
        ComponentName smsReceiverComponent = new ComponentName(mActivity, className);
        if (bool) {
            mActivity.getPackageManager().setComponentEnabledSetting(smsReceiverComponent,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        } else {
            mActivity.getPackageManager().setComponentEnabledSetting(smsReceiverComponent,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }

    //images methods
    public static void loadImageIntoImageView(Context mContext, ImageView pictureIV, String fileName) {
        File profileImageDirectory = mContext.getDir(profileImgDirName, Context.MODE_PRIVATE);
        DisplayPictureUtil.loadImageFromStorage(pictureIV, profileImageDirectory.getPath(), fileName);
    }

    public static void deleteImage(Context mContext, String fileName) {
        File profileImageDirectory = mContext.getDir(profileImgDirName, Context.MODE_PRIVATE);
        DisplayPictureUtil.deleteImageFromStorage(profileImageDirectory.getPath(), fileName);
    }

    //shared preferences methods - overloading
    // string
    public static void savePreferences(Activity mActivity, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String loadSavedPreferences(Activity mActivity, String key, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        return sharedPreferences.getString(key, defaultValue);
    }

    //boolean
    public static void savePreferences(Activity mActivity, String key, Boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static Boolean loadSavedPreferences(Activity mActivity, String key, Boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    //check contains
    public static boolean checkContainsSharedPreferences(Activity mActivity, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        return sharedPreferences.contains(key);
    }

    //clear all
    public static void clearSharedPreferences(Activity mActivity) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    //toast methods
    public static void createAndShowToast(Activity mActivity, String toastMessage, int length) {
        Toast.makeText(mActivity, toastMessage, length).show();
    }

    public static void createAndShowToast(Context mContext, String toastMessage, int length) {
        Toast.makeText(mContext, toastMessage, length).show();
    }
}
