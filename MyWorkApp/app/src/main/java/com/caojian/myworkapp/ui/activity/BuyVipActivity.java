package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.data.VipItem;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.BuyVipContract;
import com.caojian.myworkapp.ui.fragment.BuyModelFragment;
import com.caojian.myworkapp.ui.presenter.BuyVipPresenter;
import com.caojian.myworkapp.widget.ListDialogFragment;
import com.caojian.myworkapp.widget.PersonalInstance;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.beecloud.BCPay;
import cn.beecloud.BeeCloud;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;

public class BuyVipActivity extends MvpBaseActivity<BuyVipContract.View,BuyVipPresenter> implements ListDialogFragment.FragmentBuyListener,BuyVipContract.View {
    public static void go2BuyVipActivity(Context fromClass,int buyKind)
    {
        Intent intent = new Intent(fromClass,BuyVipActivity.class);
        intent.putExtra("buyKind",buyKind);
        fromClass.startActivity(intent);
    }
    public static final int TYPE_WEIXIN = 1;
    public static final int TYPE_ZHIFUBAO = 2;
    public static final int TYPE_JIFEN = 3;
    public int mPayModel = 1;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_deadline)
    TextView mTvDeadLine;
    @BindView(R.id.buy_model1)
    RelativeLayout mBuyModel1;
    @BindView(R.id.buy_model2)
    RelativeLayout mBuyModel2;
    Unbinder unbinder;
    ListDialogFragment mListDialogFragment;
    BuyVipPresenter mPresenter;
    VipItem mBuyItem;
    int mBuyKind = 0; //0:购买基本会员，1,轨迹回放，2，电子围栏
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_vip);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("会员中心");
        mBuyKind = getIntent().getIntExtra("buyKind",1);
        PersonalMsg personalMsg = PersonalInstance.getInstance().getPersonalMsg();
        mTvName.setText(personalMsg.getData().getNickName());
        mTvDeadLine.setText(personalMsg.getData().getMemberEndTime());
        if(mBuyKind == 0){
            mBuyModel1.setVisibility(View.VISIBLE);
            mBuyModel2.setVisibility(View.INVISIBLE);
        }else {
            mBuyModel1.setVisibility(View.INVISIBLE);
            mBuyModel2.setVisibility(View.VISIBLE);
        }
         BeeCloud.setAppIdAndSecret("f40dfec0-1a82-47c9-83a3-847735097111", "a26da064-703e-46cb-bfce-e1f54487e72d");
//        // 如果需要开启测试模式
//        // BeeCloud.setSandbox(true);
//        // BeeCloud.setAppIdAndSecret("appId", "testSecret");
//
//        // 如果用到微信支付，在用到微信支付的Activity的onCreate函数里调用以下函数.
//        // 第二个参数需要换成你自己的微信AppID.
        String initInfo = BCPay.initWechatPay(BuyVipActivity.this, "wxa105e46c873b9b6e");
        if (initInfo != null) {
            Toast.makeText(this, "微信初始化失败：" + initInfo, Toast.LENGTH_LONG).show();
        }
    }

    //按钮点击
    public void buy(View v){
        if(mBuyKind > 0){  //购买增值服务直接付款
            VipItem item = new VipItem();
            item.setType(mBuyKind);
            item.setName("");
            item.setPrice(1000);
            showBuy(item);
            return;
        }
        //购买基础会员 需要选择类型
        mListDialogFragment = ListDialogFragment.newInstance("购买会员");
        mListDialogFragment.show(getSupportFragmentManager(),"list");
        mListDialogFragment.setmListener(this);
    }
    @Override
    public BuyVipPresenter createPresenter() {
        mPresenter = new BuyVipPresenter(BuyVipActivity.this,this);
        return mPresenter;
    }


    @Override
    public void cancelBuy() {
        mListDialogFragment.dismiss();
    }

    private BuyModelFragment modelFragment;
    @Override
    public void showBuy(VipItem item) {
        if(mListDialogFragment != null)
            mListDialogFragment.dismiss();
        mBuyItem = item;
        PersonalMsg personalMsg = PersonalInstance.getInstance().getPersonalMsg();
        if(personalMsg == null){
            modelFragment = BuyModelFragment.newInstance(1);
        }else {
            if(item.getPrice() > Double.parseDouble(personalMsg.getData().getRewardScore()+""))
            {
                modelFragment = BuyModelFragment.newInstance(1);
            }else {
                modelFragment = BuyModelFragment.newInstance(2);
            }
        }
        modelFragment.setListen(new BuyModelFragment.PayAction() {
            @Override
            public void pay(int model) {
                mPayModel = model;
                modelFragment.dismiss();  //隐藏fragment
                if(mPayModel == TYPE_JIFEN){
                    payJiFen();
                }
                mPresenter.getOrderNum(mBuyKind+"",(mBuyKind==0)?item.getType()+"":"");
            }
        });
        modelFragment.show(getSupportFragmentManager(),"model");
    }

    //微信支付
    private void payWeiXin(String orderNum)
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
                        // TODO: 2017/12/14  修改会员类型
                        break;
                    case BCPayResult.RESULT_CANCEL:
                        //用户取消支付"
                        showToast("用户取消支付",Toast.LENGTH_LONG);
                        break;
                    case BCPayResult.RESULT_FAIL:
                        //支付失败
                        showToast("支付失败",Toast.LENGTH_LONG);
                }
            }
        };
        Map<String, String> mapOptional = new HashMap<String, String>();
        mapOptional.put("testkey1", "测试value值1");

        if (BCPay.isWXAppInstalledAndSupported() &&
                BCPay.isWXPaySupported()) {

            BCPay.getInstance(BuyVipActivity.this).reqWXPaymentAsync(
                    "购买"+mBuyItem.getName()+"会员",               //订单标题
                    1,                        //mBuyItem.getPrice()*100   //订单金额(分)
                    orderNum,  //订单流水号
                    mapOptional,            //扩展参数(可以null)
                    bcCallback);            //支付完成后回调入口

        } else {
            Toast.makeText(BuyVipActivity.this,
                    "您尚未安装微信或者安装的微信版本不支持", Toast.LENGTH_LONG).show();
        }
//        //创建支付参数类
//        BCPay.PayParams payParam = new BCPay.PayParams();
//        // 发起的渠道类型
//        payParam.channelType = BCReqParams.BCChannelTypes.WX_APP;
//        //商品描述, 32个字节内, 汉字以2个字节计
//        payParam.billTitle = "安卓微信支付测试";
//        //支付金额，以分为单位，必须是正整数
//        payParam.billTotalFee = 10;
//        //商户自定义订单号
//        payParam.billNum = "111111111111111111";
//        //发起支付
//        BCPay.getInstance(BuyVipActivity.this).reqPaymentAsync(payParam,
//                bcCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideProgress();
    }

    @Override
    public void getOrderNumSuccess(String orderNum) {
        switch (mPayModel)
        {
            case TYPE_WEIXIN:
                showProgress(BuyVipActivity.this);
                payWeiXin(orderNum);
                break;
            case TYPE_ZHIFUBAO:
                break;
            case TYPE_JIFEN:
//                showProgress(BuyVipActivity.this);
//                payJiFen();
                break;
        }

    }

    private void payJiFen() {
        if(mBuyKind > 0)
        {
            mPresenter.buyValueAddedServiceByRewardScore(mBuyKind+"");
        }else {
            mPresenter.buyVipByJiFen(mBuyItem.getType() + "");
        }
    }

    @Override
    public void buySuccess(String msg) {
        showToast(msg,Toast.LENGTH_SHORT);
    }

    @Override
    public void error(String errorMsg) {
        showToast(errorMsg,Toast.LENGTH_SHORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
