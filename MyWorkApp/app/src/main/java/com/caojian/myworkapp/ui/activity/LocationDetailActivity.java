package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.data.LocationItem;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.ui.adapter.LocationDetailAdapter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocationDetailActivity extends BaseTitleActivity implements LocationDetailAdapter.ItemClick{

    public static void go2LocationDetailActivity(Context from){
        Intent intent = new Intent(from,LocationDetailActivity.class);
        ((Activity)from).startActivityForResult(intent,REQUEST_CODE);
    }
    public static final int REQUEST_CODE = 1002;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.expandableListView_location)
    ExpandableListView mExpandableListView;
    private Unbinder unbinder;
    private LocationDetailAdapter mListAdapter;
    MyApplication mApplication;
    List<FriendsAndGroupsMsg.DataBean.GroupsBean> mFriendData = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        unbinder = ButterKnife.bind(this);
        toolbar.setTitle("定位好友");
        mApplication = (MyApplication) getApplication();
        if (mApplication.getmSeeFriends() == null || mApplication.getmSeeFriends().isEmpty()) return;
        mFriendData.clear();
        mFriendData.addAll(mApplication.getmSeeFriends());
        mListAdapter = new LocationDetailAdapter(mApplication.getmSeeFriends(),this);
        mExpandableListView.setAdapter(mListAdapter);
        for (int i = 0; i < mFriendData.size();i++){
            mExpandableListView.expandGroup(i);
        }

    }

    //单击好友跳到此好友位置
    @Override
    public void itemClick(LocationItem item) {
        if (item == null){
            showToast("未获取好友坐标", Toast.LENGTH_SHORT);
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("lat",item.getLatitude());
        intent.putExtra("lot",item.getLongitude());
        setResult(RESULT_OK,intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
