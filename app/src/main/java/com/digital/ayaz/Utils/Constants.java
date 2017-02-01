package com.digital.ayaz.Utils;

public interface Constants {
    int SPLASH_TIME_OUT = 1000;
    int HOTEL_RADIUS=3500;
    String BASE_PLACE_API="https://maps.googleapis.com/maps/api/place/";
    String PLACE_API=BASE_PLACE_API+"details/json?placeid=";
    String PLACE_API_IMAGE_URL=BASE_PLACE_API+"photo?maxwidth=500&photoreference=";
    String PLACE_API_SMALL_IMAGE_URL=BASE_PLACE_API+"photo?maxwidth=250&photoreference=";
    String NEAR_BY_API = BASE_PLACE_API+"nearbysearch/json?";
    int TOURIST_SPOT_RADIUS = 20000;
    int RESTAURANT_RADIUS = 2500;
    int SHOPPING_RADIUS = 3000;
    int CAFETERIA_RADIUS =3000;

String KEY_RESULT = "result";
    String KEY_RESULTS = "results";
String KEY_PLACE_NAME = "name";
String JSON_KEY_LAT = "lat";
String JSON_KEY_LNG = "lng";
String KEY_VICINITY = "vicinity";
String JSON_KEY_PHOTOS = "photos";
}
