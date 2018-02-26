package com.caojian.myworkapp.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CJ on 2017/8/18.
 */

public class RetrofitManger {
    private static OkHttpClient client;
    static Interceptor interceptor ;

    //初始化client
    private static void initClient(Context context)
    {
        if (interceptor == null)
        {
            interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("deviceNo",((MyApplication)context.getApplicationContext()).getDeviceId())
                            .addHeader("version", ActivityUntil.getVersionName(context))
                            .addHeader("noForceFlag",((MyApplication)context.getApplicationContext()).getNoForceFlag())
                            .addHeader("client", "2").build();
                    return chain.proceed(request);
                }
            };
        }
        client = new OkHttpClient.Builder()
                     .addInterceptor(interceptor)
                     .connectTimeout(30, TimeUnit.SECONDS)
                     .readTimeout(30,TimeUnit.SECONDS).build();
    }

    //获取rxjava的retrofit
    public static Retrofit getRetrofitRxjava(String url, Context context)
    {
        if(client == null)
        {
            initClient(context);
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create())
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .build();
        return retrofit;
    }
    //处理普通的retrofit请求
    public static Retrofit getRetrofit(String url,Context context)
    {
        if(client == null)
        {
            initClient(context);
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
        return retrofit;
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
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
}
