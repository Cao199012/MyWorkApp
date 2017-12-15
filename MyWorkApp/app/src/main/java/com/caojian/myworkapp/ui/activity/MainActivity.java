package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.caojian.myworkapp.R;

import com.caojian.myworkapp.services.UpdateLocationService;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.fragment.FriendFragment;
import com.caojian.myworkapp.ui.fragment.FragmentLocation;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.widget.MyDialogFragment;
import com.caojian.myworkapp.ui.fragment.RailFragment;
import com.caojian.myworkapp.ui.fragment.TrackFragment;
import com.caojian.myworkapp.manager.ActivityControl;


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
    private Unbinder unbinder;

    private String[] titles = {"定位","好友","围栏报警","轨迹"};
    private Fragment[] fragments = {FragmentLocation.newInstance(),FriendFragment.newInstance("",""),
                                    RailFragment.newInstance("",""),TrackFragment.newInstance()};
    private Fragment mCurrent_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        mToolBar.setTitle(titles[0]);
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
                }
                return true;

            }
        });
        initBottom();
        //首页显示定位模块
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragments[0]).commit();
        mCurrent_fragment = fragments[0];
        //判断是否联网
        if(getActiveNetWork(MainActivity.this) == -1)
        {
            showToast("请打开网络",Toast.LENGTH_SHORT);
        }

        UpdateLocationService.startActionBaz(MainActivity.this,"AAA","BBB");
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    /**
     * 设置指第三方的bottom bar
     */
    private void initBottom() {
        mBottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT)
                .addItem(new BottomNavigationItem(R.mipmap.ic_home,"定位"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_home,"好友"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_home,"围栏报警"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_home,"查看轨迹"))
                .setFirstSelectedPosition(0)
                .setTabSelectedListener(new BottomSelect())
                .initialise();
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
            case 102:
                fragments[0].onActivityResult(requestCode, resultCode, data);
                break;
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
            showToast("再按一次推出应用", Toast.LENGTH_LONG);
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
