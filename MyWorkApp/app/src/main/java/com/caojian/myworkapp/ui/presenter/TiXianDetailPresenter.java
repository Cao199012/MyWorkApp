package com.caojian.myworkapp.ui.presenter;


import com.caojian.myworkapp.model.response.RewardScoreDetailMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.IntegralDetailContract;
import com.caojian.myworkapp.ui.contract.TiXianDetailContract;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/10/21.
 */

public class TiXianDetailPresenter extends BasePresenter<TiXianDetailContract.View> implements TiXianDetailContract.Presenter{
    TiXianDetailContract.View mView;
    BaseTitleActivity activity;
    public TiXianDetailPresenter(TiXianDetailContract.View view, BaseTitleActivity activity)
    {
        super(activity);
        mView = view;
        this.activity = activity;
    }

    @Override
    public void getTiXianDetail(int pageNum) {
        Observable<RewardScoreDetailMsg> observable;
        if(pageNum == -1)
        {
            observable = service.getRewardScoreDetail(ActivityUntil.getToken(activity),"");
        }else{
            observable = service.getRewardScoreDetail(ActivityUntil.getToken(activity),pageNum+"");
        }
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<RewardScoreDetailMsg>(activity,this) {
                    @Override
                    protected void baseNext(RewardScoreDetailMsg resultMsg) {
                        if(resultMsg.getCode() == 0)
                        {
                            mView.getSuccess(resultMsg.getData());
                        }else if(resultMsg.getCode()==3){
                            activity.outLogin(resultMsg.getMessage());
                        }else
                        {
                            mView.error(resultMsg.getMessage());
                        }
                    }
                });
    }
}
