package com.caojian.myworkapp;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

/**
 * Created by caojian on 2017/7/19.
 */

public class MyApplication extends Application{


    private String token;
    @Override
    public void onCreate() {
        super.onCreate();
        // 加载系统默认设置，字体不随用户设置变化
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
