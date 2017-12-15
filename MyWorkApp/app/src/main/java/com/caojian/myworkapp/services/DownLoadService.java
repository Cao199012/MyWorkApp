package com.caojian.myworkapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.activity.MainActivity;
import com.caojian.myworkapp.until.DownloadTask;

import java.io.File;

import static com.caojian.myworkapp.until.ActivityUntil.setPermission;

public class DownLoadService extends Service {
    DownloadTask downloadTask;
    String downloadUrl = "http://www.lljyz.cn/lljyz/Gas_Station_qh.apk";


    DownloadTask.DownloadListen downloadListen = new DownloadTask.DownloadListen() {
        @Override
        public void onProgress(int progress) {
            //跟新显示进度
            getNotificationManager().notify(1,getNotification("Downloading....",progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null; //自动回收
            //下载成功时将前台服务通知关闭，并建立下载成功通知
            stopForeground(true);

            getNotificationManager().notify(1,getNotification("Download Success",-1));
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            File file = new File(directory+fileName);
            Log.d("lengtn",file.length()+"");
            setPermission(file.getPath());

            Toast.makeText(DownLoadService.this,file.length()+"Download success",Toast.LENGTH_SHORT).show();
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //版本在7.0以上是不能直接通过uri访问的
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri = FileProvider.getUriForFile(DownLoadService.this, "com.caojian.myworkapp.fileProvider", file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                installIntent.setDataAndType(apkUri,"application/vnd.android.package-archive");
            } else {
                installIntent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
            }
//            installIntent.setDataAndType(Uri.fromFile(file),
//                    "application/vnd.android.package-archive");
            startActivity(installIntent);
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);

            getNotificationManager().notify(1,getNotification("Download Failed",-1));

            Toast.makeText(DownLoadService.this,"Download Failed",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;

            Toast.makeText(DownLoadService.this,"Download Paused",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancled() {
            downloadTask = null;
            stopForeground(true);

            getNotificationManager().notify(1,getNotification("Download Cancel",-1));

            Toast.makeText(DownLoadService.this,"Download Cancel",Toast.LENGTH_SHORT).show();
        }
    };


    public DownLoadService() {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(downloadTask == null)
        {
            downloadTask = new DownloadTask(downloadListen);
        }
        downloadTask.execute(downloadUrl);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new DownBinder();
    }

    public class DownBinder extends Binder{

        public void startDown(String url)
        {
            if(downloadTask == null)
            {
                downloadUrl = url;
                downloadTask = new DownloadTask(downloadListen);
                downloadTask.execute(url);

                startForeground(1,getNotification("Download....",0));
                Toast.makeText(DownLoadService.this,"Downlaod......",Toast.LENGTH_SHORT).show();
            }

        }

        public void pauseDown()
        {
            if(downloadTask != null)
            {
                downloadTask.pauseDownLoad();
            }
            Toast.makeText(DownLoadService.this,"Pauselaod......",Toast.LENGTH_SHORT).show();
        }

        public void cancleDown()
        {
            if(downloadTask != null)
            {
                downloadTask.cancleDownLoad();
            }else {
                if(downloadUrl != null)
                {
                    //取消下载时需要将文件删除，并通知关闭
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

                    File file = new File(directory+fileName);
                    if(file.exists())
                    {
                        file.delete();
                    }
                    //取消通知
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownLoadService.this,"CancelLaod......",Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    NotificationManager getNotificationManager()
    {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    Notification getNotification(String title,int progress)
    {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));

        builder.setContentIntent(pi);
        builder.setContentTitle(title);

        if(progress > 0)
        {
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }
}
