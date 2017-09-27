package com.caojian.myworkapp;

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
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.caojian.myworkapp.base.BaseActivity;
import com.caojian.myworkapp.modules.buy.BuyVipActivity;
import com.caojian.myworkapp.modules.friend.FriendFragment;
import com.caojian.myworkapp.modules.integral.IntegralActivity;
import com.caojian.myworkapp.modules.location.FragmentLocation;
import com.caojian.myworkapp.modules.personal.PersonalActivity;
import com.caojian.myworkapp.view.MyDailogFragment;
import com.caojian.myworkapp.modules.rail.RailFragment;
import com.caojian.myworkapp.modules.track.TrackFragment;
import com.caojian.myworkapp.until.ActivityControl;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.caojian.myworkapp.until.ActivityUntil.isActiveNetWork;

public class MainActivity extends BaseActivity implements FriendFragment.OnFragmentInteractionListener
,MyDailogFragment.FragmentDialogListener{

    public static void go2MainActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,MainActivity.class);
       // intent.pu
        fromClass.startActivity(intent);
    }
    @BindView(R.id.main_bottom)
    BottomNavigationBar mBottomBar;
    @BindView(R.id.main_toolbar)
    Toolbar mToolBar;
    @BindView(R.id.fragment_container)
    FrameLayout mContainer;
    @BindView(R.id.main_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_nav)
    NavigationView navigationView;
    private Unbinder unbinder;

    private String[] titles = {"定位","好友","围栏报警","轨迹"};
    private Fragment[] fragments = {new FragmentLocation().newIntance(),new FriendFragment().newInstance("",""),
                                    new RailFragment().newInstance("",""),new TrackFragment().newInstance()};
    private Fragment mCurrent_fragment;
    private MyDailogFragment mDailogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        mToolBar.setTitle(titles[0]);
        ActivityUntil.initActionBar(mToolBar,MainActivity.this,R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.personal:
                            PersonalActivity.go2PersonalActivity(MainActivity.this);
                            return true;
                        case R.id.jifen:
                            IntegralActivity.go2IntegralActivity(MainActivity.this);
                    }
                            return true;

            }
        });
      //  initToolBar();
        initBottom();
        initFragment();
        Log.i("HelloWorldActivity","______________onCreate execute______________");
        //判断是否联网
        isActiveNetWork(MainActivity.this);
        //调用显示弹出框
//        mDailogFragment = MyDailogFragment.newInstance("测试","asldfalaa;am;sla;;vldv");
//        mDailogFragment.show(getSupportFragmentManager(),"dialog");
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
    private void initFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragments[0]).commit();
        mCurrent_fragment = fragments[0];
    }

    //fragment 调用   Activity
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    //弹出框（mydialogFragment）的接口调用。
    @Override
    public void cancel() {

        if(mDailogFragment != null)
        {
            mDailogFragment.setCancelable(true);
            mDailogFragment.dismiss();
        }
    }

    //
    @Override
    public void sure() {

        if(mDailogFragment != null)
        {
            mDailogFragment.setCancelable(true);
            mDailogFragment.dismiss();
            BuyVipActivity.go2BuyVipActivity(MainActivity.this);
        }
    }
    //供fragment调用
    public void showDialogFragment(String title,String comments,String cancel,String sure)
    {
        //mDailogFragment = MyDailogFragment.newInstance("暂无权限","此功能需要付费购买","去购买","暂不使用");
        mDailogFragment = MyDailogFragment.newInstance(title,comments,cancel,sure);
        mDailogFragment.setCancelable(false);
        mDailogFragment.show(getSupportFragmentManager(),"dialog");
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
        if(requestCode == 102 && resultCode == RESULT_OK) {
            int num = data.getIntExtra("num", 0);
            ((FragmentLocation)fragments[0]).handleResult(num);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
            ActivityUntil.showToast(MainActivity.this,"再按一次推出应用", Toast.LENGTH_LONG);
            downTimer.start();
            return;
        }
        if(back_time == 1){
            finish();
            ActivityControl.finishActivity();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityUntil.hideToast();
    }
}
