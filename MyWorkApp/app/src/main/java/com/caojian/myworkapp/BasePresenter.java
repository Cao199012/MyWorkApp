package com.caojian.myworkapp;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by CJ on 2017/7/20.
 * 通过弱引用Fragment和Activity来解决MVP可能引起的内存泄漏
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

}
