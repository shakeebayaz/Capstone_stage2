package com.digital.ayaz.storage;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by Shakeeb on 7/10/2015.
 */
public class TouristContract {

    public static final String CONTENT_AUTHORITY = "com.digital.ayaz";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PLACES = "places";
    public static final String PATH_HOTEL = "hotels";
    public static final String PATH_RESTAURANT = "restaurants";

    public static long normalizeDate(long startDate) {
        Time time = new Time();
        time.set(startDate);
        int jul_day = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(jul_day);
    }

    public static final class PlaceEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACES;

        // Table name
        public static final String TABLE_NAME = "places";
        public static final String KEY_ID = "id";
        public static final String KEY_PLACE_ID = "placeid";

        public static Uri buildPlaceUri() {
            /**
             * "content://com.popularmovies.app/movie"
             */
            return CONTENT_URI;
        }

        public static Uri buildPlaceUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class HotelEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HOTEL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HOTEL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HOTEL;

        // Table name
        public static final String TABLE_NAME = "hotels";

        public static final String KEY_ID = "id";
        public static final String KEY_HOTELS_ID = "hotelid";

        public static Uri buildHotelsUri() {
            /**
             * "content://com.popularmovies.app/movie"
             */
            return CONTENT_URI;
        }

        public static Uri buildHotelUri(long hotelId) {
            /**
             * "content://com.popularmovies.app/trailers/135397"
             *  movieId = 135397
             */

            return ContentUris.withAppendedId(CONTENT_URI, hotelId);
        }

        public static String getHotelIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);

        }

    }

    public static final class RestaurantEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESTAURANT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESTAURANT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESTAURANT;

        // Table name
        public static final String TABLE_NAME = "restaurants";
        public static final String KEY_ID = "id";
        public static final String KEY_RESTAURANTS_ID = "restaurantid";

        public static Uri buildRestaurantUri() {
            /**
             * "content://com.popularmovies.app/movie"
             */
            return CONTENT_URI;
        }

        public static Uri buildRestautantUri(long movieId) {
            return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }

        public static String getRestautantIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}