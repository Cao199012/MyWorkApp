package com.caojian.myworkapp.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.LruCache;

import com.caojian.myworkapp.until.ActivityUntil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by CJ on 2018/1/21.
 *
 * 把图片存入本地 减少请求
 */

public class ImageLoad {
    LruCache<String,Bitmap> mLruCache;

    private static String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    //从本地缓存中获取图片
    public static File getBitmapFile(String url){
        String fileName = directory+"/wayApp/"+ url+".jpg";
        File file = new File(fileName);
        if(file.exists()){

            return file;
            // return BitmapFactory.decodeFile(fileName);
        }
        return  null;
    }
    //从本地缓存中获取图片
    public static Bitmap getBitmap(String url){
        String fileName = directory+"/wayApp/"+ url+".jpg";
        File file = new File(fileName);
        if(file.exists()){
//            Paint paint = new Paint();
//            paint.setColor(color);
            Bitmap bitmap = BitmapFactory.decodeFile(fileName);
//            Canvas canvas = new Canvas(bitmap);
//            Paint paint = new Paint();
//            paint.setAlpha(0x40); //设置透明程度
//            canvas.drawBitmap(bitmap, 0,0,paint);
            if(bitmap != null)
                return toRoundBitmap(bitmap);
           // return BitmapFactory.decodeFile(fileName);
        }
        return  null;
    }
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
        paint.setColor(color);

        // 以下有两种方法画圆,drawRounRect和drawCircle
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); //以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }
    public static void putBitmap(String url,Bitmap bitmap){
        File file = new File(directory, "/wayApp/"+ url+".jpg");
        if(!file.exists()){
           file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, bufferedOutputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


}
