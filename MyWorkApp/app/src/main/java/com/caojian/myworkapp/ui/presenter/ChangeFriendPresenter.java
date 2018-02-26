package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.ChangeFriendMsgContract;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/8/20.
 */

public class ChangeFriendPresenter extends BasePresenter<ChangeFriendMsgContract.View> implements ChangeFriendMsgContract.Presenter {

    private ChangeFriendMsgContract.View mView;
    BaseTitleActivity activity;
    public ChangeFriendPresenter(BaseTitleActivity activity, ChangeFriendMsgContract.View view){
        super(activity);
        mView = view;
        this.activity = activity;
    }

    @Override
    public void changeMsg(FriendDetailInfo.DataBean friendsBean) {

        Observable<CustomResult> observable = service.modifyFriendInfo(ActivityUntil.getToken(activity),friendsBean.getFriendPhoneNo(),friendsBean.getFriendRemarkName(),friendsBean.getAccreditStartTime(),
                friendsBean.getAccreditEndTime(),friendsBean.getIsAccreditVisible()+"",friendsBean.getAccreditWeeks());
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult checkMsg) {
                        mView.changeSuccess();
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    @Override
    public void deleteFriend(String phoneNum) {
        Observable<CustomResult> observable = service.deleteFriend(ActivityUntil.getToken(activity),phoneNum);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult result) {
                        mView.deleteSuccess(result.getMessage());
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    @Override
    public void getFriendInfo(String phoneNum) {
        Observable<FriendDetailInfo> observable = service.getFriendInfo(ActivityUntil.getToken(activity),phoneNum);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<FriendDetailInfo>(activity,this) {
                    @Override
                    protected void baseNext(FriendDetailInfo result) {
                        mView.getFriendInfo(result);
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }
}
