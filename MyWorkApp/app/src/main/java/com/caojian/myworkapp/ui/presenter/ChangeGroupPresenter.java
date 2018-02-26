package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.GroupInfo;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.ChangeGroupMsgContract;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/8/20.
 */

public class ChangeGroupPresenter extends BasePresenter<ChangeGroupMsgContract.View> implements ChangeGroupMsgContract.Presenter {

    private ChangeGroupMsgContract.View mView;
    BaseTitleActivity activity;
    public ChangeGroupPresenter(BaseTitleActivity activity, ChangeGroupMsgContract.View view){
        super(activity);
        mView = view;
        this.activity = activity;
    }
    @Override
    public void changeMsg(GroupInfo.DataBean groupInfoBean) {

        Observable<CustomResult> observable = service.modifyGroupInfo(ActivityUntil.getToken(activity),groupInfoBean.getGroupId(),groupInfoBean.getGroupName(),groupInfoBean.getAccreditStartTime(),
                groupInfoBean.getAccreditEndTime(),groupInfoBean.getIsAccreditVisible()+"",groupInfoBean.getAccreditWeeks());
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult checkMsg) {
                        mView.changeSuccess(1);
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    @Override
    public void deleteGroup(String groupId) {
        Observable<CustomResult> observable = service.deleteGroupInfo(ActivityUntil.getToken(activity),groupId);
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
    public void getGroupInfo(String groupId) {
        Observable<GroupInfo> observable = service.getGroupInfo(ActivityUntil.getToken(activity),groupId);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<GroupInfo>(activity,this) {
                    @Override
                    protected void baseNext(GroupInfo groupInfo) {
                        mView.getInfoSuccess(groupInfo);
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    @Override
    public void addFriend2Group(String groupId, String friendId) {
        Observable<CustomResult> observable = service.moveFriendToGroup(ActivityUntil.getToken(activity),groupId,friendId);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult result) {
                         mView.changeSuccess(2);
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    @Override
    public void removeFriendFromGroup(String groupId, String friendId) {
        Observable<CustomResult> observable = service.removeFriendFromGroup(ActivityUntil.getToken(activity),groupId,friendId);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult result) {
                        mView.changeSuccess(3);
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

}
