package com.caojian.myworkapp.modules.rail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RailSelectActivity extends BaseTitleActivity {
    public static void go2RailSelectActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,RailSelectActivity .class);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rail_select);
        unbinder = ButterKnife.bind(this);

        mToolbar.setTitle("提现");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
