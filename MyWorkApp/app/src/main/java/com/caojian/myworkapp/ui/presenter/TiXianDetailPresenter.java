package com.caojian.myworkapp.ui.presenter;


import com.caojian.myworkapp.model.response.DrawInfo;
import com.caojian.myworkapp.ui.base.BaseNoProgressObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
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
        Observable<DrawInfo> observable ;

        if(pageNum == -1)
        {
            observable = service.getWithdrawalRewardScoreInfo(ActivityUntil.getToken(activity),1+"");
        }else{
            observable = service.getWithdrawalRewardScoreInfo(ActivityUntil.getToken(activity),(pageNum+1)+"");
        }
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseNoProgressObserver<DrawInfo>(activity,this,0) {
                    @Override
                    protected void baseNext(DrawInfo resultMsg) {
                       mView.getSuccess(resultMsg.getData());
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }
}
