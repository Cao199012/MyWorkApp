package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.data.VipItem;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.BuyVipContract;
import com.caojian.myworkapp.widget.BuyModelFragment;
import com.caojian.myworkapp.ui.presenter.BuyVipPresenter;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.ListDialogFragment;
import com.caojian.myworkapp.widget.PersonalInstance;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.beecloud.BCPay;
import cn.beecloud.BeeCloud;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;
/*
*
* 购买会员和增值服务
* */
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
    @BindView(R.id.img_head)
    ImageView mImgHead;
    @BindView(R.id.tv_deadline)
    TextView mTvDeadLine;
    @BindView(R.id.function_des)
    TextView mFunction_des;
    @BindView(R.id.buy_model1)
    RelativeLayout mBuyModel1;
    @BindView(R.id.buy_model2)
    RelativeLayout mBuyModel2;
    @BindView(R.id.error_layout)
    RelativeLayout mErrorModel;
    @BindView(R.id.pay_kind)
    TextView mPay_kind;
    @BindView(R.id.pay_num)
    TextView mPay_num;
    @BindView(R.id.vip_buy)
    Button mPay_Button;
    @BindView(R.id.btn_renew)
    TextView mButtonRenew;
    Unbinder unbinder;
    ListDialogFragment mListDialogFragment;
    BuyVipPresenter mPresenter;
    VipItem mBuyItem;
    int mBuyKind = 0; //0:购买基本会员，1,轨迹回放，2，电子围栏
    PersonalMsg personalMsg;
//    功能提示: 为至爱亲友设置电子围栏,一旦超出立即收到警示.
// 有效期: XXXX年XX月XX日 总费用:XX元,可通过微信、积分及各种银卡卡支付。
    //功能提示: 随时查看自己或好友的足迹，分享旅途。
// 有效期: XXXX年XX月XX日  总费用:XX元,可通过微信、积分及各种银卡卡支付。
    private static final String[] PAY_KINDS = {"轨迹回放增值服务\n到期时间：","围栏报警增值服务\n到期时间："};
    private static final String[] DES_KINDS = {"随时查看自己或好友的足迹，分享旅途","为至爱亲友设置电子围栏,一旦超出立即收到警示."};
    private static final String[] TITLE_KIND = {"购买会员","购买轨迹","购买围栏"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_vip);
        unbinder = ButterKnife.bind(this);

        mBuyKind = getIntent().getIntExtra("buyKind",1);
        mToolbar.setTitle(TITLE_KIND[mBuyKind]);
        mPay_Button.setText(TITLE_KIND[mBuyKind]);
        //个人信息会存在，获取不到的情况。
      //  personalMsg = PersonalInstance.getInstance().getPersonalMsg();
        mPresenter.getPersonalInfo(); //获取最新个人信息
//        if(personalMsg != null){
//          initPerson();
//        }else {
//
//        }
        if(mBuyKind == 0){  //根据购买类型显示不同界面
            mBuyModel1.setVisibility(View.VISIBLE);
            mBuyModel2.setVisibility(View.INVISIBLE);

        }else {  //显示是购买增值服务
            mBuyModel1.setVisibility(View.INVISIBLE);
            mBuyModel2.setVisibility(View.VISIBLE);
            mFunction_des.setText(DES_KINDS[mBuyKind - 1]);
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

    //初始化个人信息
    private void initPerson(){
        if(personalMsg == null){ //个人信息，没有显示错误页面。防止错买
            mErrorModel.setVisibility(View.VISIBLE);
            return;
        }
        mErrorModel.setVisibility(View.GONE);
        mTvName.setText(personalMsg.getData().getNickName());
        mTvDeadLine.setText(personalMsg.getData().getMemberEndTime());
        if(mBuyKind == 0){

            if(!personalMsg.getData().getHeadPic().isEmpty())
            {
                Glide.with(BuyVipActivity.this).load(Until.HTTP_BASE_URL+"downLoadPic.do?fileId="+personalMsg.getData().getHeadPic()).into(mImgHead);
            }
            if(personalMsg.getData().getMemberType() == 2){
                mButtonRenew.setVisibility(View.VISIBLE);
                mPay_Button.setVisibility(View.GONE);
            }else {
                mButtonRenew.setVisibility(View.GONE);
                mPay_Button.setVisibility(View.VISIBLE);
            }
        }else {  //显示是购买增值服务
            mPay_kind.setText(PAY_KINDS[mBuyKind-1]+personalMsg.getData().getMemberEndTime());
            if(personalMsg != null)
            {
                mPresenter.getOrderTime(mBuyKind+""); //获取增值服务时间

            }
        }
    }
    @OnClick(R.id.go_detail)
    public void go2Detail(){
        // TODO: 2017/12/9 查看充值记录
        RechargeInfoDetailActivity.go2RechargeInfoDetailActivity(BuyVipActivity.this);
    }
    //按钮点击
    VipItem item = new VipItem();
    @OnClick(R.id.vip_buy) //点击购买按钮
    public void buy(View v){
        if(mBuyKind > 0){  //购买增值服务直接付款

            showBuy(item);
            return;
        }
        //购买基础会员 需要选择类型
        mListDialogFragment = ListDialogFragment.newInstance("购买会员");
        mListDialogFragment.show(getSupportFragmentManager(),"list");
        mListDialogFragment.setmListener(this);
    }

    //刷新个人信息按钮
    public void refresh(View view){
        mPresenter.getPersonalInfo();
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
        if(personalMsg == null){
            modelFragment = BuyModelFragment.newInstance(1);
        }else {
            if(item.getPrice() > Double.parseDouble(personalMsg.getData().getRewardScore()+"")*100)
            {
                modelFragment = BuyModelFragment.newInstance(1);
            }else {
                modelFragment = BuyModelFragment.newInstance(2);
            }
        }
        modelFragment.setListen(new BuyModelFragment.PayAction() {
            @Override
            public void pay(int model) {
                if(model == -1)
                {
                    showToast("请选择支付方式",Toast.LENGTH_SHORT);
                    return;
                }
                mPayModel = model;
                modelFragment.dismiss();  //隐藏fragment
                if(mPayModel == TYPE_JIFEN){
                    payJiFen();
                    return;
                }
                mPresenter.getOrderNum(mBuyKind+"",(mBuyKind==0)?item.getType()+"":"");
            }
        });
      //  modelFragment.show(getSupportFragmentManager(),"model");
        ActivityUntil.showDialogFragment(getSupportFragmentManager(),modelFragment,"model");
    }

    //微信支付   (订单号，支付金额)
    private void payWeiXin(String orderNum,String amount)
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
                        buySuccess("支付成功");
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
        mapOptional.put("order", "支付");

        if (BCPay.isWXAppInstalledAndSupported() &&
                BCPay.isWXPaySupported()) {

            BCPay.getInstance(BuyVipActivity.this).reqWXPaymentAsync(
                    "购买"+mBuyItem.getName()+"会员",               //订单标题
                    mBuyItem.getPrice(),                        //   //订单金额(分)
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
    public void getOrderNumSuccess(String orderNum,String amount) {
        switch (mPayModel)
        {
            case TYPE_WEIXIN:
                showProgress(BuyVipActivity.this);
                payWeiXin(orderNum,amount);
                break;
            case TYPE_ZHIFUBAO:
                break;
            case TYPE_JIFEN:
//                showProgress(BuyVipActivity.this);
//                payJiFen();
                break;
        }

    }

    @Override
    public void getOrderTimeSuccess(String startTime, String endTime) {
        if(endTime != null){
            try {
                Date date1 = ActivityUntil.parseStringToDate(personalMsg.getData().getMemberEndTime(),"yyyy-MM-dd hh:mm:ss");
                Date date2 = ActivityUntil.parseStringToDate(endTime,"yyyy-MM-dd hh:mm:ss");
                int i = differentDaysByMillisecond(date2,date1);
                if(i == -1)
                {
                    mPay_num.setText("已到购买期限");
                    mPay_Button.setVisibility(View.GONE);
                    return;
                }
                item.setType(mBuyKind); //创建购买类
                item.setName("");
                item.setPrice((i*10)); //价格需要后台获取
                mPay_num.setText((float)(i*0.1)+"元  可通过微信、积分支付");
               // mPay_num.setText(i+"元");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            try {
                Date date1 = ActivityUntil.parseStringToDate(personalMsg.getData().getMemberEndTime(),"yyyy-MM-dd hh:mm:ss");
                Date date2 = new Date();
                int i = differentDaysByMillisecond(date2,date1);
                if(i == -1) //如果 已到当前
                {
                    mPay_num.setText("");
                    mPay_num.setText("已到购买期限");
                    mPay_Button.setVisibility(View.GONE);
                    return;
                }
                item.setType(mBuyKind); //创建购买类
                item.setName("");
                item.setPrice((int) (i*10)); //价格需要后台获取
                mPay_num.setText((float) (i*0.1)+"元  可通过微信、积分支付");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //获取信息成功
    @Override
    public void getPersonalSuccess(PersonalMsg personalMsg) {
        this.personalMsg = PersonalInstance.getInstance().getPersonalMsg();
        initPerson();
    }

    @Override
    public void getPersonalError(String msg) {
        this.personalMsg = null;  //请求个人信息失败，置空
        initPerson();
        showToast(msg,Toast.LENGTH_SHORT);

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

        //购买
        switch (mBuyKind){
            case 0:
                personalMsg.getData().setMemberType(2);
                break;
            case 1:
                personalMsg.getData().setTrajectoryService("1");
                break;
            case 2:
                personalMsg.getData().setFenceService("1");
                break;
        }
        setResult(RESULT_OK);
        finish();

    }

    @Override
    public void error(String errorMsg) {
        showToast(errorMsg,Toast.LENGTH_SHORT);
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2)
    {
        long s = date2.getTime() - date1.getTime();
        if((date2.getTime() - date1.getTime()) > 0)
        {
           return (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        }
        return -1;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
