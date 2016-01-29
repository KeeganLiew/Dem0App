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
public class DevelopmentLogParserCopy {
    private static final String TAG = DevelopmentLogParserCopy.class.getSimpleName();

    public static void getMultiCurrencyList() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(MainActivity.mContext.getAssets().open("TODO.txt"), "UTF-8"));

            String mLine = reader.readLine();
            //String groupItemLines;
            //while (mLine != null && !mLine.equals("")) {
            if (mLine != null && !mLine.equals("")) {
                if (mLine.contains("[ToCreate]")) {
                    Log.d(TAG, "Found ToCreate line: " + mLine);
                    mLine = reader.readLine();
                    while (mLine != null && !mLine.equals("")) {
                        if(mLine.contains("[ToImprove]")){
                            break;
                        }
                        GlobalVariables.toCreateList.add(mLine);
                        mLine = reader.readLine();
                    }
                }
                if (mLine.contains("[ToImprove]")) {
                    Log.d(TAG, "Found ToImprove line: " + mLine);
                    mLine = reader.readLine();
                    while (mLine != null && !mLine.equals("")) {
                        if(mLine.contains("[ToFix]")){
                            break;
                        }
                        GlobalVariables.toImproveList.add(mLine);
                        mLine = reader.readLine();
                    }
                }
                if (mLine.contains("[ToFix]")) {
                    Log.d(TAG, "Found ToFix line: " + mLine);
                    mLine = reader.readLine();
                    while (mLine != null && !mLine.equals("")) {
                        if(mLine.contains("[Done]")){
                            break;
                        }
                        GlobalVariables.toFixList.add(mLine);
                        mLine = reader.readLine();
                    }
                }
                //mLine = reader.readLine();
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
