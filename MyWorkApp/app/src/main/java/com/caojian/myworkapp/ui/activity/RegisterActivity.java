package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;

import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.RegisterContract;
import com.caojian.myworkapp.ui.presenter.RegisterPresenter;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.widget.ImageCheckFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends MvpBaseActivity<RegisterContract.View,RegisterPresenter> implements ImageCheckFragment.FragmentImageCheckListener,RegisterContract.View{

    public static void go2RegisterActivity(Context from, String phoneNum)
    {
        Intent intent = new Intent(from,RegisterActivity.class);
        intent.putExtra("phoneNum",phoneNum);
        from.startActivity(intent);
    }
    @BindView(R.id.phone_num)
    EditText mPhoneNumEdit;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.verity_code)
    EditText mEdit_code;
    @BindView(R.id.get_code)
    Button mBtn_code;
    @BindView(R.id.code_time)
    TextView mTv_time;
    @BindView(R.id.register_nickname)
    EditText mNickName;
    ImageCheckFragment imageCheckFragment;
    private Unbinder unbinder;
    private EditText mPasswordView;
    RegisterPresenter mPresenter;
    //倒计时60秒
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
        setContentView(R.layout.activity_register);
        unbinder = ButterKnife.bind(this);
        //设置全屏显示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mToolbar.setTitle("");
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * 获取验证码
     * @param view
     */
    public void verityCode(View view)
    {
        String phoneNum = mPhoneNumEdit.getText().toString().trim();
        if(!ActivityUntil.CheckPhone(phoneNum).equals(""))
        {
            showToast(ActivityUntil.CheckPhone(phoneNum), Toast.LENGTH_SHORT);
            return;
        }
        imageCheckFragment = new ImageCheckFragment();
        imageCheckFragment.setCancelable(false);
       // imageCheckFragment.show(getSupportFragmentManager(),"img");
        ActivityUntil.showDialogFragment(getSupportFragmentManager(),imageCheckFragment,"img");

    }



    private void attemptLogin() {
        String recommendNum = getIntent().getStringExtra("phoneNum");
        String phoneNum = mPhoneNumEdit.getText().toString().trim();
        String nickName = mNickName.getText().toString().trim();
        if(!ActivityUntil.CheckPhone(phoneNum).equals(""))
        {
            showToast(ActivityUntil.CheckPhone(phoneNum), Toast.LENGTH_SHORT);
            return;
        }
        if(nickName.equals(""))
        {
            showToast("昵称不能为空", Toast.LENGTH_SHORT);
            return;
        }
        if(nickName.length() > 15){
            showToast("昵称过长", Toast.LENGTH_SHORT);
            return;
        }
        String password = mPasswordView.getText().toString().trim();
        if(password.isEmpty())
        {
            showToast("请设置登录密码", Toast.LENGTH_SHORT);
            return;
        }
        if(!ActivityUntil.checkPassword(password)){
            showToast("密码不符合规则",Toast.LENGTH_SHORT);
            return;
        }
        String code = mEdit_code.getText().toString().trim();
        if(code.isEmpty())
        {
            showToast("请输入验证码", Toast.LENGTH_SHORT);
            return;
        }
        mPresenter.checkRegister(phoneNum,code,ActivityUntil.getStringMD5(password),recommendNum,nickName);
    }

    @Override
    public RegisterPresenter createPresenter() {
        mPresenter = new RegisterPresenter(RegisterActivity.this,this);
        return mPresenter;
    }
    //取消图形验证码
    @Override
    public void cancelCheck() {
        imageCheckFragment.setCancelable(true);
        imageCheckFragment.dismiss();
        imageCheckFragment = null;
    }
    //提交图形验证码，获取短信验证码
    @Override
    public void submitCheck(String code) {
        imageCheckFragment.setCancelable(true);
        imageCheckFragment.dismiss();
        imageCheckFragment = null;
        mPresenter.verityCode(mPhoneNumEdit.getText().toString().trim(),code);
    }

    //注册成功
    @Override
    public void registerSuccess() {
        ActivityUntil.savePhone(RegisterActivity.this,mPhoneNumEdit.getText().toString().trim());
        SplashActivity.go2SplashActivity(RegisterActivity.this);
        finish();
    }

    //获取验证码成功
    @Override
    public void verityCodeSuccess() {
        mBtn_code.setVisibility(View.GONE);
        mTv_time.setVisibility(View.VISIBLE);
        mDownTimer.start();
        showToast("验证码已发送",Toast.LENGTH_SHORT);
    }
    //注册失败提示失败信息
    @Override
    public void registerError(String errorMsg) {
        showToast(errorMsg,Toast.LENGTH_SHORT);
        clearMsg();
    }

    private void clearMsg() {
        mDownTimer.onFinish();
        mEdit_code.setText("");

    }

    @Override
    protected void onPause() {
        super.onPause();
        //界面消失 取消倒计时更新
        mDownTimer.onFinish();
        mDownTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mDownTimer.cancel();
    }
}

