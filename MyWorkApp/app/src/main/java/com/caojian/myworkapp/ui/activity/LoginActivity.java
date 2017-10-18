package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.caojian.myworkapp.ui.presenter.LoginPresenter;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.contract.LoginContract;
import com.caojian.myworkapp.manager.ActivityControl;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginActivity extends MvpBaseActivity<LoginContract.View,LoginPresenter>implements LoginContract.View{

    @BindView(R.id.editPhone)
    EditText mEditName;
    @BindView(R.id.editPassword)
    EditText mEditPassword;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private LoginPresenter mLoginPresenter;

    private Unbinder unbinder;

    public static void go2LoginActivity(Context from){
        Intent intent = new Intent(from,LoginActivity.class);
        from.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void login(View v){
        String name = mEditName.getText().toString();
        String password = mEditPassword.getText().toString();


        if(!ActivityUntil.CheckPhone(name).equals(""))
        {
            showToast(ActivityUntil.CheckPhone(name), Toast.LENGTH_SHORT);
            return;
        }
        if(password.isEmpty())
        {
            showToast("密码不能为空", Toast.LENGTH_SHORT);
            return;
        }

        //ProgressDialog只能和activity绑定 一个activity对应一个ProgressDialog
        showProgerss(LoginActivity.this);

        //
        // 模拟请求 1秒之后取消dilog 跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUntil.saveToken(LoginActivity.this,"aaaaa");
                hideProgress();
                MainActivity.go2MainActivity(LoginActivity.this);
                ActivityControl.finishActivity();
            }
        },1000);

        // TODO: 2017/8/21 后台校验手机号和密码
       // mLoginPresenter.checkLogin(name,password);


    }

    public void register(View v)
    {
        PhoneCheckActivity.go2PhoneCheckActivity(LoginActivity.this,1);
        //finish();
    }

    public void forgetPassword(View v)
    {
        PhoneCheckActivity.go2PhoneCheckActivity(LoginActivity.this,2);
        //finish();
    }



    @Override
    public void LoginSuccess() {

    }

    @Override
    public void LoginError(String errorMsg) {

    }

    @Override
    public LoginPresenter createPresenter() {
        mLoginPresenter = new LoginPresenter(this);
        return mLoginPresenter;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
