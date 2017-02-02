package com.digital.ayaz.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public interface DataType {
        int place = 10;
        int hotel = 20;
        int restaurant = 30;
    }

    public static final String DATABASE_NAME = "guideMe.db";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE_PLACE = "CREATE TABLE " + TouristContract.PlaceEntry.TABLE_NAME + "("
                + TouristContract.PlaceEntry.KEY_ID + " INTEGER PRIMARY KEY," + TouristContract.PlaceEntry.KEY_PLACE_ID + " TEXT unique" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_PLACE);

        String CREATE_TABLE_HOTELS = "CREATE TABLE " + TouristContract.HotelEntry.TABLE_NAME + "("
                + TouristContract.HotelEntry.KEY_ID + " INTEGER PRIMARY KEY," + TouristContract.HotelEntry.KEY_HOTELS_ID + " TEXT unique" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_HOTELS);

        String CREATE_TABLE_RESTAURANT = "CREATE TABLE " + TouristContract.RestaurantEntry.TABLE_NAME + "("
                + TouristContract.RestaurantEntry.KEY_ID + " INTEGER PRIMARY KEY," + TouristContract.RestaurantEntry.KEY_RESTAURANTS_ID + " TEXT unique" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_RESTAURANT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public static ContentValues populateContentValue(int dataType, String placeId) {
        ContentValues values = new ContentValues();
        switch (dataType) {
            case DataType.place:
                values.put(TouristContract.PlaceEntry.KEY_PLACE_ID, placeId);
                break;
            case DataType.hotel:
                values.put(TouristContract.HotelEntry.KEY_HOTELS_ID, placeId);
                break;
            case DataType.restaurant:
                values.put(TouristContract.RestaurantEntry.KEY_RESTAURANTS_ID, placeId);
                break;
        }
        return values;
    }

}