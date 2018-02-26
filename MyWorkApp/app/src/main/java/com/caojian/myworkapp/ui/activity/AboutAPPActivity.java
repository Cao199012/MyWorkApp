package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.model.response.UpdateResponse;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.PersonalContract;
import com.caojian.myworkapp.ui.presenter.PersonalPresenter;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.caojian.myworkapp.ui.activity.UpdateActivity.go2UpdateActivity;

public class AboutAppActivity extends MvpBaseActivity<PersonalContract.View,PersonalPresenter> implements PersonalContract.View {
    public static void go2AboutAPPActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,AboutAppActivity.class);
        fromClass.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_version)
    TextView mTv_version;
    PersonalPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        ButterKnife.bind(this);
        mToolbar.setTitle("关于我们");
        mTv_version.setText(ActivityUntil.getVersionName(AboutAppActivity.this));
    }

    @Override
    public PersonalPresenter createPresenter() {
        mPresenter = new PersonalPresenter(AboutAppActivity.this,this);
        return mPresenter;
    }

    //跳转会员购买页面
    public void goFunction(View view)
    {
        // TODO: 2018/1/9  常见问题网页
        WebViewDetailActivity.go2WebViewDetailActivity(AboutAppActivity.this,2);
    }

    //检测版本
    public void checkVersion(View view){
        ((MyApplication)getApplicationContext()).setNoForceFlag("0"); //检测版本，更改更新标志位
       // checkVersion(3);
        mPresenter.getPersonalInfo();
    }


    @Override
    public void getPersonalSuccess(PersonalMsg personalMsg) {
        showToast("已是最新版本", Toast.LENGTH_SHORT);
    }

    @Override
    public void changeMsgSuccess(String msg) {

    }

    @Override
    public void error(String errorMsg) {

    }
}
