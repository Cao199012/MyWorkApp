package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.UpdateResponse;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.caojian.myworkapp.ui.activity.UpdateActivity.go2UpdateActivity;

public class AboutAppActivity extends BaseTitleActivity {
    public static void go2AboutAPPActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,AboutAppActivity.class);
        fromClass.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        ButterKnife.bind(this);
        mToolbar.setTitle("关于我们");
    }

    public void goFunction(View view)
    {
        BuyVipActivity.go2BuyVipActivity(AboutAppActivity.this,0);
    }

    private void checkVersion(Context context) {
        //                MainActivity.go2MainActivity(SplashActivity.this);
//                finish();
        UpdateResponse.DataBean dataBean = new UpdateResponse.DataBean();
        dataBean.setComment("更新内容说明");
        dataBean.setMandatory("0");
        go2UpdateActivity(AboutAppActivity.this,dataBean,101);
//        Retrofit retrofit = RetrofitManger.getRetrofitRxjava(Until.HTTP_BASE_URL);
//        UpdateService updateService = retrofit.create(UpdateService.class);
//        Observable<UpdateResponse> observable = updateService.getUpdateMsg(getVersionCode(context),Until.TREMINALtYPE);
//        disposable = observable.observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(new Consumer<UpdateResponse>() {
//            @Override
//            public void accept(UpdateResponse updateMsg) throws Exception {
//                if(Integer.parseInt(updateMsg.getCode(),1) == 0)
//                {
//                    UpdateResponse.DataBean dataBean = updateMsg.getData();
//                    if(dataBean != null && dataBean.equals("1"))
//                    {
//                        //显示更新提示窗口
//                        go2UpdateActivity(context,dataBean,101);
//                    }else
//                    {
//                        go2NextActivity();
//                    }
//
//                }else
//                {
//                    showToast(context,updateMsg.getMessage(), Toast.LENGTH_SHORT);
//                }
//            }
//        });
    }
}
