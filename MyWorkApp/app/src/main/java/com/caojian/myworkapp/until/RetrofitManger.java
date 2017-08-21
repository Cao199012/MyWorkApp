package com.caojian.myworkapp.until;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CJ on 2017/8/18.
 */

public class RetrofitManger {
    private static OkHttpClient client;


    private static void initClient()
    {
        client = new OkHttpClient.Builder()
                     .connectTimeout(15, TimeUnit.SECONDS)
                     .readTimeout(15,TimeUnit.SECONDS).build();

    }

    public static Retrofit getRetrofitRxjava(String url)
    {
        if(client == null)
        {
            initClient();
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create())
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .build();
        return retrofit;
    }
    public static Retrofit getRetrofit(String url)
    {
        if(client == null)
        {
            initClient();
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
        return retrofit;
    }
}
