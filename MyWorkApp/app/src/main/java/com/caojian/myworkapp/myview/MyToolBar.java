package com.caojian.myworkapp.myview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

/**
 * Created by caojian on 2017/9/2.
 */

public class MyToolBar extends Toolbar {

    public MyToolBar(Context context) {
        super(context);
    }

    public MyToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void initView()
    {
      //  addView();
    }
}
