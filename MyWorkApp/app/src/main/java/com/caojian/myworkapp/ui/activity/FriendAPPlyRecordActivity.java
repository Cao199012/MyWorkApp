package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.data.ApplyFriendsRecord;
import com.caojian.myworkapp.ui.adapter.MessageAdapter;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.ApplyRecordContract;
import com.caojian.myworkapp.ui.presenter.ApplyRecordsPresenter;
import com.caojian.myworkapp.widget.LineDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendAPPlyRecordActivity extends MvpBaseActivity<ApplyRecordContract.View,ApplyRecordsPresenter> implements MessageAdapter.RequestMessageListener,ApplyRecordContract.View {

    public static void go2FriendMessageActivity(Context from){
        Intent intent = new Intent(from,FriendAPPlyRecordActivity.class);
        ((Activity)from).startActivityForResult(intent,102);
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recy_messgae)
    RecyclerView mRecyMessage;
    private Unbinder unbinder;
    private List<ApplyFriendsRecord.DataBean.RecordsBean> mListData = new ArrayList<>();
    private MessageAdapter mListAdapter;
    ApplyRecordsPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_message);
        unbinder = ButterKnife.bind(this);
        toolbar.setTitle("请求信息");
        intRecy();
    }
    private void intRecy() {
        mListAdapter = new MessageAdapter(mListData,this,FriendAPPlyRecordActivity.this);
        mRecyMessage.addItemDecoration(new LineDecoration(getBaseContext()));
        mRecyMessage.setLayoutManager(new LinearLayoutManager(FriendAPPlyRecordActivity.this));
        mRecyMessage.setAdapter(mListAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getRecords();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    @Override
    public ApplyRecordsPresenter createPresenter() {
        mPresenter = new ApplyRecordsPresenter(FriendAPPlyRecordActivity.this,this);
        return mPresenter;
    }
    //list 按钮点击
    @Override
    public void accept(ApplyFriendsRecord.DataBean.RecordsBean item) {
        // TODO: 2017/9/30 向后台发送确认请求
        mPresenter.agreeApply(item);
    }
    //请求成功，刷新列表
    @Override
    public void getRecords(List<ApplyFriendsRecord.DataBean.RecordsBean> recordsBeanList) {
        mListData.clear();
        mListData.addAll(recordsBeanList);
        mListAdapter.notifyDataSetChanged();
    }

    //接受邀请，返回好友列表展示好友
    @Override
    public void agreeSuccess() {
      finish();
    }
    //请求失败，显示错误信息
    @Override
    public void error(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }
}

