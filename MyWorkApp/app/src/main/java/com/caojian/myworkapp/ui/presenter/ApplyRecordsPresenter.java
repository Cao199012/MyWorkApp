package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.ApplyFriendsRecord;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.ui.base.BaseNoProgressObserver;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.ApplyRecordContract;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
                .subscribe(new BaseNoProgressObserver<ApplyFriendsRecord>(activity,this,0) {
                    @Override
                    protected void baseNext(ApplyFriendsRecord recordsMsg) {
                        mView.getRecords(recordsMsg.getData().getRecords());
                    }

                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    @Override
    public void agreeApply(ApplyFriendsRecord.DataBean.RecordsBean recordsBean) {
        Observable<CustomResult> observable = service.agreeToAddFriend(ActivityUntil.getToken(activity),recordsBean.getFriendPhoneNo(),
                      recordsBean.getId()+"","1");
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult result) {
                        mView.agreeSuccess();
                    }

                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }
}
