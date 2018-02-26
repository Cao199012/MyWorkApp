package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.CustomResult;
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
        Observable<CustomResult> observable = service.checkPhone(phone);

        observable.observeOn(AndroidSchedulers.mainThread())
                  .subscribeOn(Schedulers.newThread())
                  .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                      @Override
                      protected void baseNext(BaseResponseResult checkMsg) {
                          mView.checkResultSuccess();
                      }
                      @Override
                      protected void baseError(String msg) {
                          mView.checkResultError(msg);
                      }
                  });

    }
}
