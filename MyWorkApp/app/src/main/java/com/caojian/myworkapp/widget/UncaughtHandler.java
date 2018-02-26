package com.caojian.myworkapp.widget;

import java.io.File;
import java.io.PrintWriter;

import android.app.Application;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by on 2017/6/16.
 */

public class UncaughtHandler implements Thread.UncaughtExceptionHandler
{
    private static final String TAG = UncaughtHandler.class.getSimpleName();
    
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    
    public UncaughtHandler()
    {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }
    
    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        if (!handleException(ex) && null != mDefaultHandler)
        {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
        else
        {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        
    }



    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable ex)
    {
        if (ex == null)
        {
            return false;
        }
        File file = new File(
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/eSale/bug/");
        if (!file.exists())
        {
            file.mkdirs();
        }
        try
        {
            PrintWriter writer = new PrintWriter(
                    Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/eSale/bug/" + System.currentTimeMillis()
                + ".log");
            ex.printStackTrace(writer);
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
