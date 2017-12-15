package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.response.RegisterMsg;
import com.caojian.myworkapp.model.response.VerityCodeMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.PasswordContract;
import com.caojian.myworkapp.ui.contract.RegisterContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

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
                        if(registerMsg.getCode() == 0)
                        {
                            mView.changePasswordSuccess();
                            //保存token到本地
                            ActivityUntil.saveToken(activity,registerMsg.getData().getToken());
                        }else
                        {
                            mView.requestError(registerMsg.getMessage());
                        }
                    }
                });
    }

    @Override
    public void verityCode(String phone, String imgCode) {
        //后台获取验证码
        service.verityCode(phone, imgCode,"1").observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<VerityCodeMsg>(activity,this) {
                    @Override
                    protected void baseNext(VerityCodeMsg verityCodeMsg) {
                        if(verityCodeMsg.getCode() == 0)
                        {
                            mView.verityCodeSuccess();
                        }else
                        {
                            mView.requestError(verityCodeMsg.getMessage());
                        }
                    }
                });
    }

}
