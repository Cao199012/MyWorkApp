package com.caojian.myworkapp.ui.base;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by CJ on 2017/10/21.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    protected abstract void baseNext(T t);
    //abstract void baseComplete();

    BaseTitleActivity baseTitleActivity;
    BasePresenter mPresenter;
    public BaseObserver(BaseTitleActivity activity,BasePresenter presenter){
        baseTitleActivity = activity;
        mPresenter = presenter;
    }

    @Override
    public void onSubscribe( Disposable d) {
        mPresenter.addDisposable(d); //防止内存泄漏
        if(baseTitleActivity != null)
        {
            baseTitleActivity.showProgress(baseTitleActivity);
        }
    }

    @Override
    public void onNext( T t) {
        baseNext(t);
    }

    @Override
    public void onError(@NonNull Throwable t) {
        if(baseTitleActivity != null)
        {
            baseTitleActivity.hideProgress();
        }
        StringBuffer sb = new StringBuffer();
        sb.append("请求失败：");
        if (t instanceof NetworkErrorException || t instanceof UnknownHostException || t instanceof ConnectException) {
            sb.append("网络异常");
        } else if (t instanceof SocketTimeoutException || t instanceof InterruptedIOException || t instanceof TimeoutException) {
            sb.append("请求超时");
        } else if (t instanceof JsonSyntaxException) {
            sb.append("请求不合法");
        } else if (t instanceof JsonParseException
                || t instanceof JSONException
                || t instanceof ParseException) {   //  解析错误
            sb.append("解析错误");
        } else {
            if(baseTitleActivity != null)
            {
                baseTitleActivity.showToast(t.getMessage(), Toast.LENGTH_SHORT);
            }
            return;
        }
        //Log.e(TAG, "onBaseError: " + sb.toString());
        if(baseTitleActivity != null)
        {
            baseTitleActivity.showToast(sb.toString(), Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void onComplete() {
        //结束时取消进度条显示
        if(baseTitleActivity != null)
        {
            baseTitleActivity.hideProgress();
        }
    }
}
