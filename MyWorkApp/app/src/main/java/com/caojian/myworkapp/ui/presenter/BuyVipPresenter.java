package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.OrderNumberMsg;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.model.response.ValueAddedResult;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.BuyVipContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.widget.PersonalInstance;

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
                        mView.getOrderNumSuccess(orderNumberMsg.getData().getOrderNumber(),orderNumberMsg.getData().getAmount());
                    }

                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }



    @Override
    public void buyVipByJiFen(String vipType) {
        Observable<CustomResult> observable = service.buyMemberByRewardScore(ActivityUntil.getToken(activity),vipType);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult baseResponseResult) {
                        mView.buySuccess(baseResponseResult.getMessage());
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }
    //积分购买增值服务
    @Override
    public void buyValueAddedServiceByRewardScore(String kindType) {
        Observable<CustomResult> observable = service.buyValueAddedServiceByRewardScore(ActivityUntil.getToken(activity),kindType);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult baseResponseResult) {
                        mView.buySuccess(baseResponseResult.getMessage());
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    public void getOrderTime(String vipType) {
        Observable<ValueAddedResult> observable = service.getMemberValueAddedTime(ActivityUntil.getToken(activity),vipType);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<ValueAddedResult>(activity,this) {
                    @Override
                    protected void baseNext(ValueAddedResult orderNumberMsg) {
                       if(orderNumberMsg.getData() == null){
                                mView.getOrderTimeSuccess(null,null);
                                return;
                            }
                        mView.getOrderTimeSuccess(orderNumberMsg.getData().getStartTime(),orderNumberMsg.getData().getEndTime());
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }
    public void getPersonalInfo() {
        Observable<PersonalMsg> observable = service.getMemberInfo(ActivityUntil.getToken(activity));
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<PersonalMsg>(activity,this) {
                    @Override
                    protected void baseNext(PersonalMsg personalMsg) {
                        if(personalMsg != null) {
                            //单例存储个人信息
                            PersonalInstance.getInstance().setPersonalMsg(personalMsg);
                            mView.getPersonalSuccess(personalMsg);
                        }
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.getPersonalError(msg);
                    }
                });
    }
}
