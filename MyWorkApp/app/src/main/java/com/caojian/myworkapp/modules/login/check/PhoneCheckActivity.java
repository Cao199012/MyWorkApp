package com.caojian.myworkapp.modules.login.check;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BaseTitleActivity;
import com.caojian.myworkapp.modules.login.password.PasswordActivity;
import com.caojian.myworkapp.modules.login.register.RegisterActivity;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 注册和重设密码都需要验证密码是否已经注册
 */
public class PhoneCheckActivity extends BaseTitleActivity implements CheckContract.View{

    /**
     * @param from
     * @param flag 1 是注册 2 是找回密码
     */
    public static void go2PhoneCheckActivity(Context from,int flag)
    {
        Intent intent = new Intent(from,PhoneCheckActivity.class);
        intent.putExtra("fromFlag",flag);
        from.startActivity(intent);
    }

    @BindView(R.id.edit_check)
    EditText mEdit_check;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.deal_body)
    LinearLayout mDeal_body; //显示注册协议（找回密码隐藏，注册显示）

    @BindView(R.id.tv_forget)
    TextView mTv_forget;
    @BindView(R.id.tv_check_num)
    TextView check_num;

    private Unbinder unbinder;

    private CheckContract.Presenter mPresenter;
    private int from_flag = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置窗口没有标题
        setContentView(R.layout.activity_check);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        unbinder = ButterKnife.bind(this);

        mToolbar.setTitle("");
        from_flag = getIntent().getIntExtra("fromFlag",-1);
        if(from_flag == 1)
        {
            mDeal_body.setVisibility(View.VISIBLE);
            mTv_forget.setVisibility(View.GONE);
            check_num.setText("推荐人号码");
        }else
        {
            mTv_forget.setVisibility(View.VISIBLE);
            mDeal_body.setVisibility(View.GONE);
            check_num.setText("手机号码");
        }
        mPresenter = new CheckPersenter(this);
    }

    String phone ="";
    public void checkPhone(View view)
    {
        //对输入的号码做判断
        phone = mEdit_check.getText().toString();

        if(!ActivityUntil.CheckPhone(phone).equals(""))
        {
            ActivityUntil.showToast(PhoneCheckActivity.this,ActivityUntil.CheckPhone(phone), Toast.LENGTH_SHORT);
            return;
        }
        //向后台校验号码
       // mPresenter.checkPhone(phone);
        showProgerss(PhoneCheckActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                checkResultSuccess();
            }
        },1000);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void checkResultError(String result) {
        ActivityUntil.showToast(getBaseContext(),result,Toast.LENGTH_SHORT);
    }

    //手机验证通过
    @Override
    public void checkResultSuccess() {
        if(from_flag == 1)
        {
            RegisterActivity.go2RegisterActivity(PhoneCheckActivity.this,phone);
        }
        if(from_flag == 2)
        {
            PasswordActivity.go2PasswordActivity(PhoneCheckActivity.this,phone);
        }
    }


}
