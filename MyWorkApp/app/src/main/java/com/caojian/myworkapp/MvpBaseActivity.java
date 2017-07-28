package com.caojian.myworkapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by CJ on 2017/7/20.
 */

public abstract class MvpBaseActivity<V,P extends BasePresenter<V>> extends AppCompatActivity{

    protected P myPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        myPresenter = createPresenter();
        myPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myPresenter != null && myPresenter.isAttach())
        {
            myPresenter.detachView();
        }
    }

    public abstract P createPresenter();
}
