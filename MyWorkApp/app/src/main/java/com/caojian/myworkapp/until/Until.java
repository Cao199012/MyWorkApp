package com.caojian.myworkapp.until;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by CJ on 2017/8/16.
 */

public class Until {

    //public final static String HTTP_BASE_URL = "http://103.255.178.23:8081/mobile-server/";
    public final static String HTTP_BASE_URL = "http://192.168.188.109:8080/mobile-server/";
    public final static String ACTION_FRIEND = "com.caojian.myworkapp.ui.fragment.FriendFragment";
    public final static int TREMINALtYPE = 2; //ios 1 android 2
    public final static int MIN_DISTANCE = 10; //ios 1 android 2

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

    /**
     * 照片转byte二进制
     * @param fs 需要转byte的照片路径
     * @return 已经转成的byte
     * @throws Exception
     */
    public static String readStream(InputStream fs) throws Exception {
       // FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toString();
    }
    //偏移编码
//        double x= lot;double y=lat;
//        double z=Math.sqrt(x*x+y*y)+0.00002*Math.sin(y*Math.PI);
//        double temp=Math.atan2(y,x)+0.000003*Math.cos(x*Math.PI);
//        double bdLon=z*Math.cos(temp)+0.0065;
//        double bdLat=z*Math.sin(temp)+0.006;
    // LatLng newcenpt=new LatLng(bdLat,bdLon);
}
