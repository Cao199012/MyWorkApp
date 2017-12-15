package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.Request.PositionInfoDto;
import com.caojian.myworkapp.model.data.LocationItem;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.ui.adapter.LocationDetailAdapter;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.LocationDetailContact;
import com.caojian.myworkapp.ui.presenter.LocationDetailPresenter;
import com.caojian.myworkapp.widget.LineDecoration;
import com.caojian.myworkapp.widget.MyDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocationDetailActivity extends MvpBaseActivity<LocationDetailContact.View,LocationDetailPresenter> implements LocationDetailAdapter.ItemClick,MyDialogFragment.FragmentDialogListener
,LocationDetailContact.View{

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
    private LocationDetailPresenter mPresenter;
    LocationItem mHandleItem;
    boolean isChanged = false;
    MyApplication mApplication;
    List<FriendsAndGroupsMsg.DataBean.GroupsBean> mFriendData = new LinkedList<>();
    int changeType = 0;
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
    }


    @Override
    public LocationDetailPresenter createPresenter() {
        mPresenter = new LocationDetailPresenter(this,LocationDetailActivity.this);
        return mPresenter;
    }

    MyDialogFragment myDialogFragment;

    //单击好友跳到此好友位置
    @Override
    public void itemClick(LocationItem item) {
        Intent intent = new Intent();
        intent.putExtra("lat",item.getLatitude());
        intent.putExtra("lat",item.getLongitude());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void itemSelect(LocationItem item) {
        mHandleItem = item;
        // TODO: 2017/9/5 提升是否要取消监测好友
        myDialogFragment = MyDialogFragment.newInstance("取消查看","不再查看好友位置","取消","确定");
        myDialogFragment.show(getSupportFragmentManager(),"newCancel");
    }

    private FriendsAndGroupsMsg.DataBean.GroupsBean mHandleGroup;
    @Override
    public void groupSelect(FriendsAndGroupsMsg.DataBean.GroupsBean groupsBean) {
        mHandleGroup = groupsBean;
        myDialogFragment = MyDialogFragment.newInstance("取消查看","不再查看全组好友位置","取消","确定");
        myDialogFragment.show(getSupportFragmentManager(),"groupCancel");
    }

    //取消查看好友位置
    @Override
    public void cancel() {
        myDialogFragment.dismiss();
    }

    @Override
    public void sure() {
        if(myDialogFragment.getTag().equals("newCancel"))
        {
            List<String> _list = new ArrayList<>();
            _list.add(mHandleItem.getPhoneNo());
            String _str = new Gson().toJson(_list);
            mPresenter.cancelLocation(_str);
        }else {
            mPresenter.cancelGroupLocation(mHandleGroup.getGroupId());
        }
        myDialogFragment.dismiss();

    }
    //取消检测好友成功
    @Override
    public void cancelSuccess(String msg) {
        isChanged = true;
       // mApplication.get.remove(mHandleItem);
       // mFriendData.remove(mHandleGroup);
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if(isChanged){
            setResult(RESULT_CANCELED);
        }else {
            setResult(RESULT_OK);
        }
        finish();
        super.onBackPressed();
    }

    @Override
    public void error(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
