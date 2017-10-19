package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.data.VipItem;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.fragment.BuyModelFragment;
import com.caojian.myworkapp.widget.ListDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.beecloud.BCPay;
import cn.beecloud.BeeCloud;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;
import cn.beecloud.entity.BCReqParams;

public class BuyVipActivity extends BaseTitleActivity implements ListDialogFragment.FragmentBuyListener {

    public static void go2BuyVipActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,BuyVipActivity.class);
        fromClass.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_vip);

        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("会员中心");


        BeeCloud.setAppIdAndSecret("f40dfec0-1a82-47c9-83a3-847735097111", "a26da064-703e-46cb-bfce-e1f54487e72d");
        // 如果需要开启测试模式
        // BeeCloud.setSandbox(true);
        // BeeCloud.setAppIdAndSecret("appId", "testSecret");

        // 如果用到微信支付，在用到微信支付的Activity的onCreate函数里调用以下函数.
        // 第二个参数需要换成你自己的微信AppID.
        String initInfo = BCPay.initWechatPay(BuyVipActivity.this, "wxa105e46c873b9b6e");
        if (initInfo != null) {
            Toast.makeText(this, "微信初始化失败：" + initInfo, Toast.LENGTH_LONG).show();
        }
        BeeCloud.setSandbox(true);
    }
    ListDialogFragment fragment;
    public void buy(View v){
        fragment = ListDialogFragment.newInstance("购买会员");
        fragment.show(getSupportFragmentManager(),"list");
        fragment.setmListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public void cancelBuy() {

    }

    @Override
    public void showBuy(VipItem item) {
        fragment.dismiss();
        BuyModelFragment modelFragment = BuyModelFragment.newInstance();
        modelFragment.show(getSupportFragmentManager(),"model");
        modelFragment.setListen(new BuyModelFragment.PayAction() {
            @Override
            public void pay(int model) {
                payWeiXin();
            }
        });
    }

    //微信支付
    private void payWeiXin()
    {
        //定义回调
        BCCallback bcCallback = new BCCallback() {
            @Override
            public void done(final BCResult bcResult) {
                //此处根据业务需要处理支付结果
                final BCPayResult bcPayResult = (BCPayResult)bcResult;

                switch (bcPayResult.getResult()) {
                    case BCPayResult.RESULT_SUCCESS:
                        //用户支付成功
                        break;
                    case BCPayResult.RESULT_CANCEL:
                        //用户取消支付"
                        break;
                    case BCPayResult.RESULT_FAIL:
                        //支付失败
                }
            }
        };

        //创建支付参数类
        BCPay.PayParams payParam = new BCPay.PayParams();
        // 发起的渠道类型
        payParam.channelType = BCReqParams.BCChannelTypes.WX_APP;
        //商品描述, 32个字节内, 汉字以2个字节计
        payParam.billTitle = "安卓微信支付测试";
        //支付金额，以分为单位，必须是正整数
        payParam.billTotalFee = 10;
        //商户自定义订单号
        payParam.billNum = "unique bill number";
        //发起支付
        BCPay.getInstance(getBaseContext()).reqPaymentAsync(payParam,
                bcCallback);
    }
}
