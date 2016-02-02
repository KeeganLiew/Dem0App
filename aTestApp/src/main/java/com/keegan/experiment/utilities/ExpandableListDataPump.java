package com.keegan.experiment.utilities;

import com.keegan.experiment.Global;

import java.util.HashMap;
import java.util.List;

/**
 * Created by keegan on 29/01/16.
 */
public class ExpandableListDataPump {
    private final String TAG = ExpandableListDataPump.class.getSimpleName();

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        expandableListDetail.put("To Improve List", Global.toImproveList);
        expandableListDetail.put("To Fix List", Global.toFixList);
        expandableListDetail.put("To Create List", Global.toCreateList);
        expandableListDetail.put("Done List", Global.doneList);
        return expandableListDetail;
    }
}