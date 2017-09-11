package com.caojian.myworkapp.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.caojian.myworkapp.until.ActivityControl;

/**
 * Created by caojian on 2017/7/19.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //向control list 添加打开的Activity
        ActivityControl.addActivity(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除关闭的Activity
        ActivityControl.removeActivity(this);
    }
    ProgressDialog dialog = null;
    protected void showProgerss(Context context)
    {
        dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("链接中...");
        dialog.show();
    }

    protected void hideProgress()
    {
        dialog.cancel();
        dialog = null;
    }
}
