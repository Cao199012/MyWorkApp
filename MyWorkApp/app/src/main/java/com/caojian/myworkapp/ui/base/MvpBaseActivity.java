package com.caojian.myworkapp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by CJ on 2017/7/20.
 */

public abstract class MvpBaseActivity<V,P extends BasePresenter<V>> extends BaseTitleActivity {

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
