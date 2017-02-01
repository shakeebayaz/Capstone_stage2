package com.digital.ayaz;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.digital.ayaz.Utils.Constants;
import com.digital.ayaz.Utils.Utility;
import com.digital.ayaz.activity.MainActivity;
import com.digital.ayaz.storage.DatabaseSave;
import com.digital.ayaz.app.MainApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class MyAppWidgetProvider extends AppWidgetProvider {


    RemoteViews view;
    String placename;
    String place_name;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        ComponentName thisWidget = new ComponentName(context,
                MyAppWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {

            view = new RemoteViews(context.getPackageName(), R.layout.appwidget);

            Cursor data = new DatabaseSave(context).getSavedHotels();
            if (data.moveToFirst()) {
                do {
                    place_name = getPlaceDetail(Utility.getPlaceApiUrlFromPLaceId(context,data.getString(1)));

                } while (data.moveToNext());
            }

            view.setTextViewText(R.id.place_name, place_name);

            Intent intent = new Intent(context, MainActivity.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.lay, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, view);

        }
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

    public String getPlaceDetail(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject list = jsonObject.getJSONObject(Constants.KEY_RESULT);
                    placename = list.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        MainApplication.getInstance().addToAPIRequestQueue(jsonObjectRequest);

        return placename;
    }
}
