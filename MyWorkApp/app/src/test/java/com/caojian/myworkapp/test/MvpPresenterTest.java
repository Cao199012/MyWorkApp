package com.caojian.myworkapp.test;

import com.caojian.myworkapp.mvp.MvpContract;
import com.caojian.myworkapp.mvp.MvpItem;
import com.caojian.myworkapp.mvp.MvpModel;
import com.caojian.myworkapp.mvp.MvpPresenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by CJ on 2017/7/26.
 */
public class MvpPresenterTest {
    private MvpContract.View mView;
    private MvpContract.Model model;
    private MvpPresenter mvpPresenter;

//    public MvpPresenter(String params,MvpContract.View pView){
//        mView = pView;
//        model = new MvpModel(params,this);
//    }
@BeforeClass
public static void setUpRxSchedulers() {
    final Scheduler immediate = new Scheduler() {
        @Override
        public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
            // this prevents StackOverflowErrors when scheduling with a delay
            return super.scheduleDirect(run, 0, unit);
        }

        @Override
        public Worker createWorker() {
            return new ExecutorScheduler.ExecutorWorker(Runnable::run);
        }
    };


    RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
    RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
    RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
    RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
    RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
}

    @Before
public void setUp() throws Exception {

    mView = mock(MvpContract.View.class);
    model = mock(MvpModel.class);
    mvpPresenter = new MvpPresenter(model,mView);
//
//    // 生成mock对象
//    userView = mock(UserView.class);
//    userService = mock(UserService.class);
//
//    userPresenter = new UserPresenterImpl(userService, userView);
}
    @Test
    public void onInteractionSuccess() throws Exception {
        // Given an initialized TasksPresenter with initialized tasks
       // when(model.loadContent()).thenReturn(Observable);
        //构建测试数据
        MvpItem item = new MvpItem();
        item.setMsg("aaaa");
        List<MvpItem> list= new LinkedList<>();
        list.add(item);
        Observable observable =  Observable.fromArray(list.toArray());
        mvpPresenter.onInteractionSuccess(observable);
        //参数捕获器，获取入参
         ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(mView).refreshContentView(captor.capture());
        MvpItem item1 = (MvpItem) captor.getValue().get(0);
        //结果比较
        Assert.assertEquals(item1.getMsg(),"aaaa");
    }

    @Test
    public void requestRefresh() throws Exception {
        MvpItem item = new MvpItem();
        item.setMsg("aaaa");
//        调用loadContent()测试返回是否为Observable.just(item)对象
        when(model.loadContent()).thenReturn(Observable.just(item));

        //测试model.loadContent()是否被调用
        mvpPresenter.requestRefresh();
        verify(model).loadContent();

       // ArgumentCaptor<Observable> captor = ArgumentCaptor.forClass(LinkedList.class);
        verify(mvpPresenter).onInteractionSuccess(Observable.just(item));
//        MvpItem item1 = (MvpItem) captor.getValue().get(0);
//        Assert.assertEquals(item1.getMsg(),"aaaa");
    }



}