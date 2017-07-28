package com.caojian.myworkapp.mvp;

import com.caojian.myworkapp.mvp.MvpContract;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by CJ on 2017/7/20.
 */

public class MvpModel implements MvpContract.Model<Observable<MvpItem>> {
    //回调监听函数
    private MvpContract.InteractionListener mListener;
    //请求参数
    private String params;

    private List<MvpItem> list;
    public MvpModel(String params){
        this.params = params;
        list = new LinkedList<MvpItem>();
    }

    public void setmListener(MvpContract.InteractionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public Observable<MvpItem> loadContent() {


        //网络请求数据
        // TODO: 2017/7/20
        list.clear();
        for (int i = 0; i < 10 ; i++)
        {
            MvpItem item = new MvpItem();
            item.setMsg("hello Mvp"+i);
            list.add(item);
        }
        //rxjava生成上游数据
        Observable observable =  Observable.fromArray(list.toArray());

        //请求成功
        mListener.onInteractionSuccess(observable);
        //请求失败
       // mListener.onInteractionFail(1,"");
        return observable;
    }
}
