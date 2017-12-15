package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.AddGroupContract;
import com.caojian.myworkapp.ui.contract.FriendGroupContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by CJ on 2017/8/20.
 */

public class AddGroupPresenter extends BasePresenter<AddGroupContract.View> implements AddGroupContract.Presenter {

    private AddGroupContract.View mView;
    BaseTitleActivity activity;
    public AddGroupPresenter(BaseTitleActivity activity, AddGroupContract.View view){
        super(activity);
        mView = view;
        this.activity = activity;
    }


    @Override
    public void addGroup(String groupName) {
        Retrofit retrofit = RetrofitManger.getRetrofitRxjava(Until.HTTP_BASE_URL,activity);
        MyApi service = retrofit.create(MyApi.class);
        Observable<CustomResult> observable = service.addGroupInfo(ActivityUntil.getToken(activity),groupName);
        BaseObserver<CustomResult> observer = new BaseObserver<CustomResult>(activity,this) {
            @Override
            protected void baseNext(CustomResult resultMsg) {
                if(resultMsg != null) {
                    if(resultMsg.getCode()==0)
                    {
                        mView.addSuccess(resultMsg.getMessage());
                    }else if(resultMsg.getCode()==3){
                        activity.outLogin(resultMsg.getMessage());
                    }else {
                        mView.addError(resultMsg.getMessage());
                    }
                }else
                {
                    mView.addError("网络错误");
                }
            }
        };
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(observer);

    }
}
