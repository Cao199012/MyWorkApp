package com.caojian.myworkapp.friend.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BaseActivity;
import com.caojian.myworkapp.friend.adapter.MessageAdapter;
import com.caojian.myworkapp.friend.dummy.MessageItem;
import com.caojian.myworkapp.recy.LineDecoration;
import com.caojian.myworkapp.until.ActivityUntil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchByContactActivity extends BaseActivity {

    public static void go2SearchByContactActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,SearchByContactActivity.class);
        // intent.pu
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recy_contracts)
    RecyclerView mRecy_contracts;

    private Unbinder unbinder;
    private List<MessageItem> mListData = new ArrayList<>();
    private MessageAdapter mListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_contact);
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("请求信息");
        ActivityUntil.initActionBar(toolbar,SearchByContactActivity.this,R.drawable.ic_arrow_back);
        intRecy();
    }

    private void intRecy() {
        for (int i = 0; i < 15; i++)
        {
            mListData.add(new MessageItem());
        }
        mListAdapter = new MessageAdapter(mListData);
        mRecy_contracts.setLayoutManager(new LinearLayoutManager(SearchByContactActivity.this));
        mRecy_contracts.addItemDecoration(new LineDecoration(SearchByContactActivity.this));
        mRecy_contracts.setAdapter(mListAdapter);
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
