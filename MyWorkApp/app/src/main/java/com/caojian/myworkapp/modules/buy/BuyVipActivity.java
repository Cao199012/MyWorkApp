package com.caojian.myworkapp.modules.buy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BaseTitleActivity;
import com.caojian.myworkapp.view.BuyModelFragment;
import com.caojian.myworkapp.view.ListDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BuyVipActivity extends BaseTitleActivity implements ListDialogFragment.FragmentBuyListener {

    public static void go2BuyVipActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,BuyVipActivity.class);
        fromClass.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_vip);

        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("会员中心");

    }
    ListDialogFragment fragment;
    public void buy(View v){
        fragment = ListDialogFragment.newInstance("购买会员");
        fragment.show(getSupportFragmentManager(),"list");
        fragment.setmListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public void cancelBuy() {

    }

    @Override
    public void showBuy(VipItem item) {
        fragment.dismiss();
        BuyModelFragment modelFragment = BuyModelFragment.newInstance();
        modelFragment.show(getSupportFragmentManager(),"model");
    }
}
