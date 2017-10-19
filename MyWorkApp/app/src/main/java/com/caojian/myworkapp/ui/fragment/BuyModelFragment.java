package com.caojian.myworkapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caojian.myworkapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CJ on 2017/9/8.
 */

public class BuyModelFragment extends AppCompatDialogFragment  {

    public static BuyModelFragment newInstance()
    {
        BuyModelFragment fragment = new BuyModelFragment();

       // fragment.setArguments(bundle);
        return fragment;
    }
    private PayAction payAction = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

    }
    public void setListen(PayAction payAction)
    {
        this.payAction = payAction;
    }
    @OnClick(R.id.btn_pay)
    public void toPay(){
        if(payAction != null)
        {
            payAction.pay(1);
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