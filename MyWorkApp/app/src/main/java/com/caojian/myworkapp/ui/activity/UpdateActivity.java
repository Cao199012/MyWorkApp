package com.caojian.myworkapp.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.UpdateResponse;

import com.caojian.myworkapp.manager.ActivityControl;
import com.caojian.myworkapp.services.DownLoadService;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.until.ActivityUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.caojian.myworkapp.until.ActivityUntil.myCheckPermission;

/**
 * Created by CJ on 2017/8/16.
 */

public class UpdateActivity extends BaseTitleActivity {

    public static void go2UpdateActivity(BaseTitleActivity from, UpdateResponse.DataBean data, int requestCode)
    {
        Intent intent = new Intent(from,UpdateActivity.class);
        intent.putExtra("databean",data);
        from.startActivityForResult(intent,requestCode);
    }
    @BindView(R.id.tv_update_comments)
    TextView mTv_comments;
    private UpdateResponse.DataBean dataBean;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        unbinder = ButterKnife.bind(this);
        initVariable();
        if(dataBean.getComment() != null)
        {
            mTv_comments.setText(dataBean.getComment());
        }else
        {
            mTv_comments.setText("新版上线");
        }

    }

    private void initVariable() {
        dataBean = (UpdateResponse.DataBean) getIntent().getSerializableExtra("databean");
    }

    /**
     * 暂不更新
     * @param view
     */
    public void updateForLater(View view)
    {
        if(dataBean == null)
        {
            // TODO: 2017/8/18
            return;
        }else {
            if(dataBean.getMandatory().equals("1"))
            {
                ActivityControl.finishActivity();
                return;
            }

            if (dataBean.getMandatory().equals("0"))
            {
                finish();
            }
        }
    }

    public void updateForNow(View v)
    {
        // TODO: 2017/8/18启动服务 下载应用
        String[] pers = myCheckPermission(UpdateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE});
        if(pers.length > 0)
        {
            ActivityCompat.requestPermissions(UpdateActivity.this,pers,1);

        }else
        {
//            binder.startDown("http://download.cntv.cn/app/cntv/cbox_androidguanwang_v6.3.1.1.apk");
            //
            Intent intent = new Intent(UpdateActivity.this,DownLoadService.class);
            intent.putExtra("url",dataBean.getDownLoadAddr());
            startService(intent);
            ActivityControl.finishActivity();
        }

      //  bindService(intent,connection,BIND_AUTO_CREATE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if(grantResults.length > 0)
                {
                    for (int result :
                            grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED)
                        {
                            showToast("拒绝权限无法使用程序", Toast.LENGTH_SHORT);
                            finish();
                            return;
                        }
                    }
                    Intent intent = new Intent(UpdateActivity.this,DownLoadService.class);
                    startService(intent);
                    intent.putExtra("url",dataBean.getDownLoadAddr());
                    finish();
//                    binder.startDown("");
//                    ActivityControl.finishActivity();

                }
        }
    }



    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
//        unbindService(connection);
    }

}
