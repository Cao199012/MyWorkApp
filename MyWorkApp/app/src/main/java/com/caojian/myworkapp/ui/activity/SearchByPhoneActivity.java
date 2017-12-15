package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.AddByPhoneContrct;
import com.caojian.myworkapp.ui.contract.CheckContract;
import com.caojian.myworkapp.ui.presenter.AddByPhonePresenter;
import com.caojian.myworkapp.ui.presenter.CheckPresenter;
import com.caojian.myworkapp.widget.MyDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.caojian.myworkapp.until.ActivityUntil.CheckPhone;

public class SearchByPhoneActivity extends MvpBaseActivity<AddByPhoneContrct.View,AddByPhonePresenter> implements AddByPhoneContrct.View
                ,MyDialogFragment.FragmentDialogListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.searchView)
    SearchView mPhoneSearch;
    private Unbinder unbinder;
    AddByPhonePresenter mPersenter;
    MyDialogFragment fragment;
    String phone;
    public static void go2SearchByPhoneActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,SearchByPhoneActivity.class);
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
        phone = mPhoneSearch.getQuery().toString().trim();
        String checkResult = CheckPhone(phone);
        if(!checkResult.equals(""))
        {
            showToast(checkResult, Toast.LENGTH_SHORT);
            return;
        }
        //校验手机号
        mPersenter.checkPhone(phone);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public AddByPhonePresenter createPresenter() {
        mPersenter = new AddByPhonePresenter(SearchByPhoneActivity.this,this);
        return mPersenter;
    }
    //手机验证通过
    @Override
    public void checkSuccess() {
        if(fragment == null || !fragment.getTag().equals("check"))
        {
            fragment = MyDialogFragment.newInstance("验证结果","此用户已注册是否添加为好友","取消","确定");
        }
        fragment.show(getSupportFragmentManager(),"check");
    }

    //发送请求成功
    @Override
    public void addSuccess(String msg) {
        showToast(msg,Toast.LENGTH_SHORT);
        finish();
    }
    @Override
    public void error(String msg) {
        showToast(msg,Toast.LENGTH_SHORT);
    }

    @Override
    public void checkFail(String msg) {
        if(fragment == null || !fragment.getTag().equals("Invite"))
        {
            fragment = MyDialogFragment.newInstance("邀请加入",msg,"取消","邀请");
        }
        fragment.show(getSupportFragmentManager(),"Invite");
    }

    //dialogfragment 的监听
    @Override
    public void cancel() {
        fragment.dismiss();
        fragment = null;
    }

    @Override
    public void sure() {

        if(fragment.getTag().equals("check")){
            mPersenter.addFriend(phone,"申请加好友");
        }else {
            Intent sendIntent =new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,"This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "加入我们"));
        }
        fragment.dismiss();
        fragment = null;

    }

}
