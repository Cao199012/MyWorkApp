package com.caojian.myworkapp.mvp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.caojian.myworkapp.R;

import static android.R.attr.fragment;

public class MyMvpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mvp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mvp_tool);
        toolbar.setTitle("recy界面");
        setSupportActionBar(toolbar);

        //获取fragment中得fragment
        MvpFragment fragment = (MvpFragment) getSupportFragmentManager().findFragmentById(R.id.recy_container);
        if(fragment == null)
        {
            //添加fragment
            getSupportFragmentManager().beginTransaction().add(R.id.recy_container,MvpFragment.getInstance()).commit();
        }

    }
}
