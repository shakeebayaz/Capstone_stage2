package com.digital.ayaz.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.digital.ayaz.R;
import com.digital.ayaz.Utils.Constants;
import com.digital.ayaz.Utils.PlaceListDetail;
import com.digital.ayaz.Utils.Utility;
import com.digital.ayaz.adapter.PlaceListAdapter;
import com.digital.ayaz.app.MainApplication;
import com.digital.ayaz.databinding.ShoppingLayoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends BaseActivity {

    private static final String TAG_ICON = "icon";
    private static final String TAG_PLACE_ID = "place_id";
    private static final String TAG_RATING = "rating";
    private static final String TAG_PHOTOS_REFERENCE = "photo_reference";
    private PlaceListAdapter placeListAdapter;
    private List<PlaceListDetail> placeListDetailList = new ArrayList<>();
    private Double latitude, longitude;
    private ShoppingLayoutBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.shopping_layout);
        setToolBar(mBinding.toolBar, "");
        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBinding.progressWheel.spin();

        if (getIntent().getExtras() != null) {
            latitude = getIntent().getDoubleExtra(Constants.JSON_KEY_LAT, 0.0);
            longitude = getIntent().getDoubleExtra(Constants.JSON_KEY_LNG, 0.0);
        }

        int columnCount = 1;
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mBinding.recyclerView.setLayoutManager(sglm);

        placeListAdapter = new PlaceListAdapter(this, placeListDetailList, 4);
        mBinding.recyclerView.setAdapter(placeListAdapter);

        getPlaceList(Utility.getNearByShoppingCorner(this, latitude, longitude));
    }

    public void getPlaceList(String url) {
        JsonObjectRequest placeReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                mBinding.progressWheel.stopSpinning();

                try {
                    JSONArray list = jsonObject.getJSONArray(Constants.KEY_RESULTS);
                    for (int i = 0; i < list.length(); i++) {

                        JSONObject m = list.getJSONObject(i);
                        PlaceListDetail placeListDetail = new PlaceListDetail();

                        placeListDetail.setPlace_id(m.getString(TAG_PLACE_ID));
                        placeListDetail.setIcon_url(m.getString(TAG_ICON));
                        placeListDetail.setPlace_address(m.getString(Constants.KEY_VICINITY));
                        placeListDetail.setPlace_name(m.getString(Constants.KEY_PLACE_NAME));

                        if (m.has(TAG_RATING)) {
                            placeListDetail.setPlace_rating(m.getDouble(TAG_RATING));
                        }

                        if (m.has(Constants.JSON_KEY_PHOTOS)) {
                            JSONArray photos = m.getJSONArray(Constants.JSON_KEY_PHOTOS);
                            for (int j = 0; j < photos.length(); j++) {
                                JSONObject photo = photos.getJSONObject(j);
                                ArrayList<String> photos_reference = new ArrayList<>();
                                photos_reference.add(photo.getString(TAG_PHOTOS_REFERENCE));
                                placeListDetail.setPhoto_url(photos_reference);
                            }
                        }
                        placeListDetailList.add(placeListDetail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                placeListAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        MainApplication.getInstance().addToAPIRequestQueue(placeReq);
    }

    @Override
    public void onResume() {
        setInternetConnectionListner(mBinding.interMsg);
        super.onResume();

    }
}
