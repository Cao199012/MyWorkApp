package com.caojian.myworkapp.until;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Created by CJ on 2017/7/28.
 */

public class ActivityUntil {
    private static Toast toast;
    private final static String PHONE_PATTERN = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17([0,1,6,7,]))|(18[0-2,5-9]))\\d{8}$";

    /**
     * @param context
     * @param msg
     * @param duration short or long
     */
    public static void showToast(Context context,String msg,int duration)
    {
        if (toast == null)
        {
            toast = Toast.makeText(context,msg,duration);
        }else {
            toast.setText(msg);
            toast.setDuration(duration);
        }
        toast.show();
    }

    public static void disToast()
    {
        if(toast != null)
        {
            toast.cancel();
            toast = null;
        }
    }


    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static String getVersionCode(Context context) {
        if(getPackageInfo(context) != null)
        {
            return getPackageInfo(context).versionCode+"";
        }else
        {
            return "1";
        }

    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 每次登录或注册就会生成一个新的token，根据token判断APP是否已经登录过
     * @param context
     * @param token
     */
    public static void saveToken(Context context,String token)
    {
        SharedPreferences file =  context.getApplicationContext().getSharedPreferences("localMsg",Context.MODE_PRIVATE);
        if(file != null && token != null)
        {
            SharedPreferences.Editor editor = file.edit();
            editor.putString("token",token);
            editor.commit();
        }
    }

    public static String getToken(Context context)
    {
        String token = "";
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("localMsg",Context.MODE_PRIVATE);
        if(preferences != null )
        {
            token = preferences.getString("token","");
        }
        return token;
    }

    /**
     * 每次退出登录 必须清除本地token的缓存
     * @param context
     */
    public static void clearToken(Context context)
    {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("localMsg",Context.MODE_PRIVATE);
        if(preferences != null )
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
        }
    }

    /**
     * 根据传入的字符串 判断是否为正确的手机号码
     * @param phone
     * @return  为空：号码不能为空  格式不对：请输入正确的手机号码  格式正确：“”
     */
    public static String CheckPhone(String phone)
    {

        if(phone == null || phone.isEmpty()){
            return "手机号码不能为空";
        }
        boolean isPhone = Pattern.compile(PHONE_PATTERN).matcher(phone).matches();
        if(!isPhone){
            return "请输入正确的手机号码";
        }
        return "";
    }
}
