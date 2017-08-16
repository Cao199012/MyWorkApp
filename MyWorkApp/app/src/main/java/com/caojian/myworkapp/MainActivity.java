package com.caojian.myworkapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.caojian.myworkapp.base.BaseActivity;
import com.caojian.myworkapp.friend.ItemFragment;
import com.caojian.myworkapp.friend.dummy.DummyContent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity implements ItemFragment.OnListFragmentInteractionListener {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        initToolBar();
        initBottom();
       // initFragment();
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
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new ItemFragment()).commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    //监听底部tab键，选择操作
    private class  BottomSelect implements BottomNavigationBar.OnTabSelectedListener {

        @Override
        public void onTabSelected(int position) {
            mToolBar.setTitle(titles[position]);
        }

        @Override
        public void onTabUnselected(int position) {

        }

        @Override
        public void onTabReselected(int position) {

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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
