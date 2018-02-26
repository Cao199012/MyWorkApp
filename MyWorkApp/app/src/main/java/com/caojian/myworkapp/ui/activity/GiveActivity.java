package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.TixianContract;
import com.caojian.myworkapp.ui.presenter.TixianPresenter;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.widget.ImageCheckFragment;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GiveActivity extends MvpBaseActivity<TixianContract.View,TixianPresenter> implements ImageCheckFragment.FragmentImageCheckListener,TixianContract.View{
    public static void go2GiveActivity(Context fromClass )
    {
        Intent intent = new Intent(fromClass,GiveActivity .class);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.get_code)
    Button mBtn_code;
    @BindView(R.id.code_time)
    TextView mTv_time;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindViews({R.id.give_num,R.id.give_phone,R.id.verity_code})
    AppCompatEditText[] mMsgEdits;
    private static final String[] TOAST_MSG = {"请填准增积分数","请填用户手机号","请填写验证码"};
    Unbinder unbinder;
    ImageCheckFragment imageCheckFragment;
    TixianPresenter mPresenter;
    private CountDownTimer mDownTimer = new CountDownTimer(60*1000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mTv_time.setVisibility(View.VISIBLE);
            mBtn_code.setVisibility(View.GONE);
            mTv_time.setText(millisUntilFinished/1000+"秒");
        }
        @Override
        public void onFinish() {
            mTv_time.setText("");
            mTv_time.setVisibility(View.GONE);
            mBtn_code.setVisibility(View.VISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("积分转赠");
    }

    @OnClick(R.id.btn_submit)
    public void submitMsg(){
        for (int i = 0; i <  mMsgEdits.length; i++){
            if(mMsgEdits[i].getText().toString().trim().isEmpty()){
                showToast(TOAST_MSG[i],Toast.LENGTH_SHORT);
                return;
            }
        }
        mPresenter.submitGiveMsg(mMsgEdits[0].getText().toString().trim(),mMsgEdits[1].getText().toString().trim(),mMsgEdits[2].getText().toString().trim());
    }
    /**
     * 获取验证码
     * @param view
     */
    public void verityCode(View view)
    {
        if(imageCheckFragment == null)
        {
            imageCheckFragment = ImageCheckFragment.newInstance();
        }
        ActivityUntil.showDialogFragment(getSupportFragmentManager(),imageCheckFragment,"img");

    }

    //取消图形验证码
    @Override
    public void cancelCheck() {
        imageCheckFragment.setCancelable(true);
        imageCheckFragment.dismiss();
    }
    //提交图形验证码，获取短信验证码
    @Override
    public void submitCheck(String code) {
        imageCheckFragment.setCancelable(true);
        imageCheckFragment.dismiss();
        mPresenter.verityCode(ActivityUntil.getPhone(GiveActivity.this), code,3);
    }

    @OnClick(R.id.go_detail)
    public void go2Detail(){
        // TODO: 2017/12/9 查看体现记录
        IntegralDetailActivity.go2IntegralDetailActivity(GiveActivity.this);
    }
    @Override
    public TixianPresenter createPresenter() {
        mPresenter = new TixianPresenter(GiveActivity.this,this);
        return mPresenter;
    }

    //向后台提交成功
    @Override
    public void submitSuccess() {
        showToast("提交成功",Toast.LENGTH_SHORT);
        IntegralDetailActivity.go2IntegralDetailActivity(GiveActivity.this);
     //   ActivityControl.removeActivity(IntegralActivity.class);
        finish();
    }

    @Override
    public void verityCodeSuccess() {
        mDownTimer.start();
    }

    @Override
    public void error(String errorMsg) {
        showToast(errorMsg, Toast.LENGTH_SHORT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDownTimer.onFinish();
        mDownTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
