package com.digital.ayaz.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.digital.ayaz.R;
import com.digital.ayaz.Utils.Constants;
import com.digital.ayaz.app.MainApplication;
import com.digital.ayaz.custome_view.ScrollGoogleMap;
import com.digital.ayaz.databinding.SelectOptionLayoutBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class SelectOptionActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG_GEOMETRY = "geometry";
    private static final String TAG_LOCATION = "location";
    String placeid;
    Double lat, lng;
    private GoogleMap mGooleMap;
    private SelectOptionLayoutBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.select_option_layout);
        try {
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent().getExtras() != null) {
            placeid = getIntent().getStringExtra("placeid");
        }

        getPlaceDetail(Constants.PLACE_API.concat(placeid) + "&key=" + getResources().getString(R.string.google_key));
        mBinding.restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectOptionActivity.this, RestaurantListActivity.class);
                i.putExtra(Constants.JSON_KEY_LAT, lat);
                i.putExtra(Constants.JSON_KEY_LNG, lng);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

            }
        });

        mBinding.tourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectOptionActivity.this, TouristMainActivity.class);
                i.putExtra(Constants.JSON_KEY_LAT, lat);
                i.putExtra(Constants.JSON_KEY_LNG, lng);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });

        mBinding.hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectOptionActivity.this, HotelListActivity.class);
                i.putExtra(Constants.JSON_KEY_LAT, lat);
                i.putExtra(Constants.JSON_KEY_LNG, lng);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });
        mBinding.shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectOptionActivity.this, ShoppingActivity.class);
                i.putExtra(Constants.JSON_KEY_LAT, lat);
                i.putExtra(Constants.JSON_KEY_LNG, lng);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });

    }

    private void initilizeMap() {
        if (mGooleMap == null) {
            ScrollGoogleMap scrollGoogleMap = ((ScrollGoogleMap) getFragmentManager().findFragmentById(
                    R.id.map));
            scrollGoogleMap.getMapAsync(this);
            scrollGoogleMap.setListener(new ScrollGoogleMap.OnTouchListener() {
                @Override
                public void onTouch() {
                    mBinding.listLayout.requestDisallowInterceptTouchEvent(true);
                }
            });
        }
    }

    public void getPlaceDetail(String url) {
        JsonObjectRequest movieReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject result = jsonObject.getJSONObject(Constants.KEY_RESULT);
                    JSONObject geometry = result.getJSONObject(TAG_GEOMETRY);
                    JSONObject location = geometry.getJSONObject(TAG_LOCATION);
                    lat = Double.parseDouble(location.optString(Constants.JSON_KEY_LAT));
                    lng = Double.parseDouble(location.optString(Constants.JSON_KEY_LNG));
                    mGooleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        MainApplication.getInstance().addToAPIRequestQueue(movieReq);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGooleMap = googleMap;
    }

    @Override
    public void onResume() {
        setInternetConnectionListner(mBinding.interMsg);
        super.onResume();
        initilizeMap();

    }
}
