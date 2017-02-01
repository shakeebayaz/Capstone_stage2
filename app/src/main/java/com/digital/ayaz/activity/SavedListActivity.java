package com.digital.ayaz.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.digital.ayaz.Utils.Constants;
import com.digital.ayaz.R;
import com.digital.ayaz.Utils.Utility;
import com.digital.ayaz.storage.DatabaseSave;
import com.digital.ayaz.adapter.PlaceListAdapter;
import com.digital.ayaz.Utils.PlaceListDetail;
import com.digital.ayaz.Utils.ProgressWheel;
import com.digital.ayaz.app.MainApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SavedListActivity extends BaseActivity {

    TextView title, nodata;
    DatabaseSave db;

    private PlaceListAdapter placeListAdapter;
    private List<PlaceListDetail> placeListDetailList = new ArrayList<>();

    private static final String TAG_PHOTOS_REFERENCE = "photo_reference";
    ProgressWheel progressWheel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_list_layout);

        db = new DatabaseSave(this);

        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        progressWheel.spin();

        int columnCount = 1;
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.name);
        nodata = (TextView) findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        setToolBar(toolbar, "");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String mode = getIntent().getStringExtra("mode");
        List<String> savedPlaceIdList = new ArrayList<String>();
        switch (mode) {
            case "place":
                title.setText("Saved Places");
                savedPlaceIdList=db.getAllPlaces();
                break;
            case "restaurant":
                title.setText("Saved Restaurant");
                savedPlaceIdList=db.getAllRes();
                break;
            case "hotel":
                title.setText("Saved Hotels");
                savedPlaceIdList=db.getAllHotels();
                break;
        }
        placeListAdapter = new PlaceListAdapter(this, placeListDetailList, 1);
        recyclerView.setAdapter(placeListAdapter);
        if (savedPlaceIdList.size() > 0) {
            for (String id : savedPlaceIdList) {
                getPlaceDetail(Utility.getPlaceApiUrlFromPLaceId(this, id));
            }
        } else {
            nodata.setVisibility(View.VISIBLE);
        }


    }

    public void getPlaceDetail(String url) {
        final ArrayList<String> photos_reference = new ArrayList<>();
        JsonObjectRequest movieReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    progressWheel.stopSpinning();
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
}
