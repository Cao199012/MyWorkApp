package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.response.CheckMsg;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.CheckContract;
import com.caojian.myworkapp.ui.contract.PersonalContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.PersonalInstance;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

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
        BaseObserver<PersonalMsg> observer = new BaseObserver<PersonalMsg>(activity,this) {
            @Override
            protected void baseNext(PersonalMsg personalMsg) {
                if(personalMsg != null) {
                    if(personalMsg.getCode()==0)
                    {
                        mView.getPersonalSuccess(personalMsg);
                        //单例存储个人信息
                        PersonalInstance.getInstance().setPersonalMsg(personalMsg);
                    }else if(personalMsg.getCode()==3){
                        activity.outLogin(personalMsg.getMessage());
                    }else {
                        mView.error(personalMsg.getMessage());
                    }

                }else
                {
                    mView.error("网络错误");
                }
            }
        };
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(observer);
    }

    @Override
    public void changePersonal(PersonalMsg.DataBean personalMsg) {
        Observable<CustomResult> observable = service.modifyMemberInfo(ActivityUntil.getToken(activity),personalMsg.getHeadPic(),personalMsg.getNickName(),personalMsg.getAge());
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult personalMsg) {
                        if(personalMsg != null) {
                            mView.error(personalMsg.getMessage());
                        }else if(personalMsg.getCode()==3){
                            activity.outLogin(personalMsg.getMessage());
                        }else
                        {
                            mView.error("网络错误");
                        }
                    }
                });
    }

    @Override
    public void uploadHeadPic(File file) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);


        Observable<CustomResult> observable = service.uploadHeadPic(ActivityUntil.getToken(activity),body);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<CustomResult>(activity,this) {
                    @Override
                    protected void baseNext(CustomResult resultMsg) {
                        if (resultMsg.getCode() == 0){
                            mView.changeMsgSuccess(resultMsg.getMessage());
                            return;
                        }else if(resultMsg.getCode()==3){
                            activity.outLogin(resultMsg.getMessage());
                            return;
                        }
                        mView.error(resultMsg.getMessage());
                    }
                });
    }

}
