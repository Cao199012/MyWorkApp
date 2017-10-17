package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.model.response.VerityCodeMsg;

import com.caojian.myworkapp.manager.ActivityControl;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.until.Until;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PasswordActivity extends BaseTitleActivity {

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
        setContentView(R.layout.activity_password);
        unbinder = ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mToolbar.setTitle("");

    }

    Call<VerityCodeMsg> call = null;

    /**
     * 获取验证码
     * @param view
     */
    public void verityCode(View view)
    {
        showProgerss(PasswordActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                mBtn_code.setVisibility(View.GONE);
                mTv_time.setVisibility(View.VISIBLE);
                downTimer.start();
            }
        },1000);

        //后台获取验证码
        Retrofit retrofit = RetrofitManger.getRetrofit(Until.HTTP_BASE_URL);
        MyApi service = retrofit.create(MyApi.class);
        call = service.verityCode("","");
        call.enqueue(new Callback<VerityCodeMsg>() {
            @Override
            public void onResponse(Call<VerityCodeMsg> call, Response<VerityCodeMsg> response) {
                VerityCodeMsg msg = response.body();
                if(msg != null)
                {
                    //验证码获取成功
                    if(msg.getRetcode().equals("0"))
                    {
                        // TODO: 2017/8/21 验证码倒计时
                    }else
                    {
                        showVerityResult(msg.getRetinfo());
                    }

                }else
                {
                    showVerityResult("网络错误");
                }
            }
            @Override
            public void onFailure(Call<VerityCodeMsg> call, Throwable t) {

            }
        });

    }

    private void showVerityResult(String msg)
    {
        showToast(msg, Toast.LENGTH_LONG);
    }

    /**
     * 提交按钮调用
     * @param v
     */
    public void sumbitPassword(View v)
    {
        // TODO: 2017/8/21 提交新密码
        String password = mTv_password.getText().toString();
        String code = mEdit_verity.getText().toString();
        if(password.isEmpty())
        {
            showToast("新密码不能为空",Toast.LENGTH_SHORT);
            return;
        }
        if(code.isEmpty())
        {
            showToast("请输入验证码",Toast.LENGTH_SHORT);
            return;
        }
        //提交成功返回登录页面
        showProgerss(PasswordActivity.this);
        try {
            Thread.sleep(1000);
            hideProgress();
            LoginActivity.go2LoginActivity(PasswordActivity.this);
            ActivityControl.finishActivity();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null && call.isExecuted())
        {
            call.cancel();
        }
        if (unbinder != null)
        {
            unbinder.unbind();
        }

        downTimer.cancel();
    }
}
