package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.ActivityControl;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.response.CheckMsg;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.ui.activity.LoginActivity;
import com.caojian.myworkapp.ui.activity.MainActivity;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.CheckContract;
import com.caojian.myworkapp.ui.contract.FriendGroupContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by CJ on 2017/8/20.
 */

public class GetGroupPresenter extends BasePresenter<FriendGroupContract.View> implements FriendGroupContract.Presenter {

    private FriendGroupContract.View mView;
    BaseTitleActivity activity;
    public GetGroupPresenter(BaseTitleActivity activity, FriendGroupContract.View view){
        super(activity);
        mView = view;
        this.activity = activity;
    }
    @Override
    public void getFriendGroup() {
        Observable<FriendsAndGroupsMsg> observable = service.getFriendsAndGroups(ActivityUntil.getToken(activity));
        observable.observeOn(AndroidSchedulers.mainThread())
                .map(new Function<FriendsAndGroupsMsg, List<FriendsAndGroupsMsg.DataBean.GroupsBean>>() {
                    @Override
                    public List<FriendsAndGroupsMsg.DataBean.GroupsBean> apply(FriendsAndGroupsMsg friendsAndGroupsMsg) throws Exception {
                        List _listData = new ArrayList();
                        if(friendsAndGroupsMsg != null) {
                            if(friendsAndGroupsMsg.getCode()==0)
                            {
                                _listData.addAll(friendsAndGroupsMsg.getData().getGroups());
                                FriendsAndGroupsMsg.DataBean.GroupsBean bean = new FriendsAndGroupsMsg.DataBean.GroupsBean();
                                bean.setGroupName("好友列表");
                                bean.setFriends(friendsAndGroupsMsg.getData().getFriends());
                                _listData.add(bean);
                                return _listData;
                            }else if(friendsAndGroupsMsg.getCode()==3){
                                // TODO: 2017/12/4 账号过期  重新登录
                                mView.onFailed(friendsAndGroupsMsg.getMessage());
                                activity.outLogin(friendsAndGroupsMsg.getMessage());
                            } else {
                                mView.onFailed(friendsAndGroupsMsg.getMessage());
                                return _listData;
                            }
                        }
                        return _listData;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<List<FriendsAndGroupsMsg.DataBean.GroupsBean>>(activity,this) {
                    @Override
                    protected void baseNext(List<FriendsAndGroupsMsg.DataBean.GroupsBean> groupsMsg) {
                        if(!groupsMsg.isEmpty()) {
                          mView.onSuccess(groupsMsg);
                        }
                    }
                });

    }
}
