package com.caojian.myworkapp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.caojian.myworkapp.base.MvpBaseActivity;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.mvp.MyMvpActivity;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends MvpBaseActivity<LoginContract.View,LoginPresenter>implements LoginContract.View{

    @BindView(R.id.editPhone)
    EditText mEditName;
    @BindView(R.id.editPassword)
    EditText mEditPassword;
    private LoginPresenter mLoginPresenter;

    public static void go2LoginActivity(Context from){
        Intent intent = new Intent(from,LoginActivity.class);
        from.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void login(View v){
        String name = mEditName.getText().toString();
        String password = mEditPassword.getText().toString();

        MyMvpActivity.go2MyMvpActivity(LoginActivity.this);

        if(name.isEmpty())
        {
            ActivityUntil.showToast(LoginActivity.this,"号码不能为空", Toast.LENGTH_SHORT);
            return;
        }

        if(password.isEmpty())
        {
            ActivityUntil.showToast(LoginActivity.this,"密码不能为空", Toast.LENGTH_SHORT);
            return;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //视图消失 取消toast显示
        ActivityUntil.disToast();
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
}
