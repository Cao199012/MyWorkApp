package com.caojian.myworkapp.ui.presenter;


import com.caojian.myworkapp.model.response.RewardScoreDetailMsg;
import com.caojian.myworkapp.ui.base.BaseNoProgressObserver;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.IntegralDetailContract;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/10/21.
 */

public class IntegralDetailPresenter extends BasePresenter<IntegralDetailContract.View> implements IntegralDetailContract.Presenter{
    IntegralDetailContract.View mView;
    BaseTitleActivity activity;
    public IntegralDetailPresenter(IntegralDetailContract.View view, BaseTitleActivity activity)
    {
        super(activity);
        mView = view;
        this.activity = activity;
    }

    @Override
    public void getIntegralDetail(int pageNum) {
        Observable<RewardScoreDetailMsg> observable;
        if(pageNum == -1)
        {
            observable = service.getRewardScoreDetail(ActivityUntil.getToken(activity),"");
        }else{
            observable = service.getRewardScoreDetail(ActivityUntil.getToken(activity),(pageNum+1)+"");
        }
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseNoProgressObserver<RewardScoreDetailMsg>(activity,this,0) {
                    @Override
                    protected void baseNext(RewardScoreDetailMsg resultMsg) {
                        mView.getSuccess(resultMsg.getData());
                    }

                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }
}
