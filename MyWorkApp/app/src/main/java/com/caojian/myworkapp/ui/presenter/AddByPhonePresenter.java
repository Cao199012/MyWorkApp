package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.FriendMsg;
import com.caojian.myworkapp.model.response.RegisterMsg;
import com.caojian.myworkapp.model.response.VerityCodeMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.AddByPhoneContrct;
import com.caojian.myworkapp.ui.contract.RegisterContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by CJ on 2017/8/20.
 */

public class AddByPhonePresenter extends BasePresenter<AddByPhoneContrct.View> implements AddByPhoneContrct.Presenter {

    private AddByPhoneContrct.View mView;
    BaseTitleActivity activity;
    public AddByPhonePresenter(BaseTitleActivity activity, AddByPhoneContrct.View view){
        super(activity);
        mView = view;
        this.activity = activity;
    }
    //检测好友s手机号
    @Override
    public void checkPhone(String phone) {
        Observable<FriendMsg> observable = service.searchUserByPhoneNo(ActivityUntil.getToken(activity),phone);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<FriendMsg>(activity,this) {
                    @Override
                    protected void baseNext(FriendMsg friendMsg) {
                        if(friendMsg.getCode() == 0 && friendMsg.getData() != null)
                        {
                            //搜索号码 判断是否已经是好友了
                            if(friendMsg.getData().getIsFriend() == 1){
                                mView.checkFail(friendMsg.getMessage());
                                return;
                            }
                            mView.checkSuccess();
                        }else if(friendMsg.getCode()==3){
                            activity.outLogin(friendMsg.getMessage());
                        }else
                        {
                            mView.checkFail(friendMsg.getMessage());
                        }
                    }
                });
    }

    //添加好友
    @Override
    public void addFriend(String phone, String applyInfo) {

        service.applyForAddFriend(ActivityUntil.getToken(activity),phone,applyInfo).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult  applyFriendMsg) {
                        if(applyFriendMsg.getCode() == 0)
                        {
                            mView.addSuccess(applyFriendMsg.getMessage());
                        }else if(applyFriendMsg.getCode()==3){
                            activity.outLogin(applyFriendMsg.getMessage());
                        }else
                        {
                            mView.error(applyFriendMsg.getMessage());
                        }
                    }
                });
    }
}
