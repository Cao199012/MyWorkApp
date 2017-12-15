package com.caojian.myworkapp.until;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.caojian.myworkapp.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by CJ on 2017/7/28.
 */

public class ActivityUntil {

    private static Toast toast;
    private final static String PHONE_PATTERN = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17([0,1,6,7,]))|(18[0-2,5-9]))\\d{8}$";
    private final static String PASSWORD_PATTERN = "^([0-9]|[a-z]|[A-Z]){6,10}$";
    public final static String[] WEEKS = {"周一","周二","周三","周四","周五","周六","周日"};
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA);
    public static String genBillNum() {
        return simpleDateFormat.format(new Date());
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

    //获取APP信息
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

    //检测密码格式（6-10位的数字、字母组合）
    public static boolean checkPassword(String password)
    {
       boolean isPassWord = Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
        return isPassWord;
    }

    public static void initActionBar(Toolbar pToolbar,AppCompatActivity context,int drawbleId)
    {
        context.setSupportActionBar(pToolbar);
        ActionBar actionBar = context.getSupportActionBar();
        actionBar.setHomeAsUpIndicator(drawbleId);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //判断网络类型 ： -1 没有网络 0 GPS 1 wifi
    public static int getActiveNetWork(Context context)
    {
        ConnectivityManager mConnectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        //检查网络连接
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();

        if (info == null || !info.isAvailable()) {
            return -1;
        }

        return info.getType();
    }

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

    public static String[] myCheckPermission(Context context,String[] permission)
    {
        List<String> permissions = new ArrayList<>();
        for (String per :
                permission) {
           if(ContextCompat.checkSelfPermission(context, per) != PackageManager.PERMISSION_GRANTED)
           {
               permissions.add(per);
           }
        }

        return (String[]) permissions.toArray(new String[permissions.size()]);
    }

    /**
     * 提升读写权限
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static void setPermission(String filePath)  {
        String command = "chmod " + "777" + " " + filePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LocationClientOption getDefaultOption(int time){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(time);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setLocationNotify(true);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        return option;
    }
    //计算两坐标点之间的距离
    public static double getDistance(LatLng lat1,LatLng lat2)
    {
        return DistanceUtil.getDistance(lat1,lat2);
    }


    //MD5加密
    private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String getBytesMD5(byte[] bytes) {
        try {

            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            mdInst.update(bytes);

            // 获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回字符串的32位MD5值
     *
     * @param s
     *            字符串
     * @return str MD5值
     */
    public final static String getStringMD5(String s) {
        Log.e("md5",getBytesMD5(s.getBytes()));
        return getBytesMD5(s.getBytes());
    }


    private static int[] dayNums = {1,2,3,4,5,6,7};
    public static String[]  numList2Str(List<Integer> listData) {
        StringBuilder days = new StringBuilder();
        StringBuilder weeks = new StringBuilder();
        for(int i : dayNums){
            for (Integer num : listData) {
                if(i == num) {
                    days.append(i);
                    days.append(",");
                    weeks.append(ActivityUntil.WEEKS[num - 1]);
                }
            }
        }

        String[] result = {days.toString(),weeks.toString()};
        return result;
    }

    private File changeFilePath(File path) {
        Bitmap _bitmap = compressImage(BitmapFactory.decodeFile(path.getPath()), 600);
        final String filename = path.getName();
        File file = new File(path.getPath().substring(0, path.getPath().length() - filename.length()), 3 + filename);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024);
            _bitmap.compress(Bitmap.CompressFormat.JPEG,100, bufferedOutputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return file;
    }

    /**
     * 压缩一张图片至指定大小
     *
     * @param image 需要压缩的图片
     * @param size  压缩后的最大值(kb)
     * @return 压缩后的图片
     */
    public static Bitmap compressImage(Bitmap image, int size) {

        // 将bitmap放至数组中，意在bitmap的大小(与实际读取的原文件要大)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;

        // 判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > size) {
            // 获取bitmap大小 是允许最大大小的多少倍
            double i = mid / size;
            // 开始压缩  此处用到平方根 将宽度和高度压缩掉对应的平方根倍
            // (保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小)
            image = zoomImage(image, image.getWidth() / Math.sqrt(i),
                    image.getHeight() / Math.sqrt(i));
        }

        return image;
    }

    private static Bitmap zoomImage(Bitmap image, double newWidth,
                                    double newHeight) {
        // 获取这个图片的宽和高
        float width = image.getWidth();
        float height = image.getHeight();

        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();

        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(image, 0, 0, (int) width,
                (int) height, matrix, true);
    }
}
