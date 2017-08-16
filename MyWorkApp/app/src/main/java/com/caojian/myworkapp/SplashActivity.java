package com.caojian.myworkapp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caojian.myworkapp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash_img)
    ImageView mImageView;

    private Unbinder mUnbinder;
    private  ObjectAnimator objectAnimator;
    private Handler handler;
    private SpalshHandler spalshHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        spalshHandler = new SpalshHandler();

        mUnbinder = ButterKnife.bind(this);
        Glide.with(this).load(R.mipmap.ic_launcher_round).into(mImageView);

        objectAnimator = ObjectAnimator.ofFloat(mImageView,"alpha",0.0f,1.0f);
        objectAnimator.setDuration(1000);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                MainActivity.go2MainActivity(SplashActivity.this);
                finish();
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

    @Override
    protected void onResume() {
        super.onResume();
        if(handler != null && spalshHandler != null)
        {
            handler.post(spalshHandler);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        写两个方法，主要是为了让线程销毁掉，场景如下：
//        1.用户打开app的一瞬间，想上厕所，关掉app（app就真的关闭了）～～（如果写在oncreate中的话，app在几秒之后还要自己启动，主要是因为页面只是finish掉了，但是并没有释放。）
//        2.用户打开app的一瞬间，忽然幺幺零给他打了一个电话，这时候app停止了，电话结束后，页面又恢复如初了。
        if(handler != null && spalshHandler != null)
        {
            handler.removeCallbacks(spalshHandler);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity销毁，防止动画被view引用无法回收，从而导致Activity无法被释放导致内存泄漏
        objectAnimator.cancel(); //取消动画

        mUnbinder.unbind();
    }
    
    private class SpalshHandler implements Runnable{

        @Override
        public void run() {
            //if(MyApplication.get)
            // TODO: 2017/8/15 判断本地是否存储token决定跳转页面
            WelcomeActivity.go2WelcomeActivity(SplashActivity.this);
            finish();
        }
    }
}
