package com.digital.ayaz.storage;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class TouristProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int RESTAURANT_EXIST = 10;
    private static final int PLACE_EXIST = 20;
    private static final int HOTEL_EXIST = 30;
    private DataBase mOpenHelper;
    // /movie
    private  static final int PLACES = 100;
    private  static final int HOTELS = 200;

    private static final int RESTAURANT = 300;


    private static final String[] HOTEL_COLUMNS = {

            TouristContract.HotelEntry.KEY_ID, TouristContract.HotelEntry.KEY_HOTELS_ID

    };
    private static final String[] PLACE_COLUMNS = {

            TouristContract.PlaceEntry.KEY_ID, TouristContract.PlaceEntry.KEY_PLACE_ID

    };
    private static final String[] RESTAURANT_COLUMNS = {

            TouristContract.RestaurantEntry.KEY_ID, TouristContract.RestaurantEntry.KEY_RESTAURANTS_ID

    };

    public static final String sPlaceIdSelection =
            TouristContract.PlaceEntry.TABLE_NAME +
                    "." + TouristContract.PlaceEntry.KEY_PLACE_ID + " = ? ";

    public static final String sHotelIdSelection =
            TouristContract.HotelEntry.TABLE_NAME +
                    "." + TouristContract.HotelEntry.KEY_HOTELS_ID + " = ? ";

    public static final String sRestaurantIdSelection =
            TouristContract.RestaurantEntry.TABLE_NAME +
                    "." + TouristContract.RestaurantEntry.KEY_RESTAURANTS_ID + " = ? ";


    public static final String[] EXIST_COLUMNS = {

            TouristContract.HotelEntry.KEY_ID

    };


    public static UriMatcher buildUriMatcher() {
        final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TouristContract.CONTENT_AUTHORITY;
        sURIMatcher.addURI(authority, TouristContract.PATH_PLACES, PLACES);
        sURIMatcher.addURI(authority, TouristContract.PATH_PLACES + "/#", PLACES);

        sURIMatcher.addURI(authority, TouristContract.PATH_HOTEL, HOTELS);
        sURIMatcher.addURI(authority, TouristContract.PATH_HOTEL + "/#", HOTELS);

        sURIMatcher.addURI(authority, TouristContract.PATH_RESTAURANT, RESTAURANT);
        sURIMatcher.addURI(authority, TouristContract.PATH_RESTAURANT + "/#", RESTAURANT);
        return sURIMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DataBase(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PLACES:
                return TouristContract.PlaceEntry.CONTENT_TYPE;
            case PLACE_EXIST:
                return TouristContract.PlaceEntry.CONTENT_ITEM_TYPE;
            case HOTELS:
                return TouristContract.HotelEntry.CONTENT_TYPE;
            case HOTEL_EXIST:
                return TouristContract.HotelEntry.CONTENT_ITEM_TYPE;
            case RESTAURANT:
                return TouristContract.RestaurantEntry.CONTENT_TYPE;
            case RESTAURANT_EXIST:
                return TouristContract.RestaurantEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case PLACES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TouristContract.PlaceEntry.TABLE_NAME,
                        PLACE_COLUMNS,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PLACE_EXIST:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TouristContract.PlaceEntry.TABLE_NAME,
                        EXIST_COLUMNS,
                        sPlaceIdSelection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;


            case HOTELS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TouristContract.HotelEntry.TABLE_NAME,
                        HOTEL_COLUMNS,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case HOTEL_EXIST:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TouristContract.HotelEntry.TABLE_NAME,
                        EXIST_COLUMNS,
                        sHotelIdSelection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            case RESTAURANT:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TouristContract.RestaurantEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case RESTAURANT_EXIST:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TouristContract.RestaurantEntry.TABLE_NAME,
                        EXIST_COLUMNS,
                        sRestaurantIdSelection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        switch (match) {
            case PLACES:
                try {
                    long _id = db.insert(TouristContract.PlaceEntry.TABLE_NAME, null, values);
                    if (_id > 0)
                        returnUri = TouristContract.PlaceEntry.buildPlaceUri(_id);
                } catch (Exception e) {
                    Log.e("Test",e.toString());
                }

                break;

            case HOTELS: {
                try {
                    long _id = db.insert(TouristContract.HotelEntry.TABLE_NAME, null, values);
                    if (_id > 0)
                        returnUri = TouristContract.HotelEntry.buildHotelUri(_id);
                }catch (Exception e) {
                    Log.e("Test",e.toString());
                }
                break;
            }
            case RESTAURANT: {
                try {
                    long _id = db.insert(TouristContract.RestaurantEntry.TABLE_NAME, null, values);
                    if (_id > 0)
                        returnUri = TouristContract.RestaurantEntry.buildRestautantUri(_id);
                }  catch (Exception e) {
                    Log.e("Test",e.toString());
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
