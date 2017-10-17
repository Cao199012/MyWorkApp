package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.adapter.FriendGroupListAdapter;
import com.caojian.myworkapp.model.data.FriendGroupItem;
import com.caojian.myworkapp.widget.LineDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendGroupActivity extends BaseTitleActivity implements FriendGroupListAdapter.ItemClick {
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

    private FriendGroupListAdapter listAdapter;
    private List<FriendGroupItem.DataBean> mListData = new ArrayList<>();
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_group);
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("好友群");

        initRecy();
        mSwipResfrsh.setColorSchemeColors(getResources().getColor(R.color.red));
        mSwipResfrsh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //cancel fresh
                        mSwipResfrsh.setRefreshing(false);
                    }
                },1000);
            }
        });
    }
    private void initRecy() {
        for (int i = 0; i < 15;i++)
        {
            FriendGroupItem.DataBean dataBean = new FriendGroupItem.DataBean();
            mListData.add(dataBean);
        }
        listAdapter = new FriendGroupListAdapter(mListData,this,FriendGroupActivity.this);
        mRecy_group.setLayoutManager(new LinearLayoutManager(FriendGroupActivity.this));
        mRecy_group.addItemDecoration(new LineDecoration(FriendGroupActivity.this));
        mRecy_group.setAdapter(listAdapter);
    }
    //创建新的群组
    public void go2CreateActivity(View view)
    {
        GroupCreateActivity.go2GroupCreateActivity(FriendGroupActivity.this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void itemSlect(FriendGroupItem.DataBean item) {
        Gson gson = new Gson();
        String databena =  gson.toJson(item);
        GroupDetailActivity.go2GroupDetailActivity(FriendGroupActivity.this,databena);
    }
}
