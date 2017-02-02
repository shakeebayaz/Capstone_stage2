package com.digital.ayaz;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.digital.ayaz.Utils.Constants;
import com.digital.ayaz.app.MainApplication;
import com.digital.ayaz.brodcast_n_service.WidgetIntentService;

import org.json.JSONException;
import org.json.JSONObject;

public class MyAppWidgetProvider extends AppWidgetProvider {


    RemoteViews view;
    String placename;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, WidgetIntentService.class));
        appWidgetManager.updateAppWidget(appWidgetIds, view);
       super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")) {
            ComponentName thisWidget = new ComponentName(context,
                    MyAppWidgetProvider.class);
            AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(context);
            int[] mAppWidgetIds = mAppWidgetManager.getAppWidgetIds(thisWidget);
            view = new RemoteViews(context.getPackageName(), R.layout.appwidget);
            view.setTextViewText(R.id.place_name, intent.getStringExtra("savedHotel"));
            for (int widgetId : mAppWidgetIds) {
                mAppWidgetManager.updateAppWidget(widgetId, view);
           /* AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.place_name);*/

            }
        }
    }
}
