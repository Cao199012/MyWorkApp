package com.caojian.myworkapp.friend.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GroupCreateActivity extends AppCompatActivity {

    public static void go2GroupCreateActivity(Context from) {
        Intent intent = new Intent(from, GroupCreateActivity.class);

        ((Activity) from).startActivityForResult(intent, 102);
    }

    @BindView(R.id.toolbar_group_create)
    Toolbar toolbar;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("新建好友群");
        ActivityUntil.initActionBar(toolbar,GroupCreateActivity.this,R.drawable.ic_arrow_back);
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
}
