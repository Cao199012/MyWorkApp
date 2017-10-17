package com.caojian.myworkapp.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.manager.ActivityControl;
import com.caojian.myworkapp.until.ActivityUntil;

/**
 * Created by caojian on 2017/7/19.
 */

public class BaseTitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //向control list 添加打开的Activity
        ActivityControl.addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActivityUntil.initActionBar(toolbar,this,R.drawable.ic_arrow_back);
    }
    private static Toast toast;

    /**
     * @param msg
     * @param duration short or long
     */
    public void showToast(String msg, int duration)
    {
        if (toast == null)
        {
            toast = Toast.makeText(this,msg,duration);
        }else {
            toast.setText(msg);
            toast.setDuration(duration);
        }
        toast.show();
    }

    void hideToast()
    {
        if(toast != null)
        {
            toast.cancel();
            toast = null;
        }
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
    protected void onPause() {
        super.onPause();
        hideToast();
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
