package com.keegan.experiment.utilities;

import android.util.Log;

import com.keegan.experiment.GlobalVariables;
import com.keegan.experiment.activities.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by keegan on 29/01/16.
 */
public class DevelopmentLogParser {
    private static final String TAG = DevelopmentLogParser.class.getSimpleName();

    public static void getMultiCurrencyList() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(MainActivity.mContext.getAssets().open("TODO.txt"), "UTF-8"));

            String mLine = reader.readLine();
            String[] dividers = {"[ToCreate]", "[ToImprove]", "[ToFix]", "[Done]"};
            int index = 0;

            GlobalVariables.toDoList.add(GlobalVariables.toCreateList);
            GlobalVariables.toDoList.add(GlobalVariables.toImproveList);
            GlobalVariables.toDoList.add(GlobalVariables.toFixList);
            GlobalVariables.toDoList.add(GlobalVariables.doneList);
            int i = 0;
            while (mLine != null && !mLine.equals("")) {
                if (mLine.contains(dividers[index])) {
                    index++;
                    Log.d(TAG, "Found: " + mLine);
                    mLine = reader.readLine();
                    Log.d(TAG, "Next Line: " + mLine);

                    if (index == dividers.length) {
                        mLine = reader.readLine();
                        Log.d(TAG, "Skip Week Next Line: " + mLine);
                        int weekNumber = 1;
                        String weekName = "Week ";
                        String weekCode;
                        while (mLine != null && !mLine.equals("")) {
                            Log.d(TAG, "Checking for: " + weekName + (weekNumber + 1));
                            if (mLine.contains(weekName + (weekNumber + 1))) {
                                weekNumber++;
                                mLine = reader.readLine();
                                Log.d(TAG, "Skip Week Next Line: " + mLine);
                            }
                            if (weekNumber < 10) {
                                weekCode = "W0" + weekNumber;
                            } else {
                                weekCode = "W" + weekNumber;
                            }
                            GlobalVariables.doneList.add(weekCode + "-" + mLine);
                            mLine = reader.readLine();
                            Log.d(TAG, "Next Line: " + mLine);
                        }
                        Log.d(TAG, "Inner while stop at: " + mLine);
                    } else {
                        //TODO fix list of list
                        while (mLine != null && !mLine.equals("") && !mLine.contains(dividers[index])) {
                            //GlobalVariables.toCreateList.add(mLine);
                            GlobalVariables.toDoList.get(i).add(mLine);
                            mLine = reader.readLine();
                            Log.d(TAG, "Next Line: " + mLine);
                        }
                        Log.d(TAG, "Inner while stop at: " + mLine);
                        i++; //try index later
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }
}
