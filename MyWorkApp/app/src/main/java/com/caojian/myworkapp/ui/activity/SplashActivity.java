package com.caojian.myworkapp.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.wallet.base.datamodel.UserData;
import com.bumptech.glide.Glide;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.model.response.UpdateResponse;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.PersonalContract;
import com.caojian.myworkapp.ui.presenter.PersonalPresenter;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.PersonalInstance;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.caojian.myworkapp.ui.activity.IntroduceActivity.go2IntroduceActivity;
import static com.caojian.myworkapp.until.ActivityUntil.getToken;

import static com.caojian.myworkapp.ui.activity.UpdateActivity.go2UpdateActivity;
import static com.caojian.myworkapp.until.ActivityUntil.getVersionCode;

//欢迎页面
public class SplashActivity extends MvpBaseActivity<PersonalContract.View,PersonalPresenter>implements PersonalContract.View {
    public static void go2SplashActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,SplashActivity.class);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.splash_img)
    ImageView mImageView;

    private Unbinder mUnbinder;
    private  ObjectAnimator objectAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否登录
        String token = getToken(getBaseContext());

       // checkVersion(getBaseContext());
        if(token.equals(""))  //没有登录记录进入介绍页面
        {
            LoginActivity.go2LoginActivity(SplashActivity.this);
            finish();
//            MainActivity.go2MainActivity(SplashActivity.this);
//            finish();
        }else  //否则
        {
            setContentView(R.layout.activity_splash);
            mUnbinder = ButterKnife.bind(this);
            //checkVersion();
            //加载图片
            //Glide.with(this).load("file:///android_asset/splash_01.jpg").into(mImageView);
            objectAnimator = ObjectAnimator.ofFloat(mImageView,"alpha",0.0f,1.0f);
            objectAnimator.setDuration(1000);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mPresenter.getPersonalInfo();
                }

                @Override
                public void onAnimationEnd(Animator animation) {


                }
                @Override
                public void onAnimationCancel(Animator animation) {

                }
                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 101)  //update 返回
        {
           go2NextActivity();
        }else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 版本无需更新跳转到下个页面
     */
    private void go2NextActivity()
    {
        MainActivity.go2MainActivity(SplashActivity.this);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity销毁，防止动画被view引用无法回收，从而导致Activity无法被释放导致内存泄漏
        if(objectAnimator != null)
        {
            objectAnimator.cancel(); //取消动画
        }
        if(mUnbinder != null)
        {
            mUnbinder.unbind();
        }


    }

    PersonalPresenter mPresenter;
    @Override
    public PersonalPresenter createPresenter() {
        mPresenter = new PersonalPresenter(SplashActivity.this,this);
        return mPresenter;
    }

    @Override
    public void getPersonalSuccess(PersonalMsg personalMsg) {
        MainActivity.go2MainActivity(SplashActivity.this);
        finish();
    }

    @Override
    public void changeMsgSuccess(String msg) {

    }

    @Override
    public void error(String errorMsg) {
        MainActivity.go2MainActivity(SplashActivity.this);
        finish();
    }
}
