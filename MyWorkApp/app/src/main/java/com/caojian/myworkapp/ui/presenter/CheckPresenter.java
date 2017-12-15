package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.model.response.CheckMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.CheckContract;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.until.Until;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by CJ on 2017/8/20.
 */

public class CheckPresenter extends BasePresenter<CheckContract.View> implements CheckContract.Presenter {

    private CheckContract.View mView;
    BaseTitleActivity activity;
    public CheckPresenter(BaseTitleActivity activity, CheckContract.View view){
        super(activity);
        mView = view;
        this.activity = activity;
    }
    @Override
    public void checkPhone(String phone) {
        Retrofit retrofit = RetrofitManger.getRetrofitRxjava(Until.HTTP_BASE_URL,activity);
        MyApi service = retrofit.create(MyApi.class);
        Observable<CheckMsg> observable = service.checkPhone(phone);
        BaseObserver<CheckMsg> observer = new BaseObserver<CheckMsg>(activity,this) {
            @Override
            protected void baseNext(CheckMsg checkMsg) {
                if(checkMsg != null) {
                    if(checkMsg.getCode()==0)
                    {
                        mView.checkResultSuccess();
                    }else if(checkMsg.getCode()==3){
                        activity.outLogin(checkMsg.getMessage());
                    }else {
                        mView.checkResultError(checkMsg.getMessage());
                    }
                }else
                {
                    mView.checkResultError("网络错误");
                }
            }
        };
        observable.observeOn(AndroidSchedulers.mainThread())
                  .subscribeOn(Schedulers.newThread())
                  .subscribe(observer);

    }
}
