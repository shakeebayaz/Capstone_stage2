package com.digital.ayaz.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.digital.ayaz.databinding.CommonRecyclerBinding;
import com.digital.ayaz.R;
import com.digital.ayaz.adapter.ViewPagerAdapterTourist;
public class TouristPlaceListActivity extends BaseActivity {

    ViewPagerAdapterTourist adapter;
    private CommonRecyclerBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.common_recycler);
        mBinding.name.setText("Tourist Places");
        setToolBar(mBinding.toolBar,"");
        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter = new ViewPagerAdapterTourist(getSupportFragmentManager(), new String[]{"MUSEUMS", "NIGHT CLUB"});
        mBinding.pager.setAdapter(adapter);
        mBinding.tabs.setupWithViewPager(mBinding.pager);
    }
}
