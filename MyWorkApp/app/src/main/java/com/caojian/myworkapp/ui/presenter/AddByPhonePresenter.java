package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.FriendMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.AddByPhoneContrct;
import com.caojian.myworkapp.until.ActivityUntil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

                        //搜索号码 判断是否已经是好友了
                        if(friendMsg.getData() == null){
                            mView.checkFail("好友还没注册，邀请加入");
                            return;
                        }
                        if(friendMsg.getData().getIsFriend() == 1){
                            mView.error("已是好友");
                            return;
                        }
                        mView.checkSuccess();

                    }

                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    //添加好友
    @Override
    public void addFriend(String phone, String applyInfo) {

        service.applyForAddFriend(ActivityUntil.getToken(activity),phone,applyInfo).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult applyFriendMsg) {
                        mView.addSuccess(applyFriendMsg.getMessage());
                    }

                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }
}
