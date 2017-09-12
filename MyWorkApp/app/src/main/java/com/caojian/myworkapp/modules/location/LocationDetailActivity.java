package com.caojian.myworkapp.modules.location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BasetitleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocationDetailActivity extends BasetitleActivity implements LocationDetailAdapter.ItemClick {

    public static void go2LocationDetailActivity(Context from){
        Intent intent = new Intent(from,LocationDetailActivity.class);
        ((Activity)from).startActivityForResult(intent,102);
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recy_location_detail)
    RecyclerView mRecy_location_detail;

    private Unbinder unbinder;
    private List<LocationItem> mListData = new ArrayList<>();
    private LocationDetailAdapter mListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("定位好友");
       // ActivityUntil.initActionBar(toolbar,LocationDetailActivity.this,R.drawable.ic_arrow_back);
        intRecy();
    }
    private void intRecy() {
        for (int i = 0; i < 5; i++)
        {
            mListData.add(new LocationItem());
        }
        mListAdapter = new LocationDetailAdapter(mListData,this);
        mRecy_location_detail.setLayoutManager(new LinearLayoutManager(LocationDetailActivity.this));
        mRecy_location_detail.setAdapter(mListAdapter);
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home)
//        {
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        finish();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public void itemSlect(LocationItem item) {
        // TODO: 2017/9/5 提升是否要取消监测好友
    }
}