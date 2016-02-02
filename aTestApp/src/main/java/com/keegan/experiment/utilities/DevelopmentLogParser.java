package com.keegan.experiment.utilities;

import android.util.Log;

import com.keegan.experiment.Global;
import com.keegan.experiment.activities.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by keegan on 29/01/16.
 */
public class DevelopmentLogParser {
    private static final String TAG = DevelopmentLogParser.class.getSimpleName();

    public static void getDevelopmentLog() {
        BufferedReader reader = null;
        try {
            if (Global.toDoList.size() > 0) {
                Log.d(TAG, "Already loaded");
                return;
            }
            reader = new BufferedReader(new InputStreamReader(MainActivity.getmActivity().getAssets().open("TODO.txt"), "UTF-8"));

            String mLine = reader.readLine();
            String[] dividers = {"[ToCreate]", "[ToImprove]", "[ToFix]", "[Done]"};
            int index = 0;

            Global.toDoList.add(Global.toCreateList);
            Global.toDoList.add(Global.toImproveList);
            Global.toDoList.add(Global.toFixList);
            Global.toDoList.add(Global.doneList);

            while (mLine != null && !mLine.equals("")) {
                if (mLine.contains(dividers[index])) {

                    Log.d(TAG, "---- Found: " + mLine);
                    mLine = reader.readLine();
                    Log.d(TAG, "Next Line: " + mLine);

                    if (index + 1 == dividers.length) {
                        mLine = reader.readLine();
                        Log.d(TAG, "Now at Line: " + mLine);
                        int weekNumber = 1;
                        String weekName = "Week ";
                        String weekCode;
                        while (mLine != null && !mLine.equals("")) {
                            if (mLine.contains(weekName + (weekNumber + 1))) {
                                Log.d(TAG, "---- Found: " + weekName + (weekNumber + 1));
                                weekNumber++;
                                mLine = reader.readLine();
                                Log.d(TAG, "Now at Line: " + mLine);
                            }
                            if (weekNumber < 10) {
                                weekCode = "W0" + weekNumber;
                            } else {
                                weekCode = "W" + weekNumber;
                            }
                            Global.doneList.add(weekCode + "-" + mLine);
                            mLine = reader.readLine();
                            Log.d(TAG, "Next Line: " + mLine);
                        }
                    } else {
                        while (mLine != null && !mLine.equals("") && !mLine.contains(dividers[index + 1])) {
                            Global.toDoList.get(index).add(mLine);
                            mLine = reader.readLine();
                            Log.d(TAG, "Next Line: " + mLine);
                        }
                    }index++;
                    Log.d(TAG, "Inner while stop at: " + mLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
