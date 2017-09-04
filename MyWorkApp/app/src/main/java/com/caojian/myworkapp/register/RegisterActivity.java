package com.caojian.myworkapp.register;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.caojian.myworkapp.MainActivity;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BaseActivity;
import com.caojian.myworkapp.password.PasswordActivity;
import com.caojian.myworkapp.password.VerityCodeMsg;
import com.caojian.myworkapp.password.VerityService;
import com.caojian.myworkapp.until.ActivityControler;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.RetrofitManger;
import com.caojian.myworkapp.until.Until;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends BaseActivity {

    public static void go2RegisterActivity(Context from, String phoneNum)
    {
        Intent intent = new Intent(from,RegisterActivity.class);
        intent.putExtra("phoneNum",phoneNum);
        from.startActivity(intent);
    }



    // UI references.
    @BindView(R.id.recommend_num)
    EditText recommend_num;
    @BindView(R.id.toolbar_register)
    Toolbar mToolbar;
    @BindView(R.id.verity_code)
    EditText mEdit_code;
    @BindView(R.id.get_code)
    Button mBtn_code;
    @BindView(R.id.code_time)
    TextView mTv_time;

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

    private Unbinder unbinder;
    //private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        unbinder = ButterKnife.bind(this);
        //设置全屏显示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mToolbar.setTitle("");
        //设置title
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mPasswordView = (EditText) findViewById(R.id.password);


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private Call<VerityCodeMsg> call;
    /**
     * 获取验证码
     * @param view
     */
    public void verityCode(View view)
    {
        showProgerss(RegisterActivity.this);
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
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        String recommendNum = recommend_num.getText().toString();
        if(!ActivityUntil.CheckPhone(recommendNum).equals(""))
        {
            ActivityUntil.showToast(getBaseContext(),ActivityUntil.CheckPhone(recommendNum), Toast.LENGTH_LONG);
            return;
        }
        String password = mPasswordView.getText().toString();
        if(password.isEmpty())
        {
            ActivityUntil.showToast(getBaseContext(),"请填写登录密码", Toast.LENGTH_LONG);
            return;
        }
        String code = mEdit_code.getText().toString();
        if(code.isEmpty())
        {
            ActivityUntil.showToast(getBaseContext(),"请输入验证码", Toast.LENGTH_LONG);
            return;
        }
        showProgerss(RegisterActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                MainActivity.go2MainActivity(RegisterActivity.this);
                ActivityControler.finishActivity();
            }
        },1000);

    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        downTimer.cancel();
    }
}

