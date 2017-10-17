package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.data.LocationItem;
import com.caojian.myworkapp.ui.adapter.LocationDetailAdapter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.widget.LineDecoration;
import com.caojian.myworkapp.widget.MyDailogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocationDetailActivity extends BaseTitleActivity implements LocationDetailAdapter.ItemClick,MyDailogFragment.FragmentDialogListener {

    public static void go2LocationDetailActivity(Context from){
        Intent intent = new Intent(from,LocationDetailActivity.class);
        ((Activity)from).startActivityForResult(intent,REQUEST_CODE);
    }

    public static final int REQUEST_CODE = 102;
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

        intRecy();
    }
    private void intRecy() {
        for (int i = 0; i < 5; i++)
        {
            mListData.add(new LocationItem());
        }
        mListAdapter = new LocationDetailAdapter(mListData,this);
        mRecy_location_detail.addItemDecoration(new LineDecoration(LocationDetailActivity.this));
        mRecy_location_detail.setLayoutManager(new LinearLayoutManager(LocationDetailActivity.this));
        mRecy_location_detail.setAdapter(mListAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    MyDailogFragment myDailogFragment;

    @Override
    public void itemSlect(LocationItem item) {
        // TODO: 2017/9/5 提升是否要取消监测好友
        if(myDailogFragment == null){
            myDailogFragment = MyDailogFragment.newInstance("aaaa","不再查看好友位置","取消","确定");
        }

        myDailogFragment.show(getSupportFragmentManager(),"aa");
    }

    @Override
    public void cancel() {
        myDailogFragment.dismiss();
    }

    @Override
    public void sure() {

    }
}
