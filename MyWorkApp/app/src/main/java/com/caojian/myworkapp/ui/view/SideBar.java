package com.caojian.myworkapp.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by CJ on 2017/9/12.
 */

public class SideBar extends View {
    //侧边栏字母显示
    private String[] alphabet = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"
    };
    //画笔
    TextPaint mPaint;
    private float mTextWidth;
    private int preCurrentPosition = -1;
    int itemHeight;
    //显示点击的字母
    private TextView mDialogView;
    public SideBar(Context context) {
        super(context);
        init(context);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setDialogView(TextView dialogView)
    {
        mDialogView = dialogView;
    }
    private void init(Context context)
    {
//        屏幕宽高
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        windowManager.getDefaultDisplay().getMetrics(outMetrics);
//        int height = outMetrics.heightPixels;
        //不设置检测不到up事件
        setClickable(true);
        Log.i("itemheight",itemHeight+"");
        mPaint = new TextPaint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        itemHeight = getHeight()/alphabet.length;
        float top = getTop();
        for (int i = 0; i < alphabet.length;i++)
        {
            mPaint.setColor(Color.rgb(34, 66, 99));//设置字母颜色
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体
            mPaint.setTextSize(40);//设置字体大小
            mPaint.setAntiAlias(true);//抗锯齿
            mTextWidth = mPaint.measureText(alphabet[i]);
            float left = (getWidth()-mTextWidth)/2;
            float _top = top + (i+1)*itemHeight;
            canvas.drawText(alphabet[i],left,_top,mPaint);
            mPaint.reset();
        }
    }

    private int currentPosition = -1;
   // OnTouchEvent



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y = event.getY();

        currentPosition = (int) (y/getHeight()*alphabet.length);
        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP:
                preCurrentPosition = -1;
                hideDialog();
                break;
            default:
                if(currentPosition != preCurrentPosition && currentPosition >= 0 && currentPosition < alphabet.length)
                {
                    // TODO: 2017/9/12 通知list跳转
                    if(mSelectPosition != null)
                    {
                        mSelectPosition.set2num(alphabet[currentPosition]);
                    }
                    showDialog(alphabet[currentPosition]);
                    preCurrentPosition = currentPosition;
                }
                invalidate();
                break;

        }
        return super.dispatchTouchEvent(event);
    }

    private SelectPosition mSelectPosition;
    public interface SelectPosition{
        void set2num(String num);
    }
    public void setListen(SelectPosition selectPosition){
        mSelectPosition = selectPosition;
    }
    private void showDialog(String num)
    {
        if(mDialogView != null)
        {
            mDialogView.setVisibility(VISIBLE);
            mDialogView.setText(num);
        }
    }
    private void hideDialog()
    {
        if(mDialogView != null)
        {
            mDialogView.setVisibility(GONE);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
