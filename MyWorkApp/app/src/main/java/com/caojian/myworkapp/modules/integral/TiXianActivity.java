package com.caojian.myworkapp.modules.integral;

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

public class TiXianActivity extends BaseTitleActivity {
    public static void go2TiXianActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,TiXianActivity .class);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ti_xian);

        unbinder = ButterKnife.bind(this);

        mToolbar.setTitle("提现");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
