package com.caojian.myworkapp.modules.friend.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BasetitleActivity;
import com.caojian.myworkapp.modules.friend.activitys.GroupDetailActivity;
import com.caojian.myworkapp.modules.friend.adapter.FriendGroupListAdapter;
import com.caojian.myworkapp.modules.friend.dummy.FriendGroupItem;
import com.caojian.myworkapp.until.recyutil.LineDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.caojian.myworkapp.modules.friend.activitys.GroupCreateActivity.go2GroupCreateActivity;

public class FriendGroupActivity extends BasetitleActivity implements FriendGroupListAdapter.ItemClick {
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

    private FriendGroupListAdapter listAdapter;
    private List<FriendGroupItem.DataBean> mListData = new ArrayList<>();
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_group);
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("好友群");
       // ActivityUntil.initActionBar(toolbar,FriendGroupActivity.this,R.drawable.ic_arrow_back);
        initRecy();
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
    public void go2CreateActivity(View view)
    {
        go2GroupCreateActivity(FriendGroupActivity.this);
    }
   // @Override
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
    public void itemSlect(FriendGroupItem.DataBean item) {
        Gson gson = new Gson();
        String databena =  gson.toJson(item);
        GroupDetailActivity.go2GroupDetailActivity(FriendGroupActivity.this,databena);
    }
}
