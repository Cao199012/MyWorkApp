package com.caojian.myworkapp.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.UpdateResponse;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

import static com.caojian.myworkapp.ui.activity.IntroduceActivity.go2IntroduceActivity;
import static com.caojian.myworkapp.until.ActivityUntil.getToken;

import static com.caojian.myworkapp.ui.activity.UpdateActivity.go2UpdateActivity;

//欢迎页面
public class SplashActivity extends BaseTitleActivity {

    @BindView(R.id.splash_img)
    ImageView mImageView;

    private Unbinder mUnbinder;
    private  ObjectAnimator objectAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否登录
        String token = getToken(getBaseContext());
        if(token.equals(""))  //没有登录记录进入介绍页面
        {
            go2IntroduceActivity(SplashActivity.this);
            finish();
        }else  //否则
        {
            setContentView(R.layout.activity_splash);
            //存入Application供全局使用
            //((MyApplication)getApplication()).setToken(token);
            mUnbinder = ButterKnife.bind(this);
            //加载图片
            Glide.with(this).load("file:///android_asset/splash_01.jpg").into(mImageView);
            objectAnimator = ObjectAnimator.ofFloat(mImageView,"alpha",0.0f,1.0f);
            objectAnimator.setDuration(1000);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //动画结束检测版本
                    checkVersion(getBaseContext());
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
    private Disposable disposable;
    private void checkVersion(Context context) {
        //                MainActivity.go2MainActivity(SplashActivity.this);
//                finish();
        UpdateResponse.DataBean dataBean = new UpdateResponse.DataBean();
        dataBean.setComment("更新内容说明");
        dataBean.setMandatory("0");
        go2UpdateActivity(SplashActivity.this,dataBean,101);
//        Retrofit retrofit = RetrofitManger.getRetrofitRxjava(Until.HTTP_BASE_URL);
//        UpdateService updateService = retrofit.create(UpdateService.class);
//        Observable<UpdateResponse> observable = updateService.getUpdateMsg(getVersionCode(context),Until.TREMINALtYPE);
//        disposable = observable.observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(new Consumer<UpdateResponse>() {
//            @Override
//            public void accept(UpdateResponse updateMsg) throws Exception {
//                if(Integer.parseInt(updateMsg.getCode(),1) == 0)
//                {
//                    UpdateResponse.DataBean dataBean = updateMsg.getData();
//                    if(dataBean != null && dataBean.equals("1"))
//                    {
//                        //显示更新提示窗口
//                        go2UpdateActivity(context,dataBean,101);
//                    }else
//                    {
//                        go2NextActivity();
//                    }
//
//                }else
//                {
//                    showToast(context,updateMsg.getMessage(), Toast.LENGTH_SHORT);
//                }
//            }
//        });
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

        if(disposable != null)
        {
            disposable.dispose();
        }
    }
}
