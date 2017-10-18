package com.caojian.myworkapp.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.BuildConfig;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by CJ on 15/11/15.
 */
public class HVCameraHunter {

  private Context mContext;

  public static final int REQUEST_TAKE_PHOTO = 34;

  public static final String TEMP_PHOTO_FILE_URI = "temp_photo_file_uri";

  public interface Callback {

    void onCapturePhotoFailed(Exception error);

    //void onCaptureSucceed(File imageFile);
    void onCaptureSucceed(Uri imageFile);

    void onCanceled();
  }

  public HVCameraHunter(Context context) {
    this.mContext = context;
  }

  /**
   * 启动相机
   */
  public void openCamera() {
    Intent intent = createCameraIntent();
    ((Activity) mContext).startActivityForResult(intent, REQUEST_TAKE_PHOTO);
  }

  /**
   * 在 onActivityResult 中调用
   *
   * @param requestCode 请求码
   * @param resultCode 结果码
   * @param callback 回调接口
   */
  public void handleActivityResult(int requestCode, int resultCode, Callback callback) {

    switch (requestCode) {
      case REQUEST_TAKE_PHOTO:
        if (resultCode == Activity.RESULT_OK) {
          onCapturePhotoFromCamera(callback);
        } else if (resultCode == Activity.RESULT_CANCELED) {
          onCancelCapturePhoto(callback);
        }
        break;
      default:
        break;
    }
  }

  /**
   * 取消使用拍照的图片
   */
  private void onCancelCapturePhoto(Callback callback) {
    //try {
      //File photoFile = getPhotoFile();
      callback.onCanceled();
    //} catch (URISyntaxException e) {
     // e.printStackTrace();
   // }
  }

  /**
   *
   *
   * @param callback
   */
  private void onCapturePhotoFromCamera(Callback callback) {
    try {
      //File photoFile = getPhotoFile();
      callback.onCaptureSucceed(getPhotoURI());
    } catch (URISyntaxException e) {
      callback.onCapturePhotoFailed(e);
    }
  }

  /**
   * 创建启动 Camera 的 Intent
   *
   * @return Intent
   */
  private Intent createCameraIntent() {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    try {
      Uri capturedPhotoUri = createCameraPictureFileUri();
      intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedPhotoUri);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return intent;
  }

  /**
   * 创建保存图片的路径
   *
   * @throws IOException
   */
  private Uri createCameraPictureFileUri() throws IOException {
   // String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
    String timeStamp = new Date().getTime()+"";
    // 图片前缀
    String tempFilePrefix = "JPEG" + timeStamp;
    // 图片目录
    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    // 创建一个空文件
    File photoFile = File.createTempFile(tempFilePrefix, ".jpg", storageDir);
    Uri photoUri;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      photoUri = FileProvider.getUriForFile(mContext, "com.caojian.myworkapp.fileProvider", photoFile);
    } else {
      photoUri = Uri.fromFile(photoFile);
    }

    savePhotoFileUri(photoUri);
    return photoUri;
  }

  /**
   * 将文件的 uri 保存到本地
   */
  private void savePhotoFileUri(Uri fileUri) {
    SharedPreferences.Editor editor =
        PreferenceManager.getDefaultSharedPreferences(mContext).edit();
    editor.putString(TEMP_PHOTO_FILE_URI, fileUri.toString()).commit();
  }

  /**
   * 得到图片 File
   *
   * @throws URISyntaxException
   */
  private File getPhotoFile() throws URISyntaxException {
//    URI photoUri = new URI(
//        PreferenceManager.getDefaultSharedPreferences(mContext).getString(TEMP_PHOTO_FILE_URI, ""));
    //addPhotoToGallery(photoUri);
    Uri uri = Uri.parse(PreferenceManager.getDefaultSharedPreferences(mContext).getString(TEMP_PHOTO_FILE_URI, ""));


        String[] proj = { MediaStore.Images.Media.DATA };
         Cursor actualimagecursor = ((Activity)mContext).managedQuery(uri,proj,null,null,null);
         int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
         actualimagecursor.moveToFirst();


    String img_path = actualimagecursor.getString(actual_image_column_index);
    File file = new File(img_path);
    return file;
  }
  private Uri getPhotoURI() throws URISyntaxException {
    Uri photoUri =  Uri.parse(PreferenceManager.getDefaultSharedPreferences(mContext).getString(TEMP_PHOTO_FILE_URI, ""));
    return photoUri;
  }

  /**
   * 通知 Gallery 更新
   */
  private void addPhotoToGallery(URI photoUri) {
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File file = new File(photoUri);
    Uri contentUri = Uri.fromFile(file);
    mediaScanIntent.setData(contentUri);
    mContext.sendBroadcast(mediaScanIntent);
  }
}
