package com.digital.ayaz.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
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
import com.digital.ayaz.databinding.SavedListLayoutBinding;
import com.digital.ayaz.storage.TouristContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SavedListActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG_PHOTOS_REFERENCE = "photo_reference";
    private static final int SAVED_LIST_LOADER = 1;
    private PlaceListAdapter placeListAdapter;
    private List<PlaceListDetail> placeListDetailList = new ArrayList<>();
    private SavedListLayoutBinding mBinding;
    private String mode;
    List<String> savedPlaceIdList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.saved_list_layout);
        mBinding.progressWheel.spin();
        int columnCount = 1;
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mBinding.recyclerView.setLayoutManager(sglm);
        mBinding.nodata.setVisibility(View.GONE);
        setToolBar(mBinding.toolBar, "");
        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mode = getIntent().getStringExtra("mode");
        mBinding.name.setText(R.string.saved_res);
        getLoaderManager().initLoader(SAVED_LIST_LOADER, null, this);
    }

    private List<String> getAllSavedItems(Cursor cursor) {
        List<String> savedPlaceIdList = new ArrayList<String>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                savedPlaceIdList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return savedPlaceIdList;
    }

    public void getPlaceDetail(String url) {
        final ArrayList<String> photos_reference = new ArrayList<>();
        JsonObjectRequest movieReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    mBinding.progressWheel.stopSpinning();
                    PlaceListDetail detail = new PlaceListDetail();
                    JSONObject list = jsonObject.optJSONObject(Constants.KEY_RESULT);
                    if (list != null) {
                        JSONArray photos = list.getJSONArray(Constants.JSON_KEY_PHOTOS);
                        for (int i = 0; i < photos.length(); i++) {
                            JSONObject photo = photos.optJSONObject(i);
                            photos_reference.add(photo.optString(TAG_PHOTOS_REFERENCE));
                            detail.setPhoto_url(photos_reference);
                        }
                        detail.setPlace_name(list.optString(Constants.KEY_PLACE_NAME));
                        detail.setPlace_address(list.optString(Constants.KEY_VICINITY));
                        detail.setPlace_id(list.optString("place_id"));
                        detail.setPlace_rating(list.optDouble("rating"));
                        placeListDetailList.add(detail);
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

        MainApplication.getInstance().addToAPIRequestQueue(movieReq);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        switch (mode) {
            case "place":
                mBinding.name.setText(R.string.saved_place);
                loader = new CursorLoader(
                        this,
                        TouristContract.PlaceEntry.buildPlaceUri(),
                        null, null, null, null);
                break;
            case "restaurant":
                mBinding.name.setText(R.string.saved_res);
                loader = new CursorLoader(
                        this,
                        TouristContract.RestaurantEntry.buildRestaurantUri(),
                        null, null, null, null);
                break;
            case "hotel":
                mBinding.name.setText(R.string.saved_hotels);
                loader = new CursorLoader(
                        this,
                        TouristContract.HotelEntry.buildHotelsUri(),
                        null, null, null, null);
                break;
        }

        return loader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        savedPlaceIdList = getAllSavedItems(data);
        placeListAdapter = new PlaceListAdapter(this, placeListDetailList, 1);
        mBinding.recyclerView.setAdapter(placeListAdapter);
        if (savedPlaceIdList.size() > 0) {
            for (String id : savedPlaceIdList) {
                getPlaceDetail(Utility.getPlaceApiUrlFromPLaceId(this, id));
            }
        } else {
            mBinding.nodata.setVisibility(View.VISIBLE);
            mBinding.progressWheel.stopSpinning();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
