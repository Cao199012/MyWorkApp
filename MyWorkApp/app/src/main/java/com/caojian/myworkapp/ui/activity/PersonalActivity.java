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
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;
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

import static com.caojian.myworkapp.until.ActivityUntil.changeFilePath;
import static com.caojian.myworkapp.until.ActivityUntil.myCheckPermission;

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
    @BindView(R.id.vip_rail)
    TextView vipRail;
    @BindView(R.id.vip_track)
    TextView vipTrack;
    @BindView(R.id.level_one_num)
    TextView mLevel_one_num;
    @BindView(R.id.level_two_num)
    TextView mLevel_two_num;
    Unbinder unbinder;
    ChangeEditFragment changeEditFragment;
    HVGalleryHunter galleryHunter;
    HVCameraHunter cameraHunter;
    SelectHvFragment selectHvFragment;
    PersonalPresenter mPresenter;
    private static final String[] MEMBERTYPE = {"非会员","正式会员","正式会员"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("个人信息");
        galleryHunter = new HVGalleryHunter(PersonalActivity.this);
        cameraHunter = new HVCameraHunter(PersonalActivity.this);
        selectHvFragment = SelectHvFragment.newInstance();
        selectHvFragment.setSelectHvKind(new SelectHvFragment.SelectHvKind() {
            @Override
            public void selectGallery() {
                selectHvFragment.dismiss();
                galleryHunter.openGallery();
            }
            @Override
            public void selectCamera() {
                selectHvFragment.dismiss();
                cameraHunter.openCamera();
            }
        });

      //  mPresenter.getPersonalInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(personal == null)
        {
            mPresenter.getPersonalInfo();
        }else {
            //购买会员后更新
            personal = PersonalInstance.getInstance().getPersonalMsg();
            initData(personal.getData());
        }


    }

    //点击修改个人信息，跳出修改弹出框
    @OnClick(R.id.body_name)
    public void showChangeName()
    {
        changeEditFragment = ChangeEditFragment.newInstance("修改昵称","不能超过20个字","取消","确定");
      //  changeEditFragment.show(getSupportFragmentManager(),"name");
        ActivityUntil.showDialogFragment(getSupportFragmentManager(),changeEditFragment,"name");

    }
    @OnClick(R.id.body_signature)
    public void showChangSignature()
    {
        if(personal == null)
        {
            mPresenter.getPersonalInfo();
        }else {
            BuyVipActivity.go2BuyVipActivity(PersonalActivity.this,0);
        }

    }
    @OnClick(R.id.body_rail) //围栏报警
    public void buyRail()
    {
        if(personal == null)
        {
            return;
        }
        BuyVipActivity.go2BuyVipActivity(PersonalActivity.this,2);
    }
    @OnClick(R.id.body_track)  //轨迹回放
    public void buyTrack()
    {
        if(personal == null)
        {
            return;
        }
        BuyVipActivity.go2BuyVipActivity(PersonalActivity.this,1);  //1 轨迹回放

    }

    @Override
    public void cancelEdit() {
        changeEditFragment.dismiss();
        changeEditFragment = null;
    }

    @Override
    public void submitEdit(String msg) {
        changeEditFragment.dismiss();
        personal.getData().setNickName(msg);
        name.setText(msg);
        mPresenter.changePersonal(personal.getData());
        changeEditFragment = null;
    }
    //选择头像图片
    @OnClick(R.id.img_head)
    public void selectImg(){
        String[] pers = myCheckPermission(PersonalActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA});
        if(pers.length > 0)
        {
            ActivityCompat.requestPermissions(PersonalActivity.this,pers,1);
        }else
        {
            SelectHvFragment.showSelectDialog(getSupportFragmentManager(),selectHvFragment,"hv");
        }
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
                    mPresenter.uploadHeadPic(changeFilePath(path,null));
                    Glide.with(PersonalActivity.this).load(path).into(mImgHead);
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
                        mImgHead.setImageBitmap(bitmap);
                        mPresenter.uploadHeadPic(changeFilePath(null,bitmap));

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
                SelectHvFragment.showSelectDialog(getSupportFragmentManager(),selectHvFragment,"hv");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    PersonalMsg personal;
    //请求信息成功，填充信息
    @Override
    public void getPersonalSuccess(PersonalMsg personalMsg) {
        personal = PersonalInstance.getInstance().getPersonalMsg();
        initData(personal.getData());
    }

    @Override
    public void changeMsgSuccess(String msg) {
        showToast(msg,Toast.LENGTH_SHORT);
       // mPresenter.getPersonalInfo();
    }
    @Override
    public void error(String errorMsg) {

        showToast(errorMsg, Toast.LENGTH_SHORT);
    }
    //填充信息
    private void initData( PersonalMsg.DataBean dataBean)
    {
        name.setText((dataBean.getNickName()==null)?"未设置":dataBean.getNickName());
        vipMsg.setText(MEMBERTYPE[dataBean.getMemberType()-1]);
        if(!dataBean.getFenceService().equals("2"))
        {
            vipRail.setText("已购买");
        }else{
            vipRail.setText("去购买");
        }
        if(!dataBean.getTrajectoryService().equals("2"))
        {
            vipTrack.setText("已购买");
        }else{
            vipTrack.setText("去购买");
        }
        if(!dataBean.getHeadPic().isEmpty())
        {
            Glide.with(PersonalActivity.this).load(Until.HTTP_BASE_IMAGE_URL+dataBean.getHeadPic()).into(mImgHead);
        }
        mLevel_one_num.setText(dataBean.getNextLevelCount());
        mLevel_two_num.setText(dataBean.getUnderNextLevelCount());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
