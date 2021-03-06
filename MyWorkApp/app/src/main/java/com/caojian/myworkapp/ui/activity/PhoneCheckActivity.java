package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.CheckContract;
import com.caojian.myworkapp.ui.presenter.CheckPresenter;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 注册和重设密码都需要验证密码是否已经注册
 */
public class PhoneCheckActivity extends MvpBaseActivity<CheckContract.View,CheckPresenter> implements CheckContract.View{

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

    private CheckPresenter mPresenter;
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
        if(from_flag == 1) //注册
        {
            mDeal_body.setVisibility(View.VISIBLE);
            mTv_forget.setVisibility(View.INVISIBLE);
            check_num.setText("推荐人号码");
            //不需要默认号码
          //  mEdit_check.setText("18252011017");
        }else
        {
            mTv_forget.setVisibility(View.VISIBLE);
            mDeal_body.setVisibility(View.GONE);
            check_num.setText("手机号码");
        }
    }

    String phone ="";
    public void checkPhone(View view)
    {
        //对输入的号码做判断
        phone = mEdit_check.getText().toString().trim();


         if(from_flag != 1)
         {
             if(!ActivityUntil.CheckPhone(phone).equals(""))
             {
                 showToast(ActivityUntil.CheckPhone(phone), Toast.LENGTH_SHORT);
                 return;
             }
             //向后台校验号码
             mPresenter.checkPhone(phone);
         }else {
             if(phone.isEmpty()){
                 showToast("邀请码不能为空",Toast.LENGTH_SHORT);
                 return;
             }
             checkResultSuccess();
         }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public CheckPresenter createPresenter() {
        mPresenter = new CheckPresenter(PhoneCheckActivity.this,this);
        return mPresenter;
    }

    @Override
    public void checkResultError(String result) {

        if(from_flag == 2)
        {
            if(result.contains("邀请")){
                showToast("号码未注册",Toast.LENGTH_SHORT);
                return;
            }

        }
        showToast(result,Toast.LENGTH_SHORT);
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

    public void goWwbService(View view)
    {
        WebViewDetailActivity.go2WebViewDetailActivity(PhoneCheckActivity.this,0);
    }
    public void goWebPrivacy(View view)
    {
        WebViewDetailActivity.go2WebViewDetailActivity(PhoneCheckActivity.this,1);
    }

}
