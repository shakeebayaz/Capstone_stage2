package com.digital.ayaz.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSave extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "savedb";
    private static final String TABLE_PLACES = "places";
    private static final String TABLE_RESTAURANTS = "restaurants";
    private static final String TABLE_HOTELS = "hotels";
    private static final String KEY_ID = "id";
    private static final String KEY_PLACE_ID = "placeid";

    private static final String KEY_ID1 = "id";
    private static final String KEY_RESTAURANTS_ID = "restaurantid";

    private static final String KEY_ID2 = "id";
    private static final String KEY_HOTELS_ID = "hotelid";

    public DatabaseSave(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_PLACE = "CREATE TABLE " + TABLE_PLACES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLACE_ID + " TEXT unique" + ")";
        db.execSQL(CREATE_TABLE_PLACE);

        String CREATE_TABLE_HOTELS = "CREATE TABLE " + TABLE_HOTELS + "("
                + KEY_ID2 + " INTEGER PRIMARY KEY," + KEY_HOTELS_ID + " TEXT unique" + ")";
        db.execSQL(CREATE_TABLE_HOTELS);

        String CREATE_TABLE_RESTAURANT = "CREATE TABLE " + TABLE_RESTAURANTS + "("
                + KEY_ID1 + " INTEGER PRIMARY KEY," + KEY_RESTAURANTS_ID + " TEXT unique" + ")";
        db.execSQL(CREATE_TABLE_RESTAURANT);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTELS);

        // Create tables again
        onCreate(db);
    }

    //methods for marking it as favourties...............................................................
    public void addPlaces(String place_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLACE_ID, place_id);
        // Inserting Row
        db.insert(TABLE_PLACES, null, values);
        db.close(); // Closing database connection
    }

    public List<String> getAllPlaces() {
        List<String> List = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_PLACES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // Adding contact to list
                List.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return List;
    }

    public boolean getPlaces(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLACES, new String[]{KEY_ID,
                        KEY_PLACE_ID}, KEY_PLACE_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    public void addRestaurants(String restaurant_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RESTAURANTS_ID, restaurant_id);
        // Inserting Row
        db.insert(TABLE_RESTAURANTS, null, values);
        db.close(); // Closing database connection
    }

    public List<String> getAllRes() {
        List<String> List = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_RESTAURANTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                List.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return List;
    }

    public boolean getRes(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RESTAURANTS, new String[]{KEY_ID1,
                        KEY_RESTAURANTS_ID}, KEY_RESTAURANTS_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    public void addHotels(String hotel_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HOTELS_ID, hotel_id);
        // Inserting Row
        db.insert(TABLE_HOTELS, null, values);
        db.close();
    }

    public List<String> getAllHotels() {
        List<String> List = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_HOTELS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                List.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return List;
    }

    public boolean gethotel(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HOTELS, new String[]{KEY_ID2,
                        KEY_HOTELS_ID}, KEY_HOTELS_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    public Cursor getSavedHotels() {
        String selectQuery = "SELECT  * FROM " + TABLE_HOTELS;

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(selectQuery, null);
    }

}