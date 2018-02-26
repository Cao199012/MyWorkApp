package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;

import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.PasswordContract;
import com.caojian.myworkapp.ui.presenter.PasswordPresenter;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.widget.ImageCheckFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PasswordActivity extends MvpBaseActivity<PasswordContract.View,PasswordPresenter>implements PasswordContract.View ,ImageCheckFragment.FragmentImageCheckListener{

    public static void go2PasswordActivity(Context from, String phoneNum)
    {
        Intent intent = new Intent(from,PasswordActivity.class);
        intent.putExtra("phoneNum",phoneNum);
        from.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_code)
    Button mBtn_code;
    @BindView(R.id.code_time)
    TextView mTv_time;
    @BindView(R.id.tv_newpassword)
    EditText mTv_password;
    @BindView(R.id.edit_verity)
    EditText mEdit_verity;
    private Unbinder unbinder;
    PasswordPresenter mPresenter;
    ImageCheckFragment imageCheckFragment;
    String mPhoneNum;
    private CountDownTimer mDownTimer = new CountDownTimer(60*1000,1000) {
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
        setContentView(R.layout.activity_password);
        unbinder = ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mToolbar.setTitle("");
        //获取传入的号码
        mPhoneNum = getIntent().getStringExtra("phoneNum");
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
      //  imageCheckFragment.show(getSupportFragmentManager(),"img");
        ActivityUntil.showDialogFragment(getSupportFragmentManager(),imageCheckFragment,"img");

    }
    /**
     * 提交按钮调用
     * @param v
     */
    public void submitPassword(View v)
    {
        // TODO: 2017/8/21 提交新密码
        String password = mTv_password.getText().toString().trim();
        String code = mEdit_verity.getText().toString().trim();
        if(password.isEmpty())
        {
            showToast("新密码不能为空",Toast.LENGTH_SHORT);
            return;
        }
        if(!ActivityUntil.checkPassword(password)){
            showToast("密码不符合规则",Toast.LENGTH_SHORT);
            return;
        }
        if(code.isEmpty())
        {
            showToast("请输入验证码",Toast.LENGTH_SHORT);
            return;
        }
        mPresenter.checkPassword(mPhoneNum,code,ActivityUntil.getStringMD5(password));
    }
    @Override
    public PasswordPresenter createPresenter() {
        mPresenter = new PasswordPresenter(PasswordActivity.this,this);
        return mPresenter;
    }
    //获取验证码成功
    @Override
    public void verityCodeSuccess() {
        //倒计时
        mBtn_code.setVisibility(View.GONE);
        mTv_time.setVisibility(View.VISIBLE);
        mDownTimer.start();
        showToast("验证码已发送",Toast.LENGTH_SHORT);
    }
    //修改密码成功 进入首页
    @Override
    public void changePasswordSuccess() {
        ActivityUntil.savePhone(PasswordActivity.this,mPhoneNum);
        SplashActivity.go2SplashActivity(PasswordActivity.this);
        finish();
    }
    //修改密码失败，弹出框提示信息
    @Override
    public void requestError(String errorMsg) {
        showToast(errorMsg,Toast.LENGTH_SHORT);
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
        mPresenter.verityCode(mPhoneNum, code);
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
        if (unbinder != null)
        {
            unbinder.unbind();
        }
        mDownTimer.cancel();
    }
}
