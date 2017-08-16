package com.caojian.myworkapp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.caojian.myworkapp.base.BaseActivity;
import com.caojian.myworkapp.base.BasePresenter;

/**
 * Created by CJ on 2017/7/20.
 */

public abstract class MvpBaseActivity<V,P extends BasePresenter<V>> extends BaseActivity {

    protected P myPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPresenter = createPresenter();
        myPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myPresenter != null && myPresenter.isAttach())
        {
            myPresenter.dispose();
            myPresenter.detachView();
        }
    }

    public abstract P createPresenter();
}
