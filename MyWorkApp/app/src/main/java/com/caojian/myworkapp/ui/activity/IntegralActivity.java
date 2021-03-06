package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.PersonalContract;
import com.caojian.myworkapp.ui.presenter.PersonalPresenter;
import com.caojian.myworkapp.widget.PersonalInstance;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class IntegralActivity extends MvpBaseActivity<PersonalContract.View,PersonalPresenter> implements PersonalContract.View {
    public static void go2IntegralActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,IntegralActivity.class);
        fromClass.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.integral_num)
    TextView mTv_integral;
    Unbinder unbinder;
    PersonalPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("积分");
//        PersonalMsg personalMsg = PersonalInstance.getInstance().getPersonalMsg();
//        if(personalMsg != null){
//            mTv_integral.setText(personalMsg.getData().getRewardScore()+"");
//        }else {
//            mPresenter.getPersonalInfo();
//        }

    }


    @Override
    protected void onResume() {
        //每次进入更新信息
        mPresenter.getPersonalInfo();
        super.onResume();
    }

    //进入详情
    @OnClick(R.id.go_detail)
    public void go2Detail()
    {
        IntegralDetailActivity.go2IntegralDetailActivity(IntegralActivity.this);
    }

    //
    @OnClick(R.id.go_tixian)
    public void go2Tixian()
    {
        TiXianActivity.go2TiXianActivity(IntegralActivity.this);
    }


    //转赠好友积分
    @OnClick(R.id.go_give)
    public void goGive(){
        GiveActivity.go2GiveActivity(IntegralActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public PersonalPresenter createPresenter() {
        mPresenter = new PersonalPresenter(IntegralActivity.this,this);
        return mPresenter;
    }

    @Override
    public void getPersonalSuccess(PersonalMsg personalMsg) {
        double rewardScore = personalMsg.getData().getRewardScore();
        String score = "";
//        if(rewardScore >= 1000){
//            score = (int)(rewardScore/1000)+","+rewardScore%1000;
//        }else {
//            score = rewardScore + "";
//        }
        mTv_integral.setText(rewardScore+"");
    }

    @Override
    public void changeMsgSuccess(String msg) {

    }

    @Override
    public void error(String errorMsg) {
        showToast(errorMsg, Toast.LENGTH_SHORT);
    }
}
