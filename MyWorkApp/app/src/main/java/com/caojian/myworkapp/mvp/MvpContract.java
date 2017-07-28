package com.caojian.myworkapp.mvp;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by CJ on 2017/7/20.
 */

public class MvpContract {
    /**
     * M获取数据模块
     */
    public interface Model<T> {
        //请求数据
        public T loadContent();
    }

    public interface View{
        /**
         * 隐藏加载界面
         */
        void disLoadingView();
        //显示加载界面
        void showLoadingView();
        //显示错误界面
        void showErrorView(String msg);
        //显示数据刷新界面
        void refreshContentView(List<MvpItem> pList);
        //显示加载更多界面
        void loadMoreContentView();
    }

    /**
     *
     */
    public interface Presenter{

        /**
         * 请求刷新的数据
         */
        void requestRefresh();

        /**
         * 请求加载更多的数据
         */
        void requestLoadMore();
    }

    /**
     * @param <T>
     *     presenter用于Model的回调借口
     */
    public interface InteractionListener<T>{
        //请求成功
        void onInteractionSuccess(T t);
        //请求失败
        void onInteractionFail(int errorCode, String errorMsg);
    }


}
