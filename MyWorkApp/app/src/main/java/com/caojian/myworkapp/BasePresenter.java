package com.caojian.myworkapp;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by CJ on 2017/7/20.
 * 通过弱引用Fragment和Activity(view)来解决MVP可能引起的内存泄漏
 * 避免RXjava可能引起的内存泄漏
 */

public class BasePresenter<V> {
    protected Reference<V> myView; //View 弱引用的接口

    /**
     * @param view
     * 绑定view
     */
    public void attachView(V view)
    {
        myView = new WeakReference<V>(view);
    }

    /**
     * @return true 已绑定
     * 判断是否绑定view
     */
    public boolean isAttach()
    {
        return myView != null && myView.get() != null;
    }

    /**
     * @return 获取
     */
    public V getView()
    {
        if(myView == null)
        {
            return null;
        }
        return myView.get();
    }

    /**
     * 解除view的引用
     *
     */
    public void detachView()
    {
        if(myView != null)
        {
            myView.clear();
            myView = null;
        }
    }
    CompositeDisposable mCompositeDisposable;
    //把所有正在处理的Disposable，添加到CompositeDisposable，统一注销
    public void addDisposable(Disposable subscription)
    {
        if(mCompositeDisposable == null || mCompositeDisposable.isDisposed())
        {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    //注销所有Disposable
    public void dispose()
    {
        if(mCompositeDisposable != null)
        {
            mCompositeDisposable.dispose();
        }
    }

}
