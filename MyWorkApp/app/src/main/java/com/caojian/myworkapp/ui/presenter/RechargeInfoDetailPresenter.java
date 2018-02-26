package com.caojian.myworkapp.ui.presenter;


import com.caojian.myworkapp.model.response.RechargeInfo;
import com.caojian.myworkapp.model.response.RewardScoreDetailMsg;
import com.caojian.myworkapp.ui.base.BaseNoProgressObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.IntegralDetailContract;
import com.caojian.myworkapp.ui.contract.RechargeInfoDetailContract;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/10/21.
 */

public class RechargeInfoDetailPresenter extends BasePresenter<RechargeInfoDetailContract.View> implements RechargeInfoDetailContract.Presenter{
    RechargeInfoDetailContract.View mView;
    BaseTitleActivity activity;
    public RechargeInfoDetailPresenter(RechargeInfoDetailContract.View view, BaseTitleActivity activity)
    {
        super(activity);
        mView = view;
        this.activity = activity;
    }

    @Override
    public void getRechargeInfoDetail(int pageNum) {
        Observable<RechargeInfo> observable;
        if(pageNum == -1)
        {
            observable = service.getRechargeInfo(ActivityUntil.getToken(activity),"");
        }else{
            observable = service.getRechargeInfo(ActivityUntil.getToken(activity),(pageNum+1)+"");
        }
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseNoProgressObserver<RechargeInfo>(activity,this,0) {
                    @Override
                    protected void baseNext(RechargeInfo resultMsg) {
                        mView.getSuccess(resultMsg.getData());
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }
}
