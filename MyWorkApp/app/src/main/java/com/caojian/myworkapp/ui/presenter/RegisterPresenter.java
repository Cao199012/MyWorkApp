package com.caojian.myworkapp.ui.presenter;

import android.view.View;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.response.RegisterMsg;
import com.caojian.myworkapp.model.response.VerityCodeMsg;
import com.caojian.myworkapp.ui.activity.RegisterActivity;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.RegisterContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by CJ on 2017/8/20.
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    private RegisterContract.View mView;
    BaseTitleActivity activity;
    public RegisterPresenter(BaseTitleActivity activity, RegisterContract.View view){
        super(activity);
        mView = view;
        this.activity = activity;
    }

    @Override
    public void checkRegister(String phone, String verificationCode, String password, String invitationCode) {
        Observable<RegisterMsg> observable = service.register(phone,verificationCode,password,invitationCode);
        observable.observeOn(AndroidSchedulers.mainThread())
                  .subscribeOn(Schedulers.newThread())
                 .subscribe(new BaseObserver<RegisterMsg>(activity,this) {
                     @Override
                     protected void baseNext(RegisterMsg registerMsg) {
                         if(registerMsg.getCode() == 0)
                         {
                             mView.registerSuccess();
                             //保存token到本地
                             ActivityUntil.saveToken(activity,registerMsg.getData().getToken());
                         }else
                         {
                             mView.registerError(registerMsg.getMessage());
                         }
                     }
                 });
    }

    @Override
    public void verityCode(String phone, String imgCode) {
        //后台获取验证码
        service.verityCode(phone, imgCode,"0").observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<VerityCodeMsg>(activity,this) {
                    @Override
                    protected void baseNext(VerityCodeMsg verityCodeMsg) {
                        if(verityCodeMsg.getCode() == 0)
                        {
                            mView.verityCodeSuccess();
                        }else
                        {
                            mView.registerError(verityCodeMsg.getMessage());
                        }
                    }
                });
    }

}
