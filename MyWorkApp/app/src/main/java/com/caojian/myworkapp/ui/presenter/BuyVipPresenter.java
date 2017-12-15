package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.OrderNumberMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.BuyVipContract;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/11/23.
 */

public class BuyVipPresenter extends BasePresenter<BuyVipContract.View> implements BuyVipContract.Presenter {
    private BaseTitleActivity activity;
    private BuyVipContract.View mView;
    public BuyVipPresenter(BaseTitleActivity activity,BuyVipContract.View view) {
        super(activity);
        this.activity = activity;
        mView = view;
    }

    @Override
    public void getOrderNum(String vipType,String validityDurationType) {
        Observable<OrderNumberMsg> observable = service.getOrderNumber(ActivityUntil.getToken(activity),vipType,validityDurationType);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<OrderNumberMsg>(activity,this) {
                    @Override
                    protected void baseNext(OrderNumberMsg orderNumberMsg) {
                        if(orderNumberMsg.getCode() == 0)
                        {
                            mView.getOrderNumSuccess(orderNumberMsg.getData().getOrderNumber());
                        }else if(orderNumberMsg.getCode()==3){
                            activity.outLogin(orderNumberMsg.getMessage());
                        }else
                        {
                            mView.error(orderNumberMsg.getMessage());
                        }
                    }
                });
    }



    @Override
    public void buyVipByJiFen(String vipType) {
        Observable<CustomResult> observable = service.buyMemberByRewardScore(ActivityUntil.getToken(activity),vipType);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult customResult) {
                        if(customResult.getCode() == 0)
                        {
                            mView.buySuccess(customResult.getMessage());
                        }else if(customResult.getCode()==3){
                            activity.outLogin(customResult.getMessage());
                        }else
                        {
                            mView.error(customResult.getMessage());
                        }
                    }
                });
    }

    @Override
    public void buyValueAddedServiceByRewardScore(String kindType) {
        Observable<CustomResult> observable = service.buyValueAddedServiceByRewardScore(ActivityUntil.getToken(activity),kindType);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult customResult) {
                        if(customResult.getCode() == 0)
                        {
                            mView.buySuccess(customResult.getMessage());
                        }else if(customResult.getCode()==3){
                            activity.outLogin(customResult.getMessage());
                        }else
                        {
                            mView.error(customResult.getMessage());
                        }
                    }
                });
    }


}
