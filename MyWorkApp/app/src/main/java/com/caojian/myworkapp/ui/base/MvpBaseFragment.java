package com.caojian.myworkapp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by CJ on 2017/7/20.
 */

public abstract class MvpBaseFragment<V,P extends BasePresenter<V>> extends Fragment {

    private P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null && mPresenter.isAttach())
        {
            mPresenter.dispose();
            mPresenter.detachView();
        }
    }

    protected abstract P createPresenter();
}
