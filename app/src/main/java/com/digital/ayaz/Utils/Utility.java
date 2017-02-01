package com.digital.ayaz.Utils;

import android.content.Context;

import com.digital.ayaz.R;

public class Utility {

    public static String getNearByHotels(Context context, double latitude, double longitude) {
        return getNearByPlaces(context.getResources().getString(R.string.google_key), Constants.HOTEL_RADIUS, "lodging", latitude, longitude);
    }

    public static String getNearByTouristSpot(Context context, double latitude, double longitude) {
        return getNearByPlaces(context.getResources().getString(R.string.google_key), Constants.TOURIST_SPOT_RADIUS, "night_club|aquarium|embassy|moving_company|mosque|night_club|church|amusement_park|zoo|park|hindu_temple|museum", latitude, longitude);

    }

    public static String getNearByRestourant(Context context, double latitude, double longitude) {
        return getNearByPlaces(context.getResources().getString(R.string.google_key), Constants.RESTAURANT_RADIUS, "restaurant", latitude, longitude);
    }

    public static String getNearByShoppingCorner(Context context, double latitude, double longitude) {
        return getNearByPlaces(context.getResources().getString(R.string.google_key), Constants.SHOPPING_RADIUS, "shopping_mall", latitude, longitude);
    }

    public static String getNearByCafeteria(Context context, double latitude, double longitude) {
        return getNearByPlaces(context.getResources().getString(R.string.google_key), Constants.CAFETERIA_RADIUS, "cafe", latitude, longitude);
    }

    public static String getNearByPlaces(String googleApiKey, int searchradius, String searchType, double latitude, double longitude) {
        StringBuilder sb = new StringBuilder(
                Constants.NEAR_BY_API);
        sb.append("location=" + latitude + "," + longitude);
        sb.append("&types=" + searchType);
        sb.append("&radius=" + searchradius);
        sb.append("&rankby=prominence");
        sb.append("&key=" + googleApiKey);
        return sb.toString();
    }
    public static String getPlaceApiUrlFromPLaceId(Context context,String placeId){
     return    Constants.PLACE_API+placeId+"&key="+context.getResources().getString(R.string.google_key);
    }

}


