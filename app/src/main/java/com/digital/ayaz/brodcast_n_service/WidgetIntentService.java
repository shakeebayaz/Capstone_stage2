package com.digital.ayaz.brodcast_n_service;

import android.app.IntentService;
import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.digital.ayaz.MyAppWidgetProvider;
import com.digital.ayaz.Utils.Constants;
import com.digital.ayaz.Utils.Utility;
import com.digital.ayaz.app.MainApplication;
import com.digital.ayaz.storage.DataBase;
import com.digital.ayaz.storage.DatabaseSave;
import com.digital.ayaz.storage.TouristContract;

import org.json.JSONException;
import org.json.JSONObject;

public class WidgetIntentService extends IntentService implements LoaderManager.LoaderCallbacks<Cursor> {

    private String placename;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WidgetIntentService(String name) {
        super(name);
    }
    public WidgetIntentService(){
        super("WidgetIntentService");

    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Cursor cursor=  getContentResolver().query(TouristContract.HotelEntry.buildHotelsUri(),
                null, null, null, null);


        if (cursor.moveToFirst()) {
                getPlaceDetail(Utility.getPlaceApiUrlFromPLaceId(this, cursor.getString(1)));
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    public void getPlaceDetail(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject list = jsonObject.getJSONObject(Constants.KEY_RESULT);
                    placename = list.getString("name");
                    Intent intent=new Intent("android.appwidget.action.APPWIDGET_UPDATE");
                    intent.putExtra("savedHotel",placename);
                    WidgetIntentService.this.sendBroadcast(intent);

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
    }

}
