package com.caojian.myworkapp.modules.friend.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BasetitleActivity;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SearchByPhoneActivity extends BasetitleActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.searchView)
    SearchView mPhoneSearch;
    private Unbinder unbinder;

    public static void go2SearchByPhoneActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,SearchByPhoneActivity.class);
        // intent.pu
        fromClass.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_phone);
        unbinder = ButterKnife.bind(this);

        mToolbar.setTitle("添加好友");
       // ActivityUntil.initActionBar(mToolbar,SearchByPhoneActivity.this,R.drawable.ic_arrow_back);

    }

    @OnClick(R.id.btnSearch)
    public void search()
    {
        mPhoneSearch.getQuery();
        ActivityUntil.showToast(SearchByPhoneActivity.this,mPhoneSearch.getQuery().toString(), Toast.LENGTH_SHORT);
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home)
//        {
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        finish();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
