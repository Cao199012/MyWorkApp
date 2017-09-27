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
import butterknife.OnClick;
import butterknife.Unbinder;

public class IntegralActivity extends BaseTitleActivity {
    public static void go2IntegralActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,IntegralActivity.class);
        fromClass.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);
        unbinder = ButterKnife.bind(this);

        mToolbar.setTitle("积分");
    }

    @OnClick(R.id.go_detail)
    public void go2Detail()
    {
        IntegralDetailActivity.go2IntegralDetailActivity(IntegralActivity.this);
    }
    @OnClick(R.id.go_tixian)
    public void go2Tixian()
    {
        TiXianActivity.go2TiXianActivity(IntegralActivity.this);
    }
    @OnClick(R.id.go_share)
    public void go2Share()
    {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
