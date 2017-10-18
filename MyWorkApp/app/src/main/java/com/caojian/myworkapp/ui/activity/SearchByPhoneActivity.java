package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.CheckContract;
import com.caojian.myworkapp.ui.presenter.CheckPersenter;
import com.caojian.myworkapp.widget.MyDailogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.caojian.myworkapp.until.ActivityUntil.CheckPhone;

public class SearchByPhoneActivity extends MvpBaseActivity<CheckContract.View,CheckPersenter> implements CheckContract.View
                ,MyDailogFragment.FragmentDialogListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.searchView)
    SearchView mPhoneSearch;


    private Unbinder unbinder;
    CheckPersenter mPersenter;

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
    }

    @OnClick(R.id.btnSearch)
    public void search()
    {
        String phone = mPhoneSearch.getQuery().toString().trim();
        String checkResult = CheckPhone(phone);
        if(!checkResult.equals(""))
        {
            showToast(checkResult, Toast.LENGTH_SHORT);
            return;
        }

        // TODO: 2017/9/29 校验手机号
        mPersenter.checkPhone(phone);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public CheckPersenter createPresenter() {
        mPersenter = new CheckPersenter(this);
        return mPersenter;
    }

    @Override
    public void checkResultError(String result) {

    }

    @Override
    public void checkResultSuccess() {
        MyDailogFragment fragment = MyDailogFragment.newInstance("验证结果","此用户已注册是否添加为好友","取消","确定");
        fragment.show(getSupportFragmentManager(),"check");
    }

    //dialogfragment 的监听
    @Override
    public void cancel() {

    }

    @Override
    public void sure() {

    }
}
