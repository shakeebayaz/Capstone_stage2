package com.digital.ayaz.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.digital.ayaz.Utils.Constants;
import com.digital.ayaz.Utils.Utility;
import com.digital.ayaz.databinding.PlaceDetailLayoutBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.digital.ayaz.R;
import com.digital.ayaz.storage.DatabaseSave;
import com.digital.ayaz.app.MainApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceDetailActivity extends BaseActivity implements OnMapReadyCallback {

    private static final int CALLING_PERMISSION = 999;
    String phone_number, place_website;


    private static final String TAG_OPENING_HOURS = "opening_hours";
    private static final String TAG_FORMATTED_ADDRESS = "formatted_address";
    private static final String TAG_PHONE_NUMBER = "formatted_phone_number";
    private static final String TAG_TIMETABLE = "weekday_text";

    private static final String TAG_PHOTOS_REFERENCE = "photo_reference";
    private static final String TAG_TOTAL_RATING = "user_ratings_total";
    private static final String TAG_WEBSITE = "website";
    private static final String TAG_GEOMETRY = "geometry";
    private static final String TAG_LOCATION = "location";


    DatabaseSave db;
    SupportMapFragment fm;
    GoogleMap mGoogleMap;
    double lat, lng;
    int choice;
    ArrayList<String> photos_references = new ArrayList<>();
    private PlaceDetailLayoutBinding mBinding;
    private String place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.place_detail_layout);
        db = new DatabaseSave(this);
        mBinding.articleToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBinding.collapsingToolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);
        place_id = getIntent().getStringExtra("place_id");
        choice = getIntent().getIntExtra("choice", 0);
        getPlaceDetail(Utility.getPlaceApiUrlFromPLaceId(this, place_id));

        mBinding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone_number != null) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone_number));
                    if ((ActivityCompat.checkSelfPermission(PlaceDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALLING_PERMISSION);
                        }
                    } else {
                        startActivity(callIntent);
                    }
                } else {
                    showSnack(PlaceDetailActivity.this, R.string.error_phone_no_available);
                }


            }
        });

        mBinding.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (place_website != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(mBinding.website + ""));
                    startActivity(intent);
                } else showSnack(PlaceDetailActivity.this, R.string.error_no_webpage);
            }
        });

        mBinding.reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlaceDetailActivity.this, ReviewActivity.class);
                i.putExtra("place_id", place_id);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

            }
        });

        mBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (choice) {
                    case 1:

                        if (!db.getPlaces(place_id)) {
                            mBinding.saveImage.setImageResource(R.drawable.favourite_icon_red);
                            mBinding.saveText.setText("SAVED");
                            mBinding.saveText.setTextColor(Color.RED);
                            db.addPlaces(place_id);
                        } else {
                            showSnack(PlaceDetailActivity.this, R.string.already_saved);
                        }
                        break;

                    case 2:

                        if (!db.gethotel(place_id)) {
                            mBinding.saveImage.setImageResource(R.drawable.favourite_icon_red);
                            mBinding.saveText.setText("SAVED");
                            mBinding.saveText.setTextColor(Color.RED);
                            db.addHotels(place_id);
                        } else {
                            showSnack(PlaceDetailActivity.this, R.string.already_saved);
                        }
                        break;

                    case 3:

                        if (!db.getRes(place_id)) {
                            mBinding.saveImage.setImageResource(R.drawable.favourite_icon_red);
                            mBinding.saveText.setText("SAVED");
                            mBinding.saveText.setTextColor(Color.RED);
                            db.addRestaurants(place_id);
                        } else {
                            showSnack(PlaceDetailActivity.this, R.string.already_saved);
                        }
                        break;

                    case 4:
                        if (!db.getPlaces(place_id)) {
                            mBinding.saveImage.setImageResource(R.drawable.favourite_icon_red);
                            mBinding.saveText.setText("SAVED");
                            mBinding.saveText.setTextColor(Color.RED);
                            db.addPlaces(place_id);
                        } else {

                            showSnack(PlaceDetailActivity.this, R.string.already_saved);
                        }
                        break;
                }
            }
        });
    }

    public void getPlaceDetail(String url) {
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    JSONObject list = jsonObject.getJSONObject(Constants.KEY_RESULT);

                    if (list.has(Constants.JSON_KEY_PHOTOS)) {
                        JSONArray photos = list.optJSONArray(Constants.JSON_KEY_PHOTOS);

                        for (int j = 0; j < photos.length(); j++) {
                            JSONObject photo = photos.optJSONObject(j);

                            photos_references.add(photo.optString(TAG_PHOTOS_REFERENCE));
                        }
                        Glide.with(PlaceDetailActivity.this).load(Constants.PLACE_API_IMAGE_URL + photos_references.get(0) +"&key="+ getString(R.string.google_key)).override(300, 300).centerCrop().into(mBinding.imagePosterFull);
                        mBinding.imagePosterFull.setAlpha(0.6f);

                    } else {
                        mBinding.photoInclude.photoGalaryCard.setVisibility(View.GONE);
                    }

                    mBinding.placeName.setText(list.optString(Constants.KEY_PLACE_NAME));
                    mBinding.placeVicinity.setText(list.optString(Constants.KEY_VICINITY));
                    mBinding.addressInclude.address.setText(list.optString(TAG_FORMATTED_ADDRESS));

                    JSONObject geometry = list.optJSONObject(TAG_GEOMETRY);
                    JSONObject location = geometry.optJSONObject(TAG_LOCATION);

                    lat = Double.parseDouble(location.optString(Constants.JSON_KEY_LAT));
                    lng = Double.parseDouble(location.optString(Constants.JSON_KEY_LNG));

                    double rating = list.optDouble(TAG_TOTAL_RATING);
                    if (rating > 0) {
                        mBinding.rating.setRating((float) rating);
                        mBinding.rating.setVisibility(View.VISIBLE);
                        mBinding.userRatingText.setText("User Rating");
                    }
                    if (list.has(TAG_OPENING_HOURS)) {
                        JSONObject timing = list.optJSONObject(TAG_OPENING_HOURS);
                        JSONArray times = timing.optJSONArray(TAG_TIMETABLE);
                        TextView textView;
                        for (int j = 0; j < times.length(); j++) {
                            String time = (String) times.get(j);
                            textView = new TextView(PlaceDetailActivity.this);
                            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            textView.setLayoutParams(params);
                            textView.setText(time);
                            textView.setTextColor(ContextCompat.getColor(PlaceDetailActivity.this, R.color.black_overlay));
                            mBinding.timetableInclude.timetable.addView(textView);
                        }
                    } else {
                        mBinding.timetableInclude.timingCard.setVisibility(View.GONE);
                    }

                    place_website = list.optString(TAG_WEBSITE);
                    phone_number = list.optString(TAG_PHONE_NUMBER);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setMap(lat, lng);
                setPhotos();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        MainApplication.getInstance().addToAPIRequestQueue(request);
    }

    public void setMap(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    public void setPhotos() {

        ImageView img;

        for (int i = 0; i < photos_references.size(); i++) {
            img = new ImageView(PlaceDetailActivity.this);
            img.setPadding(5, 0, 5, 0);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(400, 400);
            img.setLayoutParams(params);
            Glide.with(PlaceDetailActivity.this).load(Constants.PLACE_API_IMAGE_URL + photos_references.get(i) + "&key=" + getString(R.string.google_key)).fitCenter().into(img);
            mBinding.photoInclude.photoLayout.addView(img);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
