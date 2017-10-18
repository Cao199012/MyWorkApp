package com.caojian.myworkapp.until;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by caojian on 2017/10/12.
 */

public class DownloadTask extends AsyncTask<String,Integer,Integer>{


    public interface DownloadListen{
        void onProgress(int progress);

        void onSuccess();

        void onFailed();

        void onPaused();

        void onCancled();
    }

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCLED = 3;

    private DownloadListen downloadListen;

    private boolean isCancled = false;

    private boolean isPaused = false;

    private int lastProgress;

    public DownloadTask(DownloadListen listen){
        downloadListen = listen;
    }
    //后台下载
    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile saveFile = null;
        File file = null;
        try {
            long downloadedlenth = 0; //记录已经下载的长度
            String downUrl = params[0]; //传入的第一个参数为下载地址

            String fileName = downUrl.substring(downUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

            file = new File(directory+fileName);

            if(file.exists())
            {
                downloadedlenth = file.length();
            }
            long contentLength = getContentLength(downUrl);
            //请求失败
            if(contentLength == 0)
            {
                return TYPE_FAILED;
            }
            if(contentLength == downloadedlenth)
            {
                return TYPE_SUCCESS;
            }

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("RANGE","bytes=" + downloadedlenth + "-")
                    .url(downUrl)
                    .build();
            Response response = client.newCall(request).execute();

            if(response != null)
            {
                is = response.body().byteStream();
                saveFile = new RandomAccessFile(file,"rw");
                //逃到已下载的地方
                saveFile.seek(downloadedlenth);

                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1)
                {
                    if(isCancled)
                    {
                        return TYPE_CANCLED;
                    }
                    if (isPaused)
                    {
                        return TYPE_PAUSED;
                    }
                    //
                    total += len;
                    saveFile.write(b,0,len);
                    int progress = (int) ((total + downloadedlenth)*100/contentLength);
                    publishProgress(progress);
                }

                response.body().close();
                return TYPE_SUCCESS;

            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null)
                {
                    is.close();
                }

                if(saveFile != null)
                {
                    saveFile.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if(progress > lastProgress)
        {
            downloadListen.onProgress(progress);
            lastProgress = progress;
        }

    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer)
        {
            case TYPE_CANCLED:
                downloadListen.onCancled();
                break;
            case TYPE_PAUSED:
                downloadListen.onPaused();
                break;
            case TYPE_SUCCESS:
                downloadListen.onSuccess();
                break;
            case TYPE_FAILED:
                downloadListen.onFailed();
                break;
            default:
                break;

        }
    }


    public void pauseDownLoad()
    {
        isPaused = true;
    }

    public void cancleDownLoad()
    {
        isCancled = true;
    }
    private long getContentLength(String downUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downUrl)
                .build();
        Response response = client.newCall(request).execute();
        if(response != null && response.isSuccessful())
        {
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }

}


