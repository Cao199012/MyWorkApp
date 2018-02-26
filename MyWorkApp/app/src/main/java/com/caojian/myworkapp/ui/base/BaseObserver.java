package com.caojian.myworkapp.ui.base;

import android.accounts.NetworkErrorException;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.UpdateResponse;
import com.caojian.myworkapp.ui.activity.UpdateActivity;
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
    protected abstract void baseError(String msg);
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
        BaseResponseResult baseResponseResult = (BaseResponseResult) t;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(baseTitleActivity.isDestroyed()) {
                return;

            }
        }
        if (baseTitleActivity == null ||baseTitleActivity.isFinishing()) {
            return;
        }
        if (baseResponseResult.getCode() == 0)
        {
            baseNext(t);
        } else if(baseResponseResult.getCode()==3 || baseResponseResult.getCode()==2){

            baseTitleActivity.outLogin(baseResponseResult.getMessage());

        }else if(baseResponseResult.getCode() == 4  || baseResponseResult.getCode() == 5){
            UpdateResponse.DataBean dataBean = new UpdateResponse.DataBean();
            dataBean.setComment(baseResponseResult.getData().getComment());
            dataBean.setMandatory(baseResponseResult.getData().getMandatory());
            dataBean.setIsUpdate(baseResponseResult.getData().getIsUpdate());
            dataBean.setDownLoadAddr(baseResponseResult.getData().getDownLoadAddr());
            UpdateActivity.go2UpdateActivity(baseTitleActivity,dataBean,101);
            if(baseResponseResult.getCode() == 4){ //标记已经升级过一次
                ((MyApplication)baseTitleActivity.getApplicationContext()).setNoForceFlag("1");
            }
            return;
        }else if(baseResponseResult.getCode()== 6)
        {
            baseTitleActivity.showBuyVip();
        }else {
            baseError(baseResponseResult.getMessage());
        }

    }

    @Override
    public void onError(@NonNull Throwable t) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(baseTitleActivity.isDestroyed()) {
                return;

            }
        }
        if (baseTitleActivity == null ||baseTitleActivity.isFinishing()) {
            return;
        }

        baseTitleActivity.hideProgress();
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

            baseError(t.getMessage());
            return;
        }

        baseError(sb.toString());


    }

    @Override
    public void onComplete() {
        //结束时取消进度条显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(baseTitleActivity.isDestroyed()) {
                return;

            }
        }
        if (baseTitleActivity == null ||baseTitleActivity.isFinishing()) {
            return;
        }

        baseTitleActivity.hideProgress();

    }
}
