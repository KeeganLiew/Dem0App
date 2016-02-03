package com.keegan.experiment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keegan on 22/01/16.
 */
public class Global {

    public enum DeviceIdType {
        undigestedDeviceId, digestedDeviceId
    }

    public static final int hide_keyboard_login_drawer_percentage = 50;
    public static final int CONTACT_PICKER_RESULT = 1001;
    public static final int GALLERY_ACTIVITY_CODE = 2002;
    public static final int RESULT_CROP = 3003;
    public static final String EMPTY_STRING = "";
    public static final int SMS_TEXT_LIMIT = 160;
    public static final String profileImageDirectoryName = "imageDir";

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
}
