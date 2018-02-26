package com.caojian.myworkapp.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.caojian.myworkapp.ui.activity.BuyVipActivity.TYPE_JIFEN;
import static com.caojian.myworkapp.ui.activity.BuyVipActivity.TYPE_WEIXIN;

/**
 * Created by CJ on 2017/9/8.
 */

public class BuyModelFragment extends AppCompatDialogFragment  {

    public static BuyModelFragment newInstance(int type)
    {
        BuyModelFragment fragment = new BuyModelFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }
    @BindView(R.id.tv_jifen)
    TextView mTv_jifenShow;
    @BindView(R.id.check_weixin)
    AppCompatCheckBox mCheckWeiXin;
    @BindView(R.id.check_jifen)
    AppCompatCheckBox mCheckJiFen;
    @BindView(R.id.check_zhifubao)
    AppCompatCheckBox mCheckZhi;
    AppCompatCheckBox mPreCheckBox;
    @BindView(R.id.model2)
    RelativeLayout modelZhi;
    private PayAction payAction = null;
    private int payType = TYPE_WEIXIN;
    private int modelType = 1;  //代表积分余额不足必须第三方支付
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            modelType = getArguments().getInt("type");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_model_list,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        modelZhi.setVisibility(View.GONE);
        mCheckWeiXin.setChecked(true);  //默认勾选微信支付
        mPreCheckBox = mCheckWeiXin;
        mCheckWeiXin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true && mPreCheckBox != mCheckWeiXin) {
                    if(mPreCheckBox != null) {  //如果之前有勾选 吧前一个取消勾选
                        mPreCheckBox.setChecked(false);
                    }
                    mPreCheckBox = mCheckWeiXin;
                    payType = TYPE_WEIXIN;
                } else {
                    mPreCheckBox = null;
                    payType = -1; //取消支付方式置-1
                }
            }
        });
        if(modelType == 1){  //
            mCheckJiFen.setChecked(true);
            mCheckJiFen.setEnabled(false);
            mTv_jifenShow.append(" (余额不足)");
        }else {
            mCheckJiFen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked == true && mPreCheckBox != mCheckJiFen){
                        if(mPreCheckBox != null) {
                            mPreCheckBox.setChecked(false);
                        }
                        mPreCheckBox = mCheckJiFen;
                        payType = TYPE_JIFEN;
                    }else {
                        mPreCheckBox = null;
                        payType = -1;  //取消支付方式置-1
                    }
                }
            });
        }

    }
    public void setListen(PayAction payAction)
    {
        this.payAction = payAction;
    }
    @OnClick(R.id.btn_pay)
    public void toPay(){
        if(mPreCheckBox == null)
        {
            ((BaseTitleActivity)getActivity()).showToast("请选择支付方式", Toast.LENGTH_SHORT);
        }
        if(payAction != null)
        {
            payAction.pay(payType);
        }
    }
    @Override
    public void onAttach(Context context) {
//        if(context instanceof FragmentDialogListener)
//        {
//            mListrner = (FragmentDialogListener) context;
//        }else {
//            throw new RuntimeException("activity必须implements接口");
//        }
         super.onAttach(context);
    }

    public interface PayAction{
        void pay(int model);
    }

}
