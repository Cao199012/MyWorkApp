package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.GroupInfo;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.ChangeGroupMsgContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

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
                groupInfoBean.getAccreditEndTime(),groupInfoBean.getIsAccreditVisible()+"",groupInfoBean.getAuthorizedWeeks());
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult checkMsg) {
                        if(checkMsg.getCode() == 0)
                        {
                            mView.changeSuccess(1);
                        }else if(checkMsg.getCode()==3){
                            activity.outLogin(checkMsg.getMessage());
                        }else
                        {
                            mView.error(checkMsg.getMessage());
                        }
                    }
                });
    }

    @Override
    public void deleteGroup(String groupId) {
        Observable<CustomResult> observable = service.deleteGroupInfo(ActivityUntil.getToken(activity),groupId);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult result) {
                        if(result.getCode() == 0)
                        {
                            mView.deleteSuccess(result.getMessage());
                        }else if(result.getCode()==3){
                            activity.outLogin(result.getMessage());
                        }else
                        {
                            mView.error(result.getMessage());
                        }
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
                        if(groupInfo.getCode() == 0)
                        {
                            mView.getInfoSuccess(groupInfo);
                        }else if(groupInfo.getCode()==3){
                            activity.outLogin(groupInfo.getMessage());
                        }else
                        {
                            mView.error(groupInfo.getMessage());
                        }
                    }
                });
    }

    @Override
    public void addFriend2Group(String groupId, String friendId) {
        Observable<CustomResult> observable = service.moveFriendToGroup(ActivityUntil.getToken(activity),groupId,friendId);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult result) {
                        if(result.getCode() == 0)
                        {
                            mView.changeSuccess(2);
                        }else if(result.getCode()==3){
                            activity.outLogin(result.getMessage());
                        }else
                        {
                            mView.error(result.getMessage());
                        }
                    }
                });
    }

    @Override
    public void removeFriendFromGroup(String groupId, String friendId) {
        Observable<CustomResult> observable = service.removeFriendFromGroup(ActivityUntil.getToken(activity),groupId,friendId);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult result) {
                        if(result.getCode() == 0)
                        {
                            mView.changeSuccess(3);
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
