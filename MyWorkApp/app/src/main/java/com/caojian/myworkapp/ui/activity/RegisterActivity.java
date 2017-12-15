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
import com.caojian.myworkapp.api.MyApi;

import com.caojian.myworkapp.model.response.VerityCodeMsg;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.RegisterContract;
import com.caojian.myworkapp.ui.presenter.RegisterPresenter;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.ImageCheckFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    @BindView(R.id.recommend_num)
    EditText recommend_num;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.verity_code)
    EditText mEdit_code;
    @BindView(R.id.get_code)
    Button mBtn_code;
    @BindView(R.id.code_time)
    TextView mTv_time;
    ImageCheckFragment imageCheckFragment;
    private Unbinder unbinder;
    private EditText mPasswordView;
    RegisterPresenter mPresenter;
    //倒计时60秒
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
        String recommendNum = recommend_num.getText().toString().trim();
        if(!ActivityUntil.CheckPhone(recommendNum).equals(""))
        {
            showToast(ActivityUntil.CheckPhone(recommendNum), Toast.LENGTH_SHORT);
            return;
        }
        if(imageCheckFragment == null)
        {
            imageCheckFragment = new ImageCheckFragment();
        }
        imageCheckFragment.setCancelable(false);
        imageCheckFragment.show(getSupportFragmentManager(),"img");
    }



    private void attemptLogin() {
        String recommendNum = getIntent().getStringExtra("phoneNum");
        String phoneNum = recommend_num.getText().toString().trim();
        if(!ActivityUntil.CheckPhone(recommendNum).equals(""))
        {
            showToast(ActivityUntil.CheckPhone(recommendNum), Toast.LENGTH_SHORT);
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
        mPresenter.checkRegister(phoneNum,code,ActivityUntil.getStringMD5(password),recommendNum);
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
    }
    //提交图形验证码，获取短信验证码
    @Override
    public void submitCheck(String code) {
        imageCheckFragment.setCancelable(true);
        imageCheckFragment.dismiss();
        mPresenter.verityCode(recommend_num.getText().toString().trim(),code);
    }

    //注册成功
    @Override
    public void registerSuccess() {
        MainActivity.go2MainActivity(RegisterActivity.this);
    }

    //获取验证码成功
    @Override
    public void verityCodeSuccess() {
        mBtn_code.setVisibility(View.GONE);
        mTv_time.setVisibility(View.VISIBLE);
        downTimer.start();
        showToast("验证码已发送",Toast.LENGTH_SHORT);
    }
    //注册失败提示失败信息
    @Override
    public void registerError(String errorMsg) {
        showToast(errorMsg,Toast.LENGTH_SHORT);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        downTimer.cancel();
    }
}

