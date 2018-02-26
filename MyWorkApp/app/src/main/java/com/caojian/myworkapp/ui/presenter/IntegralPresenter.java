package com.caojian.myworkapp.ui.presenter;


import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.IntegralContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.widget.PersonalInstance;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/10/21.
 */

public class IntegralPresenter extends BasePresenter<IntegralContract.View> implements IntegralContract.Presenter{
    IntegralContract.View mView;
    BaseTitleActivity activity;
    public IntegralPresenter(IntegralContract.View view, BaseTitleActivity activity)
    {
        super(activity);
        mView = view;
        this.activity = activity;
    }

    @Override
    public void getPersonalInfo() {
        Observable<PersonalMsg> observable = service.getMemberInfo(ActivityUntil.getToken(activity));
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<PersonalMsg>(activity,this) {
                    @Override
                    protected void baseNext(PersonalMsg personalMsg) {
                        if(personalMsg != null) {
                            mView.getPersonalSuccess(personalMsg);
                            //单例存储个人信息
                            PersonalInstance.getInstance().setPersonalMsg(personalMsg);
                        }else
                        {
                            mView.error("网络错误");
                        }
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    @Override
    public void transferRewardScoreToFriend(String rewardScore, String friendPhoneNo) {
        Observable<CustomResult> observable = service.transferRewardScoreToFriend(ActivityUntil.getToken(activity),rewardScore,friendPhoneNo,"");
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult customMsg) {
                        mView.transferRewardScoreSuccess(customMsg.getMessage());

                    }

                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }
}
