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
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.ByteArrayAdapter;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.PersonalContract;
import com.caojian.myworkapp.ui.presenter.PersonalPresenter;
import com.caojian.myworkapp.widget.ChangeEditFragment;
import com.caojian.myworkapp.widget.HVCameraHunter;
import com.caojian.myworkapp.widget.HVGalleryHunter;
import com.caojian.myworkapp.widget.PersonalInstance;
import com.caojian.myworkapp.widget.SelectHvFragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PersonalActivity extends MvpBaseActivity<PersonalContract.View,PersonalPresenter> implements ChangeEditFragment.FragmentChangeListener,PersonalContract.View{

    public static void go2PersonalActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,PersonalActivity.class);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.img_head)
    ImageView mImgHead;
    @BindView(R.id.tv_name)
    TextView name;
//    @BindView(R.id.phone_num)
//    TextView phoneNum;
    @BindView(R.id.vip_msg)
    TextView vipMsg;
    Unbinder unbinder;
    ChangeEditFragment changeEditFragment;
    HVGalleryHunter galleryHunter;
    HVCameraHunter cameraHunter;
    SelectHvFragment selectHvFragment;
    PersonalPresenter mPresenter;
    private static final String[] MEMBERTYPE = {"非会员","正式会员","试用期会员"};
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

//        PersonalMsg personalMsg = PersonalInstance.getInstance().getPersonalMsg();
//        if(personalMsg != null){
//            initData(personalMsg.getData());
//        }else {
//            mPresenter.getPersonalInfo();
//        }
        mPresenter.getPersonalInfo();
    }

    //点击修改个人信息，跳出修改弹出框
    @OnClick(R.id.body_name)
    public void showChangeName()
    {
        changeEditFragment = ChangeEditFragment.newInstance("修改昵称","不能超过20个字","取消","确定");
        changeEditFragment.show(getSupportFragmentManager(),"name");
    }
    @OnClick(R.id.body_signature)
    public void showChangSignature()
    {
       // changeEditFragment.show(getSupportFragmentManager(),"name");
    }

    @Override
    public void cancelEdit() {
        changeEditFragment.dismiss();
        changeEditFragment = null;
    }

    @Override
    public void submitEdit(String msg) {
        changeEditFragment.dismiss();
        personal.setNickName(msg);
        mPresenter.changePersonal(personal);
        changeEditFragment = null;
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
                public void onCaptureSucceed(File path){
                    //                        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(path));
//                        ByteArrayOutputStream bas = new ByteArrayOutputStream();
//                        int point = 100;
//                        bitmap.compress(Bitmap.CompressFormat.JPEG,point,bas);
//                        while (bas.size()/1024 > 100){
//                            if(point < 10)
//                                break;
//                            point -= 10;
//                            bitmap.compress(Bitmap.CompressFormat.JPEG,point,bas);
//                        }
//                        //Glide.with(PersonalActivity.this).load(bitmap).into(mImgHead);
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                    mPresenter.uploadHeadPic(path);
                    // mImgHead.setImageBitmap(path);

                    Glide.with(PersonalActivity.this).load(path).into(mImgHead);
                   // InputStream inputStream =
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
                     //   InputStream inputStream = getContentResolver().openInputStream(imageFile);

                        //actualOutBitmap.compress
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFile));
                        ByteArrayOutputStream bas = new ByteArrayOutputStream();
                        int point = 100;
                        bitmap.compress(Bitmap.CompressFormat.JPEG,point,bas);
                        while (bas.size()/1024 > 100){
                                point -= 10;
                            bitmap.compress(Bitmap.CompressFormat.JPEG,point,bas);
                        }
                        //Glide.with(PersonalActivity.this).load(bitmap).into(mImgHead);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                      //  mPresenter.uploadHeadPic(getContentResolver().openInputStream(imageFile).);
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
    public PersonalPresenter createPresenter() {
        mPresenter = new PersonalPresenter(PersonalActivity.this,this);
        return mPresenter;
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

    PersonalMsg.DataBean personal;
    //请求信息成功，填充信息
    @Override
    public void getPersonalSuccess(PersonalMsg personalMsg) {
        initData(personalMsg.getData());
    }

    @Override
    public void changeMsgSuccess(String msg) {
        showToast(msg,Toast.LENGTH_SHORT);
        initData(personal);
    }
    @Override
    public void error(String errorMsg) {
        showToast(errorMsg, Toast.LENGTH_SHORT);
    }
    //进行有损压缩
//    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    int options_ = 100;
//actualOutBitmap.compress(Bitmap.CompressFormat.JPEG, options_, baos);//质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
//
//    int baosLength = baos.toByteArray().length;
//while (baosLength / 1024 > maxFileSize) {//循环判断如果压缩后图片是否大于maxMemmorrySize,大于继续压缩
//        baos.reset();//重置baos即让下一次的写入覆盖之前的内容
//        options_ = Math.max(0, options_ - 10);//图片质量每次减少10
//        actualOutBitmap.compress(Bitmap.CompressFormat.JPEG, options_, baos);//将压缩后的图片保存到baos中
//        baosLength = baos.toByteArray().length;
//        if (options_ == 0)//如果图片的质量已降到最低则，不再进行压缩
//            break;
//    }
    //填充信息
    private void initData( PersonalMsg.DataBean dataBean)
    {
        personal = dataBean;
        name.setText((dataBean.getNickName()==null)?"":dataBean.getNickName());
        vipMsg.setText(MEMBERTYPE[dataBean.getMemberType()-1]);
        if(!dataBean.getHeadPic().isEmpty())
        {
            Glide.with(PersonalActivity.this).load(dataBean.getHeadPic()).into(mImgHead);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
