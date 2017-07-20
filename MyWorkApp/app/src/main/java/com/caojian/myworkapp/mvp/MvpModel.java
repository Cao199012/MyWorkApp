package com.caojian.myworkapp.mvp;

import com.caojian.myworkapp.mvp.MvpContract;

/**
 * Created by CJ on 2017/7/20.
 */

public class MvpModel implements MvpContract.Model {
    //回调监听函数
    private MvpContract.InteractionListener mListener;
    //请求参数
    private String params;
    public MvpModel(String params,MvpContract.InteractionListener pListener){
        this.params = params;
        mListener = pListener;
    }
    @Override
    public void loadContent() {
        //网络请求数据
        // TODO: 2017/7/20
        //请求成功
        mListener.onInteractionSuccess(null);
        //请求失败
        mListener.onInteractionFail(1,"");
    }
}
