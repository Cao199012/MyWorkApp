package com.caojian.myworkapp.ui.base;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.ActivityControl;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.model.response.UpdateResponse;
import com.caojian.myworkapp.ui.activity.BuyVipActivity;
import com.caojian.myworkapp.ui.activity.FriendDetailActivity;
import com.caojian.myworkapp.ui.activity.LoginActivity;
import com.caojian.myworkapp.ui.activity.MainActivity;
import com.caojian.myworkapp.ui.activity.RailSelectActivity;
import com.caojian.myworkapp.ui.activity.SplashActivity;
import com.caojian.myworkapp.ui.activity.UpdateActivity;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.MyDialogFragment;
import com.caojian.myworkapp.widget.MyNewDialogFragment;
import com.caojian.myworkapp.widget.PersonalInstance;

import java.util.Date;
import java.util.Timer;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.caojian.myworkapp.until.Until.DOWNLOAD_BASE_URL;

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
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
        if(dialog != null){
            dialog.cancel();
            dialog = null;
        }
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

    ProgressDialog dialog = null;
    public void showProgress(Context context)
    {
        if(dialog == null)
        {
            dialog = new ProgressDialog(context);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("链接中...");
        if(dialog.isShowing()) return;
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
        mBaseTime = ((hour < 10)?"0":"")+hour+":"+((minute < 10)?"0":"")+minute;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, null,hour,minute,true){
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mBaseTime = ((hourOfDay < 10)?"0":"")+hourOfDay+":"+((minute < 10)?"0":"")+minute;
                super.onTimeChanged(view, hourOfDay, minute);
            }
        };
        return timePickerDialog;
    }
    protected DatePickerDialog initDateDialog() {
        Time date = new Time();
        date.setToNow(); // 取得系统时间。
        int year = date.year;
        int month = date.month;
        int day = date.monthDay;
        mBaseDate = year+":"+((month < 10)?"0":"")+month+":"+((day < 10)?"0":"")+day;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mBaseDate = year+":"+((month < 10)?"0":"")+month+":"+((day < 10)?"0":"")+day;
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
        if(mDialogFragment.isVisible() || mDialogFragment.isAdded()) return;
        mDialogFragment.setCancelable(false);
        //MyNewDialogFragment.showNewDialog(getSupportFragmentManager(),mDialogFragment,"out");

        ActivityUntil.showDialogFragment(getSupportFragmentManager(),mDialogFragment,"out");

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
        ActivityUntil.clearToken(this);
        ActivityUntil.clearRailMsg(this);
        ActivityControl.finishActivity();
    }

    @Override
    public void newSure() {
        if(mDialogFragment == null)
        {
            return;
        }
        ActivityUntil.clearToken(this);
        ActivityUntil.clearRailMsg(this);
        ActivityControl.finishActivity();
        if(mDialogFragment.getTag().equals("out"))
        {

            LoginActivity.go2LoginActivity(this);
        }
        mDialogFragment.setCancelable(true);
        mDialogFragment.dismiss();
        mDialogFragment = null;

    }
  protected void shareApp() //分享APP
  {
      Intent shareIntent = new Intent();
      shareIntent.setAction(Intent.ACTION_SEND);
      if(PersonalInstance.getInstance().getPersonalMsg() != null ) {
          PersonalMsg.DataBean dataBean = PersonalInstance.getInstance().getPersonalMsg().getData();
          shareIntent.putExtra(Intent.EXTRA_TEXT, "你好，我是\""+dataBean.getNickName()+"\"，我正在使用一款类似于微信好友定位的APP，相比微信增加了电子围栏和轨迹察看等功能，可以让我们随时关爱家人和好友。\n" +
                  "请点击查看:一分钟了解在哪儿APP:http://h5.ppj.io/cB7FRmKi/index.html?。\n" +
                  "下载地址"+DOWNLOAD_BASE_URL+System.currentTimeMillis()+"\n推荐码请填写:"+dataBean.getInvitationCode());
      }
      shareIntent.setType("text/plain");
      startActivity(shareIntent);
  }
   public void showBuyVip()
   {
       MyDialogFragment mDialogFragment = MyDialogFragment.newInstance("成为会员",Until.DILOG_MSG_MIAN,"暂不使用","立即开通");
       mDialogFragment.setCancelable(false);
       mDialogFragment.setListener(new MyDialogFragment.FragmentDialogListener() {
           @Override
           public void cancel() {
               finish();
               ActivityControl.finishActivity();
           }
           @Override
           public void sure() {
               mDialogFragment.setCancelable(true);
               mDialogFragment.dismiss();
               BuyVipActivity.go2BuyVipActivity(BaseTitleActivity.this,0);
           }
       });
       //mDialogFragment.showDialog(getSupportFragmentManager(),mDialogFragment,"out");
       ActivityUntil.showDialogFragment(getSupportFragmentManager(),mDialogFragment,"buy");
   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除关闭的Activity
        ActivityControl.removeActivity(this);

    }

}
