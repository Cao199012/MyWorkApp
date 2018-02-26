package com.caojian.myworkapp.ui.activity;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.fence.FenceAlarmPushInfo;
import com.baidu.trace.api.fence.MonitoredAction;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.bumptech.glide.Glide;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;

import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.services.UpdateLocationService;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.fragment.FriendFragment;
import com.caojian.myworkapp.ui.fragment.FragmentLocation;
import com.caojian.myworkapp.ui.fragment.FenceAlarmFragment;
import com.caojian.myworkapp.ui.fragment.TrackFragment;
import com.caojian.myworkapp.manager.ActivityControl;
import com.caojian.myworkapp.ui.view.CircleImageView;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.MyDialogFragment;
import com.caojian.myworkapp.widget.PersonalInstance;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.caojian.myworkapp.until.ActivityUntil.getActiveNetWork;

public class MainActivity extends BaseTitleActivity {

    public static void go2MainActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,MainActivity.class);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.main_bottom)
    BottomNavigationBar mBottomBar;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.fragment_container)
    FrameLayout mContainer;
    @BindView(R.id.main_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_nav)
    NavigationView navigationView;

    /**
     * 轨迹服务监听器
     */
    private OnTraceListener traceListener = null;

    /**
     * 轨迹监听器(用于接收纠偏后实时位置回调)
     */
    private OnTrackListener trackListener = null;

    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private OnEntityListener entityListener = null;

    private boolean isRealTimeRunning = true;

    private int notifyId = 0;
    /**
     * 打包周期
     */
    public int packInterval = 30;


    private Unbinder unbinder;

    private String[] titles = {"定位","好友","围栏","轨迹"};
    private Fragment[] fragments = {FragmentLocation.newInstance(),FriendFragment.newInstance("",""),
                                    FenceAlarmFragment.newInstance("",""),TrackFragment.newInstance()};
    private Fragment mCurrent_fragment;
    MyApplication trackApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        mToolBar.setTitle(titles[0]);
        trackApp = (MyApplication) getApplication();
        if(PersonalInstance.getInstance().getPersonalMsg() != null ) {
            PersonalMsg.DataBean dataBean = PersonalInstance.getInstance().getPersonalMsg().getData();
            //NavigationView获取Header View的问题
            FrameLayout headerLayout = (FrameLayout) navigationView.inflateHeaderView(R.layout.nav_header);
            CircleImageView mMenuHead = (CircleImageView) headerLayout.findViewById(R.id.menu_head);

            if (dataBean.getHeadPic() != null && !dataBean.getHeadPic().isEmpty()) {
                Glide.with(MainActivity.this).load(Until.HTTP_BASE_IMAGE_URL + dataBean.getHeadPic()).into(mMenuHead);
            }
        }else {
            FrameLayout headerLayout = (FrameLayout) navigationView.inflateHeaderView(R.layout.nav_header);
        }
        initBottom(); //初始化底部N按钮
        //首页显示定位模块
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragments[0]).commit();
        mCurrent_fragment = fragments[0];
        //判断是否联网
        if(getActiveNetWork(MainActivity.this) == -1)
        {
            showToast("请打开网络",Toast.LENGTH_SHORT);
            // TODO: 2017/12/26 跳转到错误页面
        }

        //鹰眼地图开始采集数据
        initListen();
        if(trackApp.mTrace == null){
            trackApp.mTrace = new Trace(158980, ActivityUntil.getPhone(this));
        }
        trackApp.mClient.startTrace(trackApp.mTrace, traceListener); //开启服务
        trackApp.mClient.startGather(traceListener); //开始采集
        trackApp.mClient.setInterval(5, packInterval);
    }

    private void initListen() {
        traceListener = new OnTraceListener() {

            /**
             * 绑定服务回调接口
             * @param errorNo  状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功 </pre>
             *                <pre>1：失败</pre>
             */
            @Override
            public void onBindServiceCallback(int errorNo, String message) {
               // showToast(String.format("onBindServiceCallback, errorNo:%d, message:%s ", errorNo, message),Toast.LENGTH_SHORT);
            }

            /**
             * 开启服务回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功 </pre>
             *                <pre>10000：请求发送失败</pre>
             *                <pre>10001：服务开启失败</pre>
             *                <pre>10002：参数错误</pre>
             *                <pre>10003：网络连接失败</pre>
             *                <pre>10004：网络未开启</pre>
             *                <pre>10005：服务正在开启</pre>
             *                <pre>10006：服务已开启</pre>
             */
            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.putBoolean("is_trace_started", true);
                    editor.apply();
                }
               // showToast(String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message),Toast.LENGTH_SHORT);
            }

            /**
             * 停止服务回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>11000：请求发送失败</pre>
             *                <pre>11001：服务停止失败</pre>
             *                <pre>11002：服务未开启</pre>
             *                <pre>11003：服务正在停止</pre>
             */
            @Override
            public void onStopTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.CACHE_TRACK_NOT_UPLOAD == errorNo) {
                    // 停止成功后，直接移除is_trace_started记录（便于区分用户没有停止服务，直接杀死进程的情况）
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.remove("is_trace_started");
                    editor.remove("is_gather_started");
                    editor.apply();
                }
               // showToast(String.format("onStopTraceCallback, errorNo:%d, message:%s ", errorNo, message),Toast.LENGTH_SHORT);
            }

            /**
             * 开启采集回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>12000：请求发送失败</pre>
             *                <pre>12001：采集开启失败</pre>
             *                <pre>12002：服务未开启</pre>
             */
            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.putBoolean("is_gather_started", true);
                    editor.apply();
                }
               // showToast(String.format("onStartGatherCallback, errorNo:%d, message:%s ", errorNo, message),Toast.LENGTH_SHORT);
            }

            /**
             * 停止采集回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>13000：请求发送失败</pre>
             *                <pre>13001：采集停止失败</pre>
             *                <pre>13002：服务未开启</pre>
             */
            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.remove("is_gather_started");
                    editor.apply();
                }
               // showToast(String.format("onStopGatherCallback, errorNo:%d, message:%s ", errorNo, message),Toast.LENGTH_SHORT);
            }

            /**
             * 推送消息回调接口
             *
             * @param messageType 状态码
             * @param pushMessage 消息
             *                  <p>
             *                  <pre>0x01：配置下发</pre>
             *                  <pre>0x02：语音消息</pre>
             *                  <pre>0x03：服务端围栏报警消息</pre>
             *                  <pre>0x04：本地围栏报警消息</pre>
             *                  <pre>0x05~0x40：系统预留</pre>
             *                  <pre>0x41~0xFF：开发者自定义</pre>
             */
            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {
                if (messageType < 0x03 || messageType > 0x04) {
                    showToast( pushMessage.getMessage(),Toast.LENGTH_SHORT);
                    return;
                }
                FenceAlarmPushInfo alarmPushInfo = pushMessage.getFenceAlarmPushInfo();
                if (null == alarmPushInfo) {
                    showToast(String.format("onPushCallback, messageType:%d, messageContent:%s ", messageType, pushMessage),Toast.LENGTH_LONG);
                    return;
                }
            }

            @Override
            public void onInitBOSCallback(int errorNo, String message) {
                showToast(String.format("onInitBOSCallback, errorNo:%d, message:%s "+errorNo,message),Toast.LENGTH_LONG);
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.logo_launcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 设置指第三方的bottom bar
     */
    private void initBottom() {
        mBottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .addItem(new BottomNavigationItem(R.drawable.ic_pin_drop_white_24dp,"定位"))
                .addItem(new BottomNavigationItem(R.drawable.ic_people_outline_black_24dp,"好友"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_book,"围栏"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_share,"轨迹"))
                .setFirstSelectedPosition(0)
                .setTabSelectedListener(new BottomSelect())
                .initialise();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.personal:
                        PersonalActivity.go2PersonalActivity(MainActivity.this);
                        return true;
                    case R.id.jifen:
                        IntegralActivity.go2IntegralActivity(MainActivity.this);
                        return true;
                    case R.id.app:
                        AboutAppActivity.go2AboutAPPActivity(MainActivity.this);
                        return true;
                    case R.id.share:

                        shareApp();
                        //跳转分享
                        //  AboutAppActivity.go2AboutAPPActivity(MainActivity.this);
                        return true;
                }
                return true;

            }
        });
    }


    //退出登录（清除缓存）
    public void outLogin(View view)
    {
        outLogin("确定要退出当前账号");
    }

    //监听底部tab键，选择操作
    private class  BottomSelect implements BottomNavigationBar.OnTabSelectedListener {

        @Override
        public void onTabSelected(int position) {
            mToolBar.setTitle(titles[position]);
            switchFragment(fragments[position]);

        }
        @Override
        public void onTabUnselected(int position) {

        }

        @Override
        public void onTabReselected(int position) {

        }
    }

    //跳转到指定的模块
    private void switchFragment(Fragment to)
    {
        if(to != mCurrent_fragment)
        {
            getSupportFragmentManager().beginTransaction().hide(mCurrent_fragment).commit();
            if(to.isAdded())
            {
                getSupportFragmentManager().beginTransaction().show(to).commit();
            }else
            {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,to).commit();
            }
            mCurrent_fragment = to;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START,true);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LocationDetailActivity.REQUEST_CODE:
            case FriendSelectActivity.SELECT_MAIN:
                fragments[0].onActivityResult(requestCode, resultCode, data);
                break;
            case FriendDetailActivity.REQUESTCODE:
                fragments[1].onActivityResult(requestCode, resultCode, data);
                break;
            case RailSelectActivity.FROM_TRACK:
                fragments[3].onActivityResult(requestCode, resultCode, data);
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
               fragments[0].onRequestPermissionsResult(requestCode,permissions,grantResults);
                break;
            default:
        }
    }
    private int back_time = 0;
    private CountDownTimer downTimer = new CountDownTimer(1000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            back_time--;
        }
    };
    @Override
    public void onBackPressed() {
        if(back_time == 0)
        {
            back_time++;
            showToast("再按一次退出应用", Toast.LENGTH_LONG);
            downTimer.start();
            return;
        }
        if(back_time == 1){
            finish();
            ActivityControl.finishActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        //stop();
    }

}
