package com.caojian.myworkapp.ui.presenter;



import com.caojian.myworkapp.model.data.FriendItem;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.FriendContract;
import com.caojian.myworkapp.until.ActivityUntil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by CJ on 2017/10/21.
 */

public class FriendPresenter extends BasePresenter<FriendContract.View> implements FriendContract.Presenter{
    FriendContract.View mView;
    BaseTitleActivity activity;
    public FriendPresenter(FriendContract.View view,  BaseTitleActivity activity)
    {
        super(activity);
        mView = view;
        this.activity = activity;
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
                            }else {
                                mView.onFailed(friend.getMessage());
                            }
                        }else if(friend.getCode()==3){
                            activity.outLogin(friend.getMessage());
                        }
                    }
                });
    }
}
