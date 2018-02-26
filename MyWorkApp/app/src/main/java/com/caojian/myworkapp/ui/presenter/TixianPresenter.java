package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.TixianContract;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    //积分提现
    @Override
    public void submitTixianMsg(int rewardScore, String cardName, String bankName, String cardNum, String verCode) {
        Observable<CustomResult> observable = service.withdrawalRewardScore(ActivityUntil.getToken(activity),rewardScore,"",bankName,cardNum,cardName,verCode);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult resultMsg) {
                        mView.submitSuccess();
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    //积分转赠
    @Override
    public void submitGiveMsg(String rewardScore, String friendNo, String verCode) {
        Observable<CustomResult> observable = service.transferRewardScoreToFriend(ActivityUntil.getToken(activity),rewardScore,friendNo,verCode);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult resultMsg) {
                       mView.submitSuccess();
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    @Override
    public void verityCode(String phone, String imgCode,int type) {
        //后台获取验证码
        service.verityCode(phone, imgCode,type+"").observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult verityCodeMsg) {
                        mView.verityCodeSuccess();
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

}
