package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.model.response.RegisterMsg;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.RegisterContract;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    public void checkRegister(String phone, String verificationCode, String password, String invitationCode, String nickname) {
        Observable<RegisterMsg> observable = service.register(phone,verificationCode,password,invitationCode,nickname);
        observable.observeOn(AndroidSchedulers.mainThread())
                  .subscribeOn(Schedulers.newThread())
                 .subscribe(new BaseObserver<RegisterMsg>(activity,this) {
                     @Override
                     protected void baseNext(RegisterMsg registerMsg) {
                         mView.registerSuccess();
                         //保存token到本地
                         ActivityUntil.saveToken(activity,registerMsg.getData().getToken());
                     }
                     @Override
                     protected void baseError(String msg) {
                         mView.registerError(msg);
                     }
                 });
    }

    @Override
    public void verityCode(String phone, String imgCode) {
        //后台获取验证码
        service.verityCode(phone, imgCode,"0").observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult verityCodeMsg) {
                      mView.verityCodeSuccess();
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.registerError(msg);
                    }
                });
    }

}
