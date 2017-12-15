package com.caojian.myworkapp.manager;

import android.content.Context;

import com.caojian.myworkapp.MyApplication;

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
                            .addHeader("version","1.0.0").build();
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
}
