package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.data.FriendItem;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.ChangeFriendMsgContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

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
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult checkMsg) {
                        if(checkMsg.getCode() == 0)
                        {
                            mView.changeSuccess();
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
    public void deleteFriend(String phoneNum) {
        Observable<CustomResult> observable = service.deleteFriend(ActivityUntil.getToken(activity),phoneNum);
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
    public void getFriendInfo(String phoneNum) {
        Observable<FriendDetailInfo> observable = service.getFriendInfo(ActivityUntil.getToken(activity),phoneNum);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<FriendDetailInfo>(activity,this) {
                    @Override
                    protected void baseNext(FriendDetailInfo result) {
                        if(result.getCode() == 0)
                        {
                            mView.getFriendInfo(result);
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
