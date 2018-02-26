package com.caojian.myworkapp.ui.presenter;

import android.widget.Toast;

import com.caojian.myworkapp.model.response.RailHistoryMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.RailSelectContract;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/7/28.
 */

public class RailPresenter extends BasePresenter<RailSelectContract.View> implements RailSelectContract.Presenter {
    private RailSelectContract.View loginView;
    BaseTitleActivity activity;
    public RailPresenter(BaseTitleActivity activity, RailSelectContract.View pLoginView){
        super(activity);
        loginView = pLoginView;
        this.activity = activity;
    }


    @Override
    public void getRailHistory(String phoneNo, String startTime, String endTime) {
        service.getMemberHistoryPositions(ActivityUntil.getToken(activity),phoneNo,startTime,endTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<RailHistoryMsg>(activity,this) {
                    @Override
                    protected void baseNext(RailHistoryMsg railHistoryMsg) {
                        if(railHistoryMsg.getData().getPositions() == null ||railHistoryMsg.getData().getPositions().isEmpty()){
                            loginView.getError(railHistoryMsg.getMessage());
                        }else {
                            loginView.getSuccess(railHistoryMsg.getData().getPositions());

                        }
                    }
                    @Override
                    protected void baseError(String msg) {
                        loginView.getError(msg);
                    }
                });
    }
}
