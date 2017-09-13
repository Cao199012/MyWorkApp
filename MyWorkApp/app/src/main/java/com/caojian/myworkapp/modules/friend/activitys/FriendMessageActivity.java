package com.caojian.myworkapp.modules.friend.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BaseTitleActivity;
import com.caojian.myworkapp.modules.friend.adapter.MessageAdapter;
import com.caojian.myworkapp.modules.friend.dummy.MessageItem;
import com.caojian.myworkapp.until.recyutil.LineDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendMessageActivity extends BaseTitleActivity {

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
        mListAdapter = new MessageAdapter(mListData);
        mRecy_messgae.addItemDecoration(new LineDecoration(getBaseContext()));
        mRecy_messgae.setLayoutManager(new LinearLayoutManager(FriendMessageActivity.this));
        mRecy_messgae.setAdapter(mListAdapter);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

