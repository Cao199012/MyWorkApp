package com.caojian.myworkapp.mvp;

import com.caojian.myworkapp.BasePresenter;

/**
 * Created by CJ on 2017/7/20.
 */

public class MvpPresenter extends BasePresenter<MvpContract.View> implements MvpContract.InteractionListener,MvpContract.Presenter{

    private MvpContract.View mView;
    private MvpContract.Model model;

    public MvpPresenter(String params,MvpContract.View pView){
        mView = pView;
        model = new MvpModel(params,this);
    }
    @Override
    public void onInteractionSuccess(Object o) {
        mView.refreshContentView();
    }

    @Override
    public void onInteractionFail(int errorCode, String errorMsg) {

    }

    @Override
    public void requestRefresh() {

    }

    @Override
    public void requestLoadMore() {

    }
}
