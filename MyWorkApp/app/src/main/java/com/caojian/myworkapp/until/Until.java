package com.caojian.myworkapp.until;

import android.content.Context;

/**
 * Created by CJ on 2017/8/16.
 */

public class Until {

    public final static String HTTP_BASE_URL = "http://测试环境/mobile-server/";
    public final static int TREMINALtYPE = 2; //ios 1 android 2

//    根据手机的分辨率从 dp 的单位 转成为 px(像素)

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
