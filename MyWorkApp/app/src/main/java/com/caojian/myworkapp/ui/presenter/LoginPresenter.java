package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.contract.LoginContract;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/7/28.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private MyApi service;
    private LoginContract.View loginView;
    public LoginPresenter(LoginContract.View pLoginView){
        loginView = pLoginView;
//        Retrofit retrofit = new Retrofit.Builder()
//                                .baseUrl("www.baidu.com")
//                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                                .addConverterFactory(GsonConverterFactory.create())
//                                .build();
//        service = retrofit.create(LoginService.class);
    }

    @Override
    public void checkLogin(String name, String password) {
        if(service != null)
        {
             service.checkLogin(name,password)
                    .observeOn(Schedulers.newThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onNext(@NonNull String s) {
                            loginView.LoginSuccess();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            loginView.LoginError(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
