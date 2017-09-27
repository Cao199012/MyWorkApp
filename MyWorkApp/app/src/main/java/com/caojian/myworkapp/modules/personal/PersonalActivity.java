package com.caojian.myworkapp.modules.personal;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BaseTitleActivity;
import com.caojian.myworkapp.modules.buy.BuyVipActivity;
import com.caojian.myworkapp.view.ChangeEditFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PersonalActivity extends BaseTitleActivity implements ChangeEditFragment.FragmentChangeListener{

    public static void go2PersonalActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,PersonalActivity.class);
        fromClass.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    Unbinder unbinder;
    ChangeEditFragment changeEditFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("个人信息");

        changeEditFragment = ChangeEditFragment.newInstance("修改个人信息","不能超过20个字","取消","确定");
    }


    //点击修改个人信息，跳出修改弹出框
    @OnClick(R.id.body_name)
    public void showChangeName()
    {
        changeEditFragment.show(getSupportFragmentManager(),"name");
    }
    @OnClick(R.id.body_signature)
    public void showChangSignature()
    {
        changeEditFragment.show(getSupportFragmentManager(),"name");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void cancelEdit() {
        changeEditFragment.dismiss();
    }

    @Override
    public void sumbitEdit() {
        // TODO: 2017/9/24
    }
}
