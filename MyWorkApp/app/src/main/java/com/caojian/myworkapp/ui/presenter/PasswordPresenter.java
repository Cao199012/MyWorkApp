package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.model.response.RegisterMsg;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.PasswordContract;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/8/20.
 */

public class PasswordPresenter extends BasePresenter< PasswordContract.View> implements PasswordContract.Presenter {

    private PasswordContract.View mView;
    BaseTitleActivity activity;
    public PasswordPresenter(BaseTitleActivity activity,  PasswordContract.View view){
        super(activity);
        mView = view;
        this.activity = activity;
    }

    @Override
    public void checkPassword(String phone, String verificationCode, String password) {

        Observable<RegisterMsg> observable = service.resetPassword(phone,verificationCode,password);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<RegisterMsg>(activity,this) {
                    @Override
                    protected void baseNext(RegisterMsg registerMsg) {
                        if (registerMsg.getData() == null){
                            mView.requestError(registerMsg.getMessage());
                            return;
                        }
                       mView.changePasswordSuccess();
                            //保存token到本地
                        ActivityUntil.saveToken(activity,registerMsg.getData().getToken());

                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.requestError(msg);
                    }
                });
    }

    @Override
    public void verityCode(String phone, String imgCode) {
        //后台获取验证码
        service.verityCode(phone, imgCode,"1").observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult verityCodeMsg) {
                       mView.verityCodeSuccess();
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.requestError(msg);
                    }
                });
    }

}
