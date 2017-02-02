package com.digital.ayaz.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.digital.ayaz.R;
import com.digital.ayaz.activity.MainActivity;
import com.digital.ayaz.activity.SelectOptionActivity;
import com.digital.ayaz.adapter.PlaceAutocompleteAdapter;
import com.digital.ayaz.databinding.FragmentMainBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class PlaceSearchSelectFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    protected GoogleApiClient mGoogleApiClient;
    private FragmentMainBinding mBinding;

    public PlaceSearchSelectFragment() {
    }

    public static PlaceSearchSelectFragment newInstance(int sectionNumber) {
        PlaceSearchSelectFragment fragment = new PlaceSearchSelectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        toolbar = mBinding.toolBar;
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("");

        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .build();
        final PlaceAutocompleteAdapter mAdapter = new PlaceAutocompleteAdapter(getActivity(), android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS, null);
        mBinding.enterplace.setAdapter(mAdapter);

        mBinding.enterplace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(i);
                final String placeId = String.valueOf(item.placeId);

                Intent i1 = new Intent(getActivity(), SelectOptionActivity.class);
                i1.putExtra("placeid", placeId);
                i1.putExtra("name", item.description);
                startActivity(i1);

            }
        });
        Resources res = getResources();
        final String[] places = res.getStringArray(R.array.places);
        final String[] place_id = res.getStringArray(R.array.place_id);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, places);
        mBinding.places.setAdapter(arrayAdapter);

        mBinding.places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String placeId = place_id[i];
                Intent intent = new Intent(getActivity(), SelectOptionActivity.class);
                intent.putExtra("placeid", placeId);
                intent.putExtra("name", places[i]);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        setInternetConnectionListner(mBinding.interMsg);
        super.onResume();

    }
}

