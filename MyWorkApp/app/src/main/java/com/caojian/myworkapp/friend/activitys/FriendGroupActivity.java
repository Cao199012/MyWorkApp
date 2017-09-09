package com.caojian.myworkapp.friend.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendGroupActivity extends AppCompatActivity {
    public static void go2FriendGroupActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,FriendGroupActivity.class);
        // intent.pu
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar_group)
    Toolbar toolbar;
    @BindView(R.id.recy_group)
    RecyclerView mRecy_messgae;

    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_group);
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("好友群");
        ActivityUntil.initActionBar(toolbar,FriendGroupActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
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
