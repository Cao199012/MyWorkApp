package com.caojian.myworkapp.mvp;

import android.os.Build;
import android.util.Log;

import com.caojian.myworkapp.BasePresenter;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/7/20.
 */

public class MvpPresenter extends BasePresenter<MvpContract.View> implements MvpContract.InteractionListener,MvpContract.Presenter{

    private MvpContract.View mView;
    private MvpContract.Model model;

    public MvpPresenter(MvpContract.Model pModel,MvpContract.View pView){
        mView = pView;
        model = pModel;
        ((MvpModel)model).setmListener(this);
    }
    @Override
    public void onInteractionSuccess(Object o) {
        final List<MvpItem>list = new LinkedList<>();
        if(o instanceof Observable)
        {
            Observer observer = new Observer<MvpItem>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }
                @Override
                public void onNext(@NonNull MvpItem mvpItem) {
                    list.add(mvpItem);
                }
                @Override
                public void onError(@NonNull Throwable e) {

                }
                @Override
                public void onComplete() {
                    //上游传输完毕 显示view
                    mView.refreshContentView(list);
                }
            };
            Disposable disposable = ((Observable) o).toList()
                            .observeOn(Schedulers.newThread())
                            .subscribeOn(AndroidSchedulers.mainThread())
//                            .subscribe(observer);
                            .subscribe(new Consumer<List<MvpItem>>() {
                                @Override
                                public void accept(List<MvpItem> mvpItems) throws Exception {
                                    mView.refreshContentView(mvpItems);
                                }
                            });
            addDisposable(disposable);
        }
    }



    @Override
    public void onInteractionFail(int errorCode, String errorMsg) {

    }

    @Override
    public void requestRefresh() {
        model.loadContent();
    }

    @Override
    public void requestLoadMore() {

    }
}