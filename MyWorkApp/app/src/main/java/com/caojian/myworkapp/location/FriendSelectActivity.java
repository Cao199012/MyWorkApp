package com.caojian.myworkapp.location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.friend.ItemFragment;
import com.caojian.myworkapp.friend.dummy.DummyContent;
import com.caojian.myworkapp.login.LoginActivity;
import com.caojian.myworkapp.until.ActivityUntil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendSelectActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener{

    public static void go2FriendSelectActivity(Context from){
        Intent intent = new Intent(from,FriendSelectActivity.class);
        ((Activity)from).startActivityForResult(intent,102);
    }
    @BindView(R.id.toolbar2)
    Toolbar toolbar;
    @BindView(R.id.fragment_container)
    FrameLayout mContainer;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_select);
        unbinder = ButterKnife.bind(this);
        toolbar.setTitle("选择好友");
        ActivityUntil.initActionBar(toolbar,FriendSelectActivity.this);

        ItemFragment itemFragment = (ItemFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(itemFragment == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,ItemFragment.newInstance(1)).commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        //放弃
        setResult(RESULT_CANCELED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        if(item.isChecked)
        {
            dummyItemList.add(item);
        }else
        {
            dummyItemList.remove(item);
        }
    }

    public void selectDone(View v)
    {
        //for ()
        Intent intent = new Intent();
        intent.putExtra("num",dummyItemList.size());
        setResult(RESULT_OK,intent);
        finish();
    }
}
