package com.caojian.myworkapp.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.caojian.myworkapp.MvpBaseFragment;

/**
 * Created by CJ on 2017/7/20.
 */

public class MvpFragment extends MvpBaseFragment<MvpContract.View,MvpPresenter> implements MvpContract.View {


    public MvpFragment getInstance(){

        return  new MvpFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null)
        {

        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected MvpPresenter createPresenter() {
        return new MvpPresenter("",this);
    }

    @Override
    public void disLoadingView() {

    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void showErrorView(String msg) {

    }

    @Override
    public void refreshContentView() {

    }

    @Override
    public void loadMoreContentView() {

    }


}
