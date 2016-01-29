package com.keegan.experiment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keegan on 22/01/16.
 */
public class GlobalVariables {

    public enum DeviceIdType {
        undigestedDeviceId, digestedDeviceId
    }

    public static int hide_keyboard_login_drawer_percentage = 50;
    public static final int CONTACT_PICKER_RESULT = 1001;
    public static final int GALLERY_ACTIVITY_CODE = 2002;
    public static final int RESULT_CROP = 3003;
    public static List<String> toCreateList = new ArrayList<String>();
    public static List<String> toImproveList = new ArrayList<String>();
    public static List<String> toFixList = new ArrayList<String>();
    public static List<String> doneList = new ArrayList<String>();
    public static List<List<String>> toDoList = new ArrayList<List<String>>();

}
