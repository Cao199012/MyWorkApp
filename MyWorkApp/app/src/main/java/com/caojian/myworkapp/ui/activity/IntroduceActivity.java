package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IntroduceActivity extends BaseTitleActivity {



    public static void go2IntroduceActivity(Context from){
        Intent intent = new Intent(from,IntroduceActivity.class);
        from.startActivity(intent);
    }
    @BindView(R.id.intro_pager)
    ViewPager mIntro_pager;
    @BindView(R.id.tip_body)
    LinearLayout mTipBody;   //填放显示滑动当前页面的点


    private ImageView[] tips ;
    private Unbinder unbinder;

    private String[] file_paths = {"file:///android_asset/splash_01.jpg","file:///android_asset/splash_02.jpg","file:///android_asset/splash_03.jpg"};

    private int pre_position = 0;//记录每次滑动的位置和最新的比较,初始化为0
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        unbinder = ButterKnife.bind(this);

        //设置viewpager中view的个数
        mIntro_pager.setOffscreenPageLimit(3);
        mIntro_pager.setAdapter(new IntroViewPagerAdapter());
        //监控viewpage页面变化时，相应的操作
        mIntro_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                setCurrentTip(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //初始化生成点视图
        initTip();
    }

    private void setCurrentTip(int currentTip) {
        if(currentTip < 0 && currentTip >= tips.length)
        {
            return;
        }
        tips[currentTip].setImageResource(R.drawable.tip_selected);
        tips[pre_position].setImageResource(R.drawable.tip_unselect);
        pre_position = currentTip;
    }

    /**
     * 根据现实图片个数，生成点的个数
     */
    private void initTip() {
        //根据图片长度初始化，view数组
        if(tips == null)
        {
            tips = new ImageView[file_paths.length];
        }
        int i = 0;

        for (; i < file_paths.length;i++)
        {
            //动态生成
            ImageView tip = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //layoutParams.setMargins(0,0, Until.dip2px(getBaseContext(),50),0);
           layoutParams.weight = 1;
            tip.setLayoutParams(layoutParams);
            //初始化点 （当前为白色，其他为红色）
            if(i == 0)
            {
                tip.setImageResource(R.drawable.tip_selected);
            }else
            {
                tip.setImageResource(R.drawable.tip_unselect);
            }
            //向body添加 view
            mTipBody.addView(tip);
            tips[i] = tip;
        }
    }

    //自定义Adapter，加入ImageView
    private class IntroViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return file_paths.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View pagerItem = LayoutInflater.from(container.getContext()).inflate(R.layout.intro_pager_item,null,false);
            ImageView imageView = (ImageView) pagerItem.findViewById(R.id.intro_show);
            Glide.with(container.getContext()).load(file_paths[position]).into(imageView);
            container.addView(pagerItem,0);
            return pagerItem;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * @param v
     * xml中的button点击出路函数，跳转到登录页面
     */
    public void go2Login(View v)
    {
        LoginActivity.go2LoginActivity(IntroduceActivity.this);
        //finish();
    }
    public void go2Register(View v)
    {
        PhoneCheckActivity.go2PhoneCheckActivity(IntroduceActivity.this,1);
        //finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
