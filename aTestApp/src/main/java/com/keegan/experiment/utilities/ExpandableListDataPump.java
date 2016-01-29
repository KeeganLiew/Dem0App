package com.keegan.experiment.utilities;

import com.keegan.experiment.GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by keegan on 29/01/16.
 */
public class ExpandableListDataPump {
    private final String TAG = ExpandableListDataPump.class.getSimpleName();
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        /*List<String> toCreateList = new ArrayList<String>();
        toCreateList.add("About page");
        toCreateList.add("Actionbar titles");
        toCreateList.add("Change username on long press");
        toCreateList.add("Snackbar to undo display picture");
        toCreateList.add("Test Cases");
        toCreateList.add("GitHub");
        toCreateList.add("HTTP requests");
        toCreateList.add("API Limitation List (Scrolling, FloatingButton < 21)");

        List<String> toImprove = new ArrayList<String>();
        toImprove.add("Widget Sizes");
        toImprove.add("Widget Display Picture");
        toImprove.add("Widget Open app on tap, except refresh button");
        toImprove.add("Clean up XML-Layouts, colors, strings etc");
        toImprove.add("Clean up Java");
        toImprove.add("Put received SMS in a list (instead of toast)");

        List<String> toFix = new ArrayList<String>();
        toFix.add("Widget only updates latest one");
        toFix.add("Keyboard to show after logout");
        toFix.add("Hide float button in fragment");

        List<String> doneWeek1 = new ArrayList<String>();
        doneWeek1.add("Toolbar (new Navigation Drawer)");
        doneWeek1.add("CollapsingToolbarLayout");
        doneWeek1.add("FloatingAction Menu + Button");
        doneWeek1.add("CoordinatedLayout");
        doneWeek1.add("Upload and crop from Gallery");
        doneWeek1.add("Widget");
        doneWeek1.add("SMS service");

        List<String> doneWeek2 = new ArrayList<String>();
        doneWeek2.add("Double press back to exit");
        doneWeek2.add("Log out");
        doneWeek2.add("Login Page");
        doneWeek2.add("Login Navigation Drawer");
        doneWeek2.add("DeviceInfo Page");
        doneWeek2.add("Get Name and Ph.Number from Contacts");
        doneWeek2.add("SMS fragment");
        doneWeek2.add("ActionBar(Toolbar) only scrolls on HomePage");*/

        expandableListDetail.put("To Improve List", GlobalVariables.toImproveList);
        expandableListDetail.put("To Fix List", GlobalVariables.toFixList);
        expandableListDetail.put("To Create List", GlobalVariables.toCreateList);
        expandableListDetail.put("Done List", GlobalVariables.doneList);
        return expandableListDetail;
    }
}