package com.caojian.myworkapp.ui.presenter;



import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.model.data.FriendItem;
import com.caojian.myworkapp.ui.base.BaseNoProgressObserver;
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
                .subscribe(new BaseNoProgressObserver<FriendItem>(activity,this,0) {
                    @Override
                    protected void baseNext(FriendItem friend) {
                        if(friend.getData() != null && friend.getData().getFriends()!=null)
                        {
                            ((MyApplication)activity.getApplication()).setFriendList(friend.getData().getFriends());
                            mView.onSuccess(friend.getData().getFriends());
                        }else {
                            mView.onFailed(friend.getMessage());
                        }
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.onFailed(msg);
                    }
                });
    }
}
