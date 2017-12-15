package com.caojian.myworkapp.ui.presenter;


import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.LocationDetailContact;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/10/21.
 */

public class LocationDetailPresenter extends BasePresenter<LocationDetailContact.View> implements LocationDetailContact.Presenter{
    LocationDetailContact.View mView;
    BaseTitleActivity activity;
    public LocationDetailPresenter(LocationDetailContact.View view, BaseTitleActivity activity)
    {
        super(activity);
        mView = view;
        this.activity = activity;
    }

    @Override
    public void cancelLocation(String phoneNo) {
        Observable<CustomResult> observable = service.cancelGetFriendPosition(ActivityUntil.getToken(activity),phoneNo);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>((BaseTitleActivity) activity,this) {
                    @Override
                    protected void baseNext(CustomResult checkMsg) {
                        if(checkMsg.getCode() == 0)
                        {
                            mView.cancelSuccess(checkMsg.getMessage());
                        }else if(checkMsg.getCode()==3){
                            activity.outLogin(checkMsg.getMessage());
                        }else
                        {
                            mView.error(checkMsg.getMessage());
                        }
                    }
                });
    }
    public void cancelGroupLocation(String groupId) {
        Observable<CustomResult> observable = service.cancelGetGroupPosition(ActivityUntil.getToken(activity),groupId);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>((BaseTitleActivity) activity,this) {
                    @Override
                    protected void baseNext(CustomResult checkMsg) {
                        if(checkMsg.getCode() == 0)
                        {
                            mView.cancelSuccess(checkMsg.getMessage());
                        }else if(checkMsg.getCode()==3){
                            activity.outLogin(checkMsg.getMessage());
                        }else
                        {
                            mView.error(checkMsg.getMessage());
                        }
                    }
                });
    }

//    @Override
//    public void getFriends() {
//
//        Retrofit retrofit = RetrofitManger.getRetrofitRxjava(Until.HTTP_BASE_URL,activity);
//        MyApi api = retrofit.create(MyApi.class);
//
//        BaseObserver observer = new BaseObserver<FriendItem>((BaseTitleActivity) activity) {
//            @Override
//            protected void baseNext(FriendItem friend) {
//                if(friend.getCode() == 0){
//                    if(friend.getData() != null && friend.getData().getFriends()!=null)
//                    {
//                        mView.onSuccess(friend.getData().getFriends());
//                    }else {
//                            mView.onFailed(friend.getMessage());
//                        }
//                }
//
//            }
//        };
//        api.getFriendLsit(ActivityUntil.getToken(activity)).observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(observer);
//    }


}
