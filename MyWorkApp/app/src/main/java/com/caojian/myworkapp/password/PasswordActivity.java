package com.caojian.myworkapp.password;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BaseActivity;
import com.caojian.myworkapp.login.LoginActivity;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.RetrofitManger;
import com.caojian.myworkapp.until.Until;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PasswordActivity extends BaseActivity {

    public static void go2PasswordActivity(Context from, String phoneNum)
    {
        Intent intent = new Intent(from,PasswordActivity.class);
        intent.putExtra("phoneNum",phoneNum);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
    }

    Call<VerityCodeMsg> call = null;

    /**
     * 获取验证码
     * @param view
     */
    public void verityCode(View view)
    {
        Retrofit retrofit = RetrofitManger.getRetrofit(Until.HTTP_BASE_URL);

        VerityService service = retrofit.create(VerityService.class);

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
        ActivityUntil.showToast(getBaseContext(),msg, Toast.LENGTH_LONG);
    }

    /**
     * 提交按钮调用
     * @param v
     */
    public void sumbitPassword(View v)
    {
        // TODO: 2017/8/21 提交新密码
        //提交成功返回登录页面
        LoginActivity.go2LoginActivity(PasswordActivity.this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        ActivityUntil.disToast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null && call.isExecuted())
        {
            call.cancel();
        }
    }
}
