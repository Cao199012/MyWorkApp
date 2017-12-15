package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.TixianContract;
import com.caojian.myworkapp.ui.presenter.TixianPresenter;
import com.caojian.myworkapp.widget.ImageCheckFragment;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TiXianActivity extends MvpBaseActivity<TixianContract.View,TixianPresenter> implements ImageCheckFragment.FragmentImageCheckListener,TixianContract.View{
    public static void go2TiXianActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,TiXianActivity .class);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.get_code)
    Button mBtn_code;
    @BindView(R.id.code_time)
    TextView mTv_time;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.integral_num)
    AppCompatEditText mIntegral_num;
    @BindViews({R.id.card_name,R.id.bank_name,R.id.card_num,R.id.verity_code})
    AppCompatEditText[] mMsgEdits;
    private static final String[] TOAST_MSG = {"请填写户主名","请填写开户行","请填写卡号","请填写验证码"};
    Unbinder unbinder;
    ImageCheckFragment imageCheckFragment;
    TixianPresenter mPresenter;
    private CountDownTimer downTimer = new CountDownTimer(60*1000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
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
        setContentView(R.layout.activity_ti_xian);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("提现");
    }


    @OnClick(R.id.btn_submit)
    public void submitMsg(){
        String num = mIntegral_num.getText().toString().trim();
        if(num.isEmpty()){
            showToast("请输入提现额度",Toast.LENGTH_SHORT);
            return;
        }
        for (int i = 0; i <  mMsgEdits.length; i++){
            if(mMsgEdits[i].getText().toString().trim().isEmpty()){
                showToast(TOAST_MSG[i],Toast.LENGTH_SHORT);
                return;
            }
        }
        mPresenter.submitMsg(Integer.parseInt(num),mMsgEdits[0].getText().toString().trim(),mMsgEdits[1].getText().toString().trim(),
                mMsgEdits[0].getText().toString().trim(),mMsgEdits[0].getText().toString().trim());
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
        imageCheckFragment.show(getSupportFragmentManager(),"img");
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
        mPresenter.verityCode("", code);
    }

    @OnClick(R.id.go_detail)
    public void go2Detail(){
        // TODO: 2017/12/9 查看体现记录
        TiXianDetailActivity.go2TiXianDetailActivity(TiXianActivity.this);
    }
    @Override
    public TixianPresenter createPresenter() {
        mPresenter = new TixianPresenter(TiXianActivity.this,this);
        return mPresenter;
    }

    @Override
    public void submitSuccess() {

    }

    @Override
    public void verityCodeSuccess() {
        downTimer.start();
    }

    @Override
    public void error(String errorMsg) {
        showToast(errorMsg, Toast.LENGTH_SHORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
