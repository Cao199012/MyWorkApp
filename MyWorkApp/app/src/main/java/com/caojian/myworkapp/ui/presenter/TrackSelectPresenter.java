package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.data.FriendItem;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.FriendContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by CJ on 2017/11/21.
 */

public class TrackSelectPresenter extends FriendPresenter {
    public TrackSelectPresenter(FriendContract.View view, BaseTitleActivity activity) {
        super(view, activity);
    }

    @Override
    public void getFriends() {
        service.getFriendLsit(ActivityUntil.getToken(activity)).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<FriendItem>(activity,this) {
                    @Override
                    protected void baseNext(FriendItem friend) {
                        if(friend.getCode() == 0){
                            if(friend.getData() != null && friend.getData().getFriends()!=null)
                            {
                                mView.onSuccess(friend.getData().getFriends());
                            }else if(friend.getCode()==3){
                                activity.outLogin(friend.getMessage());
                            }else {
                                mView.onFailed(friend.getMessage());
                            }
                        }
                    }
                });
    }
}
