package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.RegisterMsg;
import com.caojian.myworkapp.model.response.VerityCodeMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.RegisterContract;
import com.caojian.myworkapp.ui.contract.TixianContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by CJ on 2017/8/20.
 */

public class TixianPresenter extends BasePresenter<TixianContract.View> implements TixianContract.Presenter {

    private TixianContract.View mView;
    BaseTitleActivity activity;
    public TixianPresenter(BaseTitleActivity activity, TixianContract.View view){
        super(activity);
        mView = view;
        this.activity = activity;
    }

    @Override
    public void submitMsg(int rewardScore, String cardName, String cardNum, String bankName,String bankBranch) {
        Observable<CustomResult> observable = service.withdrawalRewardScore(ActivityUntil.getToken(activity),rewardScore,"",bankName,cardNum,cardName,bankBranch);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult resultMsg) {
                        if(resultMsg.getCode() == 0)
                        {
                            mView.submitSuccess();
                        }else if(resultMsg.getCode()==3){
                            activity.outLogin(resultMsg.getMessage());
                        }else
                        {
                            mView.error(resultMsg.getMessage());
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
                            mView.error(verityCodeMsg.getMessage());
                        }
                    }
                });
    }

}
