package com.caojian.myworkapp.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.caojian.myworkapp.until.ActivityControler;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.ButterKnife;

/**
 * Created by caojian on 2017/7/19.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //向control list 添加打开的Activity
        ActivityControler.addActivty(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除关闭的Activity
        ActivityControler.removeActivity(this);
    }
}
