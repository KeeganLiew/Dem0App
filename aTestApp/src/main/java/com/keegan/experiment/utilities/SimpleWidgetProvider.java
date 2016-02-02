package com.keegan.experiment.utilities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.keegan.experiment.R;
import com.keegan.experiment.activities.LoginActivity;

public class SimpleWidgetProvider extends AppWidgetProvider {
    private static final String TAG = SimpleWidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "Widget onUpdate()");
        for (int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            Log.d(TAG, "updating widget " + i + ", id: " + appWidgetId);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_user);


            remoteViews.setViewVisibility(R.id.Widget_User_ImageView_RefreshIcon, View.GONE);
            remoteViews.setViewVisibility(R.id.Widget_User_ProgressBar_Refresh, View.VISIBLE);

            //update process
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String name = sharedPreferences.getString("Username", context.getString(R.string.new_user));
            String widgetString = "Hello " + name;
            remoteViews.setTextViewText(R.id.Widget_User_TextView_Username, widgetString);

            remoteViews.setViewVisibility(R.id.Widget_User_ImageView_RefreshIcon, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.Widget_User_ProgressBar_Refresh, View.GONE);

            //configuration
            Intent configIntent = new Intent(context, SimpleWidgetProvider.class);
            configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setOnClickPendingIntent(R.id.Widget_User_ImageView_RefreshIcon, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
