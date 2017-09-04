package com.caojian.myworkapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.caojian.myworkapp.base.BaseActivity;
import com.caojian.myworkapp.friend.FriendFragment;
import com.caojian.myworkapp.friend.ItemFragment;
import com.caojian.myworkapp.friend.dummy.DummyContent;
import com.caojian.myworkapp.location.FragmentLocation;
import com.caojian.myworkapp.location.LocationContract;
import com.caojian.myworkapp.rail.RailFragment;
import com.caojian.myworkapp.track.TrackFragment;
import com.caojian.myworkapp.until.ActivityControler;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.caojian.myworkapp.until.ActivityUntil.isActiveNetWork;

public class MainActivity extends BaseActivity implements ItemFragment.OnListFragmentInteractionListener ,FriendFragment.OnFragmentInteractionListener {

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
    private Unbinder unbinder;

    private String[] titles = {"定位","好友","围栏报警","轨迹"};
    private Fragment[] fragments = {new FragmentLocation().newIntance(),new FriendFragment().newInstance("",""),
                                    new RailFragment().newInstance("",""),new TrackFragment().newInstance("","")};
    private Fragment mCurrent_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        initToolBar();
        initBottom();
        initFragment();
        Log.i("HelloWorldActivity","______________onCreate execute______________");
        isActiveNetWork(MainActivity.this);
    }

    private void initToolBar() {
        mToolBar.setTitle(titles[0]);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
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

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

        if(item.getItemId() == android.R.id.home)
        {
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
            ActivityControler.finishActivity();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityUntil.hideToast();
    }
}
