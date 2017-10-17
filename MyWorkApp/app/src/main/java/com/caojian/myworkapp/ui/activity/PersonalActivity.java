package com.caojian.myworkapp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.widget.ChangeEditFragment;
import com.caojian.myworkapp.widget.HVCameraHunter;
import com.caojian.myworkapp.widget.HVGalleryHunter;
import com.caojian.myworkapp.widget.SelectHvFragment;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PersonalActivity extends BaseTitleActivity implements ChangeEditFragment.FragmentChangeListener{

    public static void go2PersonalActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,PersonalActivity.class);
        fromClass.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.img_head)
    ImageView mImgHead;

    Unbinder unbinder;
    ChangeEditFragment changeEditFragment;
    HVGalleryHunter galleryHunter;
    HVCameraHunter cameraHunter;

    SelectHvFragment selectHvFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("个人信息");

        changeEditFragment = ChangeEditFragment.newInstance("修改个人信息","不能超过20个字","取消","确定");
        galleryHunter = new HVGalleryHunter(PersonalActivity.this);
        cameraHunter = new HVCameraHunter(PersonalActivity.this);
        selectHvFragment = SelectHvFragment.newInstance();
        selectHvFragment.setSelectHvKind(new SelectHvFragment.SelectHvKind() {
            @Override
            public void selectGallery() {
                galleryHunter.openGallery();
            }

            @Override
            public void selectCamera() {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(PersonalActivity.this,new String[]{Manifest.permission.CAMERA},1);
                    return;
                }
                cameraHunter.openCamera();
            }
        });


    }


    //点击修改个人信息，跳出修改弹出框
    @OnClick(R.id.body_name)
    public void showChangeName()
    {
        changeEditFragment.show(getSupportFragmentManager(),"name");
    }
    @OnClick(R.id.body_signature)
    public void showChangSignature()
    {
        changeEditFragment.show(getSupportFragmentManager(),"name");
    }
    //选择头像图片
    @OnClick(R.id.img_head)
    public void selectImg(){
        selectHvFragment.show(getSupportFragmentManager(),"hv");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == HVGalleryHunter.REQUEST_CAPTURE_PHOTO_FROM_GALLERY)
        {
            galleryHunter.handleActivityResult(requestCode, resultCode, data, new HVGalleryHunter.Callback() {
                @Override
                public void onCapturePhotoFailed(Exception error) {

                }

                @Override
                public void onCaptureSucceed(File path) {
                    Glide.with(PersonalActivity.this).load(path).into(mImgHead);
                   // Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(path))
                }

                @Override
                public void onCanceled() {

                }
            });
        }

        if(requestCode == HVCameraHunter.REQUEST_TAKE_PHOTO)
        {
            cameraHunter.handleActivityResult(requestCode, resultCode, new HVCameraHunter.Callback() {
                @Override
                public void onCapturePhotoFailed(Exception error) {

                }

                @Override
                public void onCaptureSucceed(Uri imageFile) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFile));
                        //Glide.with(PersonalActivity.this).load(bitmap).into(mImgHead);
                        mImgHead.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCanceled() {

                }
            });
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1)
        {
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                cameraHunter.openCamera();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void cancelEdit() {
        changeEditFragment.dismiss();
    }

    @Override
    public void sumbitEdit() {
        // TODO: 2017/9/24
    }
}
