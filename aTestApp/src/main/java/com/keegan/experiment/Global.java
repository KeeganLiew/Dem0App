package com.keegan.experiment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.ImageView;

import com.keegan.experiment.utilities.DisplayPictureUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keegan on 22/01/16.
 */
public class Global {

    public enum DeviceIdType {
        undigestedDeviceId, digestedDeviceId
    }

    public static final String KEEGAN_LINKEDIN_URL = "http://www.linkedin.com/in/keeganliew";
    public static final String EMPTY_STRING = "";
    public static final int hide_keyboard_login_drawer_percentage = 50;
    public static final int CONTACT_PICKER_RESULT = 1001;
    public static final int GALLERY_ACTIVITY_CODE = 2002;
    public static final int RESULT_CROP = 3003;
    public static final int SMS_TEXT_LIMIT = 160;
    public static final String profileImageDirectoryName = "imageDir";
    public static final Integer[] numpadKeysData = {R.id.custom_numeric_keyboard_key_0,
            R.id.custom_numeric_keyboard_key_1, R.id.custom_numeric_keyboard_key_2, R.id.custom_numeric_keyboard_key_3,
            R.id.custom_numeric_keyboard_key_4, R.id.custom_numeric_keyboard_key_5, R.id.custom_numeric_keyboard_key_6,
            R.id.custom_numeric_keyboard_key_7, R.id.custom_numeric_keyboard_key_8, R.id.custom_numeric_keyboard_key_9,
            R.id.custom_numeric_keyboard_key_menu, R.id.custom_numeric_keyboard_key_backspace
    };

    public static List<String> toCreateList = new ArrayList<String>();
    public static List<String> toImproveList = new ArrayList<String>();
    public static List<String> toFixList = new ArrayList<String>();
    public static List<String> doneList = new ArrayList<String>();
    public static List<List<String>> toDoList = new ArrayList<List<String>>();

    public static void componentChecker(Activity mActivity, String TAG, ComponentName componentToCheck) {
        int status = mActivity.getPackageManager().getComponentEnabledSetting(componentToCheck);
        if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            Log.d(TAG, componentToCheck.getShortClassName() + " is enabled");
        } else if (status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            Log.d(TAG, componentToCheck.getShortClassName() + " is disabled");
        }
    }

    public static void loadImage(Context mContext, ImageView pictureIV) {
        File profileImageDirectory = mContext.getDir(profileImageDirectoryName, Context.MODE_PRIVATE);
        DisplayPictureUtil.loadImageFromStorage(pictureIV, profileImageDirectory.getPath());
    }
}
