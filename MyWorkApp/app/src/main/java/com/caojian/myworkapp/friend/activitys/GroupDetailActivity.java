package com.caojian.myworkapp.friend.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.friend.adapter.GroupMembersAdapter;
import com.caojian.myworkapp.friend.adapter.MessageAdapter;
import com.caojian.myworkapp.friend.dummy.FriendItem;
import com.caojian.myworkapp.friend.dummy.MessageItem;
import com.caojian.myworkapp.until.ActivityUntil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GroupDetailActivity extends AppCompatActivity implements GroupMembersAdapter.ItemClick {
    public static void go2GroupDetailActivity(Context from, String detail) {
        Intent intent = new Intent(from, GroupDetailActivity.class);
        intent.putExtra("detail", detail);
        ((Activity) from).startActivityForResult(intent, 102);
    }

    @BindView(R.id.toolbar6)
    Toolbar toolbar;
    @BindView(R.id.recy_group_member)
    RecyclerView mRecy_member;
    private Unbinder unbinder;
    private List<FriendItem> mListData = new ArrayList<>();
    private GroupMembersAdapter mListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("好友群详情");
        ActivityUntil.initActionBar(toolbar, GroupDetailActivity.this,R.drawable.ic_arrow_back);
        intRecy();
    }
    private void intRecy() {
        for (int i = 0; i < 10; i++)
        {
            mListData.add(new FriendItem());
        }
        mListAdapter = new GroupMembersAdapter(mListData,this,GroupDetailActivity.this);
        mRecy_member.setLayoutManager(new GridLayoutManager(GroupDetailActivity.this,5));
        mRecy_member.setAdapter(mListAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void itemSlect(FriendItem item) {

    }
}
