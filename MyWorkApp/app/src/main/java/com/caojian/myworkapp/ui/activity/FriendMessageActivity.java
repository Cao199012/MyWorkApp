package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.adapter.MessageAdapter;
import com.caojian.myworkapp.model.data.MessageItem;
import com.caojian.myworkapp.widget.LineDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendMessageActivity extends BaseTitleActivity implements MessageAdapter.RequestMessageListener {

    public static void go2FriendMessageActivity(Context from){
        Intent intent = new Intent(from,FriendMessageActivity.class);
        ((Activity)from).startActivityForResult(intent,102);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recy_messgae)
    RecyclerView mRecy_messgae;

    private Unbinder unbinder;
    private List<MessageItem> mListData = new ArrayList<>();
    private MessageAdapter mListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_message);

        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("请求信息");
        intRecy();
    }

    private void intRecy() {
        for (int i = 0; i < 10; i++)
        {
            mListData.add(new MessageItem());
        }
        mListAdapter = new MessageAdapter(mListData,this);
        mRecy_messgae.addItemDecoration(new LineDecoration(getBaseContext()));
        mRecy_messgae.setLayoutManager(new LinearLayoutManager(FriendMessageActivity.this));
        mRecy_messgae.setAdapter(mListAdapter);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //list 按钮点击
    @Override
    public void accept(MessageItem item) {
        // TODO: 2017/9/30 向后台发送确认请求
    }
}

