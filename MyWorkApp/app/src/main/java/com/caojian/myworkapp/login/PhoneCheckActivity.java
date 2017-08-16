package com.caojian.myworkapp.login;

import android.content.Context;
import android.os.Bundle;

import com.caojian.myworkapp.base.BaseActivity;
import com.caojian.myworkapp.R;

/**
 * 注册和重设密码都需要验证密码是否已经注册
 */
public class PhoneCheckActivity extends BaseActivity {

    public void go2PhoneCheckActivity(Context from,int flag)
    {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
    }
}
