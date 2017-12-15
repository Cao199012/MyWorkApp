package com.caojian.myworkapp.ui.presenter;

import android.util.Log;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.response.LoginMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.LoginContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by CJ on 2017/7/28.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private LoginContract.View loginView;
    BaseTitleActivity activity;
    public LoginPresenter(BaseTitleActivity activity,LoginContract.View pLoginView){
        super(activity);
        loginView = pLoginView;
        this.activity = activity;
    }
    @Override
    public void checkLogin(String name, String password) {
         service.checkLogin(name,password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<LoginMsg>(activity,this) {
                    @Override
                    protected void baseNext(LoginMsg loginMsg) {
                        if(loginMsg.getCode() == 0)
                        {
                            loginView.LoginSuccess();
                            //保存token到本地
                            ActivityUntil.saveToken(activity,loginMsg.getData().getToken());
                        }else
                        {
                            loginView.LoginError(loginMsg.getMessage());
                        }
                    }

                });
    }
}
