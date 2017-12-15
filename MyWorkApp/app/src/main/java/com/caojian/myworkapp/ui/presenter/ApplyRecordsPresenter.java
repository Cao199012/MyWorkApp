package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.data.ApplyFriendsRecord;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.ApplyRecordContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by CJ on 2017/10/31.
 */

public class ApplyRecordsPresenter extends BasePresenter<ApplyRecordContract.View>implements ApplyRecordContract.Presenter{
    private ApplyRecordContract.View mView;
    BaseTitleActivity activity;
    public ApplyRecordsPresenter(BaseTitleActivity activity, ApplyRecordContract.View view){
        super(activity);
        mView = view;
        this.activity = activity;
    }


    @Override
    public void getRecords() {
        Observable<ApplyFriendsRecord> observable = service.getApplicationRecordOfFriend(ActivityUntil.getToken(activity));
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<ApplyFriendsRecord>(activity,this) {
                    @Override
                    protected void baseNext(ApplyFriendsRecord recordsMsg) {
                        if(recordsMsg.getCode() == 0)
                        {
                            mView.getRecords(recordsMsg.getData().getRecords());
                        }else if(recordsMsg.getCode()==3){
                            activity.outLogin(recordsMsg.getMessage());
                        }else
                        {
                            mView.error(recordsMsg.getMessage());
                        }
                    }
                });
    }

    @Override
    public void agreeApply(ApplyFriendsRecord.DataBean.RecordsBean recordsBean) {
        Observable<CustomResult> observable = service.agreeToAddFriend(ActivityUntil.getToken(activity),recordsBean.getFriendPhoneNo(),
                      recordsBean.getId()+"","1");
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult result) {
                        if(result.getCode() == 0)
                        {
                            mView.agreeSuccess();
                        }else if(result.getCode()==3){
                            activity.outLogin(result.getMessage());
                        }else
                        {
                            mView.error(result.getMessage());
                        }
                    }
                });
    }
}
