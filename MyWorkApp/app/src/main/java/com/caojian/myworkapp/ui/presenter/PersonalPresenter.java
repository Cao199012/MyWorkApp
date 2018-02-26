package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.PersonalContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.widget.PersonalInstance;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by CJ on 2017/8/20.
 */

public class PersonalPresenter extends BasePresenter<PersonalContract.View> implements PersonalContract.Presenter {
    private PersonalContract.View mView;
    BaseTitleActivity activity;
    public PersonalPresenter(BaseTitleActivity activity, PersonalContract.View view){
        super(activity);
        mView = view;
        this.activity = activity;
    }

    @Override
    public void getPersonalInfo() {
        Observable<PersonalMsg> observable = service.getMemberInfo(ActivityUntil.getToken(activity));
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<PersonalMsg>(activity,this) {
                    @Override
                    protected void baseNext(PersonalMsg personalMsg) {
                         PersonalInstance.getInstance().setPersonalMsg(personalMsg);
                        mView.getPersonalSuccess(personalMsg);
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    @Override
    public void changePersonal(PersonalMsg.DataBean personalMsg) {
        Observable<CustomResult> observable = service.modifyMemberInfo(ActivityUntil.getToken(activity),personalMsg.getHeadPic(),personalMsg.getNickName(),"");
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult personalMsg) {
                       mView.changeMsgSuccess(personalMsg.getMessage());
                    }
                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

    @Override
    public void uploadHeadPic(File file) {
        RequestBody requestToken =
                RequestBody.create(MediaType.parse("application/json"), ActivityUntil.getToken(activity));
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);


        Observable<CustomResult> observable = service.uploadHeadPic(requestToken,body);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult resultMsg) {
                       mView.changeMsgSuccess(resultMsg.getMessage());
                    }

                    @Override
                    protected void baseError(String msg) {
                        mView.error(msg);
                    }
                });
    }

}
