package com.caojian.myworkapp.friend.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BaseActivity;
import com.caojian.myworkapp.friend.dummy.FriendItem;
import com.caojian.myworkapp.until.ActivityUntil;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendDetailActivity extends BaseActivity {
    public static void go2FriendDetailActivity(Context fromClass,String databean)
    {
        Intent intent = new Intent(fromClass,FriendDetailActivity.class);
        intent.putExtra("databean",databean);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar_detail)
    Toolbar toolbar;


    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        FriendItem.DataBean dataBean = new Gson().fromJson(getIntent().getStringExtra("databean"),FriendItem.DataBean.class);
        Log.i("databean",dataBean.toString());
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("好友详情");
        ActivityUntil.initActionBar(toolbar,FriendDetailActivity.this,R.drawable.ic_arrow_back);
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
