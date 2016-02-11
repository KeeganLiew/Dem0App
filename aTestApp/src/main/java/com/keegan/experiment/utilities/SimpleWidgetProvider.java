package com.keegan.experiment.utilities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.keegan.experiment.Global;
import com.keegan.experiment.R;
import com.keegan.experiment.activities.LoginActivity;

import java.io.File;

public class SimpleWidgetProvider extends AppWidgetProvider {
    private static final String TAG = SimpleWidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context mContext, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "Widget onUpdate()");
        for (int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            Log.d(TAG, "updating widget " + i + ", id: " + appWidgetId);
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_user);

            //Start progress bar
            remoteViews.setViewVisibility(R.id.Widget_User_ImageView_RefreshIcon, View.GONE);
            remoteViews.setViewVisibility(R.id.Widget_User_ProgressBar_Refresh, View.VISIBLE);

            //Update username
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String name = sharedPreferences.getString("Username", mContext.getString(R.string.new_user));
            String widgetString = "Hello " + name;
            remoteViews.setTextViewText(R.id.Widget_User_TextView_Username, widgetString);
            //Update display picture
            File profileImageDirectory = mContext.getDir(Global.profileImagesDirectoryName, Context.MODE_PRIVATE);
            Bitmap displayPictureBitmap = DisplayPictureUtil.getDisplayPictureFromStorage(profileImageDirectory.getPath(), Global.profilePictureImageName);
            //remoteViews.setImageViewBitmap(R.id.Widget_User_ImageView_DisplayPic, displayPictureBitmap); //doesn't work
            if (displayPictureBitmap != null) {
                setBitmap(remoteViews, R.id.Widget_User_ImageView_DisplayPic, displayPictureBitmap); //workaround
            } else {
                Log.d(TAG, "No displayPicExist");
            }
            //Stop progress bar
            remoteViews.setViewVisibility(R.id.Widget_User_ImageView_RefreshIcon, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.Widget_User_ProgressBar_Refresh, View.GONE);

            //Refresh intent
            Intent configIntent = new Intent(mContext, SimpleWidgetProvider.class);
            configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingBroadcastIntent = PendingIntent.getBroadcast(mContext, appWidgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Activity Home intent
            //Intent startAppIntent = new Intent(mContext, MainActivity.class);
            //PendingIntent pendingActivityIntent = PendingIntent.getActivity(mContext, 0, startAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Activity Login intent
            Intent startLoginActIntent = new Intent(mContext, LoginActivity.class);
            PendingIntent pendingActivityLoginIntent = PendingIntent.getActivity(mContext, 0, startLoginActIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Set listener
            remoteViews.setOnClickPendingIntent(R.id.Widget_User_ImageView_RefreshIcon, pendingBroadcastIntent);
            remoteViews.setOnClickPendingIntent(R.id.Widget_User_TextView_Username, pendingActivityLoginIntent);
            remoteViews.setOnClickPendingIntent(R.id.Widget_User_ImageView_DisplayPic, pendingActivityLoginIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    //[Workaround] - http://stackoverflow.com/questions/14338091/setting-a-bitmap-using-remoteview-not-working
    private void setBitmap(RemoteViews views, int resId, Bitmap bitmap) {
        if (views != null) {
            Bitmap proxy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(proxy);
            c.drawBitmap(bitmap, new Matrix(), null);
            views.setImageViewBitmap(resId, proxy);
        }
    }
}
