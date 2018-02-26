package com.caojian.myworkapp.until;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ on 2017/8/16.
 */

public class Until {

    public final static String HTTP_BASE_URL = "http://app.xuxinpei.com/mobile-server/";
    public final static String DOWNLOAD_BASE_URL = "http://app.xuxinpei.com/mobile-server/index.html?";
    public final static String QUESTION_BASE_URL = "http://app.xuxinpei.com/mobile-server/questions.html?";
   // public final static String HTTP_BASE_URL = "http://103.255.178.23:8081/mobile-server/";
   // public final static String HTTP_BASE_URL = "http://192.168.1.142:8080/mobile-server/";
    public final static String HTTP_BASE_IMAGE_URL = HTTP_BASE_URL + "downLoadPic.do?fileId=";
    public final static String ACTION_FRIEND = "com.caojian.myworkapp.ui.fragment.FriendFragment";
    public final static String DILOG_MSG_MIAN = "基本功能。开通后方可使用此软件，实现好友定位，享受您所分享好友同功能的积分奖励。";
    public final static String DILOG_MSG_RAIL = "可为您的至爱亲友设置电子围栏，一旦超出立即提醒。开通后方可享受您所分享好友同功能的积分奖励。";
    public final static String DILOG_MSG_TRACK = "可查看过去一段时间您或好友的轨迹，并分享至您的朋友圈。开通后方可享受您所分享好友同功能的积分奖励。";
    public final static int TREMINALtYPE = 2; //ios 1 android 2
    public final static int MIN_DISTANCE = 10; //ios 1 android 2
    public final static int MAX_DISTANCE = 2100; //ios 1 android 2

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

    //根据采集得样本 算出平均值
    public static LatLng averageLat(List<LatLng> pLatLngLst){
        double latAverage = 0,lotAverage = 0;
        double latAll = 0,lotAll = 0;
        double latStandard = 0,lotStandard = 0;
        List<LatLng> _LatLngLst = new ArrayList<>();
        _LatLngLst.clear();
        _LatLngLst.addAll(pLatLngLst);
        for(LatLng latLng  : pLatLngLst){
            latAverage += latLng.latitude;
            lotAverage += latLng.longitude;

        }

        latAverage = latAverage/pLatLngLst.size();
        lotAverage = lotAverage/pLatLngLst.size();
        //求标准差
        for(LatLng latLng  : pLatLngLst){  //每个点和平均数的差值
            latAll += Math.pow((latLng.latitude - latAverage),2);
            lotAll += Math.pow(latLng.longitude - lotAverage,2);
        }

        latStandard = Math.sqrt(latAll/pLatLngLst.size()); //标准差
        lotStandard = Math.sqrt(lotAll/pLatLngLst.size()); //标准差
        for(LatLng latLng  : _LatLngLst){  //每个点和平均数的差值
           if(Math.abs(latLng.latitude - latAverage) > latStandard*2 || Math.abs(latLng.longitude - lotAverage) > lotStandard*2){
               //差值大于两个平方差 去除点
               _LatLngLst.remove(latLng);
           }
        }

        //清零 重新计算平均值
        latAverage = 0;
        lotAverage = 0;
        for(LatLng latLng  : _LatLngLst){
            latAverage += latLng.latitude;
            lotAverage += latLng.longitude;

        }

        latAverage = latAverage/_LatLngLst.size();
        lotAverage = lotAverage/_LatLngLst.size();

        return new LatLng(latAverage,lotAverage);
    }

    /**
     * 获取当前时间戳(单位：秒)
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

}
