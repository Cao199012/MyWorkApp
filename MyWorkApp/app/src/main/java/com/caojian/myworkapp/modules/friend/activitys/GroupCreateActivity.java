package com.caojian.myworkapp.modules.friend.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BaseTitleActivity;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GroupCreateActivity extends BaseTitleActivity {

    public static void go2GroupCreateActivity(Context from) {
        Intent intent = new Intent(from, GroupCreateActivity.class);
        ((Activity) from).startActivityForResult(intent, 102);
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        unbinder = ButterKnife.bind(this);
        //设置标题名
        toolbar.setTitle("新建好友群");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
