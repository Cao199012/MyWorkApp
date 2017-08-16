package com.caojian.myworkapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    //static 类名直接调用，加载时时创建 存在方法区。类被卸载，空间释放。
    public static void go2WelcomeActivity(Context from){
        Intent intent = new Intent(from,WelcomeActivity.class);
        from.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}
