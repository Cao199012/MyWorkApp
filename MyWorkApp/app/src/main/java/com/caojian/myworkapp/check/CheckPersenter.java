package com.caojian.myworkapp.check;

import com.caojian.myworkapp.base.BasePresenter;
import com.caojian.myworkapp.until.RetrofitManger;
import com.caojian.myworkapp.until.Until;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by CJ on 2017/8/20.
 */

public class CheckPersenter extends BasePresenter<CheckContract.View> implements CheckContract.Presenter {

    private CheckContract.View mView;
    public CheckPersenter(CheckContract.View view){
        mView = view;
    }
    @Override
    public void checkPhone(String phone) {
        Retrofit retrofit = RetrofitManger.getRetrofitRxjava(Until.HTTP_BASE_URL);

        CheckService service = retrofit.create(CheckService.class);
        Observable<CheckMsg> observable = service.checkPhone(phone);

        Observer<CheckMsg> observer = new Observer<CheckMsg>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(@NonNull CheckMsg checkMsg) {
                if(checkMsg != null) {
                    if(checkMsg.getRetcode().equals("0"))
                    {
                        mView.checkResultSuccess();
                    }else {
                        mView.checkResultError(checkMsg.getRetinfo());
                    }

                }else
                {
                    mView.checkResultError("网络错误");
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                mView.checkResultError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
        observable.observeOn(AndroidSchedulers.mainThread())
                  .subscribeOn(Schedulers.newThread())
                  .subscribe(observer);

    }
}
