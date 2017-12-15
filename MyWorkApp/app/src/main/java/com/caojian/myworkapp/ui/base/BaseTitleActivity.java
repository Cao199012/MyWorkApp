package com.caojian.myworkapp.ui.base;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.manager.ActivityControl;
import com.caojian.myworkapp.ui.activity.BuyVipActivity;
import com.caojian.myworkapp.ui.activity.FriendDetailActivity;
import com.caojian.myworkapp.ui.activity.LoginActivity;
import com.caojian.myworkapp.ui.activity.MainActivity;
import com.caojian.myworkapp.ui.activity.RailSelectActivity;
import com.caojian.myworkapp.ui.activity.UpdateActivity;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.widget.MyDialogFragment;
import com.caojian.myworkapp.widget.MyNewDialogFragment;

import java.util.Date;

/**
 * Created by caojian on 2017/7/19.
 */

public class BaseTitleActivity extends AppCompatActivity implements MyNewDialogFragment.FragmentDialogListener{

    protected  String mBaseTime;
    protected  String mBaseDate;
    private MyBroadcast mBroadcast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //向control list 添加打开的Activity
        ActivityControl.addActivity(this);
        mBroadcast = new MyBroadcast();
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
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("com.caojian.myworkapp.update");
        registerReceiver(mBroadcast,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideToast();
        unregisterReceiver(mBroadcast);
    }

    private class MyBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: 2017/12/7 进入升级下载界面
            UpdateActivity.go2UpdateActivity(BaseTitleActivity.this,null,101);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除关闭的Activity
        ActivityControl.removeActivity(this);
    }
    ProgressDialog dialog = null;
    public void showProgress(Context context)
    {
        if(dialog == null)
        {
            dialog = new ProgressDialog(context);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("链接中...");
        dialog.show();
    }

    public void hideProgress()
    {
        if(dialog != null){
            dialog.cancel();
            dialog = null;
        }
    }

    protected TimePickerDialog initTimeDialog() {
        Date date = new Date();
        int hour = date.getHours();
        int minute = date.getMinutes();
        mBaseTime = hour+":"+((minute < 10)?"0":"")+minute;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, null,hour,minute,true){
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mBaseTime = hourOfDay+":"+((minute < 10)?"0":"")+minute;
                super.onTimeChanged(view, hourOfDay, minute);
            }
        };
        return timePickerDialog;
    }
    protected DatePickerDialog initDateDialog() {
        Date date = new Date();
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();
        mBaseDate = year+":"+month+":"+day;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mBaseDate = year+":"+month+":"+day;
            }
        },year,month,day);
        return datePickerDialog;
    }
    private MyNewDialogFragment mDialogFragment;

    //退出登录
    public void outLogin(String msg)
    {
        if(mDialogFragment == null){
            mDialogFragment = MyNewDialogFragment.newInstance("退出登录",msg,"取消","确定");
        }
        if(mDialogFragment.isVisible()) return;
        mDialogFragment.setCancelable(false);
        mDialogFragment.show(getSupportFragmentManager(),"out");

    }
    //弹出框（mydialogFragment）的接口调用。
    @Override
    public void newCancel() {
        if(mDialogFragment != null)
        {
            mDialogFragment.setCancelable(true);
            mDialogFragment.dismiss();
            mDialogFragment = null;
        }
    }

    @Override
    public void newSure() {
        if(mDialogFragment == null)
        {
            return;
        }
        if(mDialogFragment.getTag().equals("out"))
        {
            ActivityUntil.clearToken(this);
            ActivityControl.finishActivity();
            LoginActivity.go2LoginActivity(this);
        }
        mDialogFragment.setCancelable(true);
        mDialogFragment.dismiss();
        mDialogFragment = null;

    }
}
