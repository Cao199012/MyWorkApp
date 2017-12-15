package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.ui.adapter.FriendGroupListAdapter;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.FriendGroupContract;
import com.caojian.myworkapp.ui.presenter.GetGroupPresenter;
import com.caojian.myworkapp.widget.LineDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendGroupActivity extends MvpBaseActivity<FriendGroupContract.View,GetGroupPresenter> implements FriendGroupListAdapter.ItemClick ,FriendGroupContract.View{
    public static void go2FriendGroupActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,FriendGroupActivity.class);
        // intent.pu
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recy_group)
    RecyclerView mRecy_group;
    @BindView(R.id.swip_group)
    SwipeRefreshLayout mSwipResfrsh;
    GetGroupPresenter mPresenter;
    private FriendGroupListAdapter mListAdapter;
    private List<FriendsAndGroupsMsg.DataBean.GroupsBean> mListData = new ArrayList<>();
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_group);
        unbinder = ButterKnife.bind(this);
        toolbar.setTitle("好友群");
        initRecy();
        mPresenter.getFriendGroup();
        mSwipResfrsh.setColorSchemeColors(getResources().getColor(R.color.red));
        mSwipResfrsh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getFriendGroup();
                mSwipResfrsh.setRefreshing(false);
            }
        });
    }
    private void initRecy() {
        mListAdapter = new FriendGroupListAdapter(mListData,this,FriendGroupActivity.this);
        mRecy_group.setLayoutManager(new LinearLayoutManager(FriendGroupActivity.this));
        mRecy_group.addItemDecoration(new LineDecoration(FriendGroupActivity.this));
        mRecy_group.setAdapter(mListAdapter);
    }
    //创建新的群组
    public void go2CreateActivity(View view)
    {
        GroupCreateActivity.go2GroupCreateActivity(FriendGroupActivity.this);
    }

    @Override
    public GetGroupPresenter createPresenter() {
        mPresenter = new GetGroupPresenter(FriendGroupActivity.this,this);
        return mPresenter;
    }

    @Override
    public void itemSlect(FriendsAndGroupsMsg.DataBean.GroupsBean item) {
//        Gson gson = new Gson();
//        String databean =  gson.toJson(item);
        GroupDetailActivity.go2GroupDetailActivity(FriendGroupActivity.this,item.getGroupId());
    }

    //请求结果
    @Override
    public void onSuccess(List<FriendsAndGroupsMsg.DataBean.GroupsBean> Groups) {
        mSwipResfrsh.setRefreshing(false);
        mListData.clear();
        mListData.addAll(Groups);
        mListData.remove(mListData.size()-1);
        mListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onFailed(String errorMsg) {
        mSwipResfrsh.setRefreshing(false);
        showToast(errorMsg, Toast.LENGTH_SHORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
