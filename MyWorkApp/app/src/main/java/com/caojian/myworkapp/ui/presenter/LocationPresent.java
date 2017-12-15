package com.caojian.myworkapp.ui.presenter;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.Request.PositionInfoDto;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.FriendPosition;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.LocationContract;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.ui.contract.LoginContract;
import com.caojian.myworkapp.ui.contract.PasswordContract;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by caojian on 2017/8/31.
 */

public class LocationPresent extends BasePresenter<LocationContract.View> implements LocationContract.Presenter{

    private LocationContract.View locationView;
    BaseTitleActivity activity;
    public LocationPresent(BaseTitleActivity activity,  LocationContract.View view){
        super(activity);
        locationView = view;
        this.activity = activity;
    }
    //获取好友位置
    @Override
    public void getPeopleLocation(String phoneNo) {
        Observable<FriendPosition> observable = service.getFriendPosition(ActivityUntil.getToken(activity),phoneNo);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<FriendPosition>(activity,this) {
                    @Override
                    protected void baseNext(FriendPosition friendPosition) {
                        if(friendPosition.getCode() == 0)
                        {
                            locationView.showPeopleList(friendPosition.getData().getPositions());
                        }else if(friendPosition.getCode()==3){
                            activity.outLogin(friendPosition.getMessage());
                        }else
                        {
                            locationView.error(friendPosition.getMessage());
                        }
                    }
                });
    }

    @Override
    public void getGroupLocation(String groupId) {
        Observable<FriendPosition> observable = service.getRealtimePositionByGroupId(ActivityUntil.getToken(activity),groupId);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<FriendPosition>(activity,this) {
                    @Override
                    protected void baseNext(FriendPosition friendPosition) {
                        if(friendPosition.getCode() == 0)
                        {
                            locationView.showPeopleList(friendPosition.getData().getPositions());
                        }else if(friendPosition.getCode()==3){
                            activity.outLogin(friendPosition.getMessage());
                        }else
                        {
                            locationView.error(friendPosition.getMessage());
                        }
                    }
                });
    }
    List<PositionInfoDto> positionInfoDtos = new ArrayList<>();
    @Override
    public void getPositionsBeforeExit() {
        Observable<FriendsAndGroupsMsg> observable = service.getUserViewBeforeExit(ActivityUntil.getToken(activity));
        observable.concatMap(new Function<FriendsAndGroupsMsg, ObservableSource<FriendsAndGroupsMsg.DataBean.GroupsBean>>() {
            @Override
            public Observable<FriendsAndGroupsMsg.DataBean.GroupsBean> apply(FriendsAndGroupsMsg friendsAndGroupsMsg) throws Exception {
                List<FriendsAndGroupsMsg.DataBean.GroupsBean> _listData = new ArrayList<>();
                if(friendsAndGroupsMsg.getCode() == 0)
                {
                    positionInfoDtos.clear(); //返回数据前先清空之前的数据
                    _listData.addAll(friendsAndGroupsMsg.getData().getGroups());
                    FriendsAndGroupsMsg.DataBean.GroupsBean bean = new FriendsAndGroupsMsg.DataBean.GroupsBean();
                    bean.setGroupName("好友列表");
                    bean.setFriends(friendsAndGroupsMsg.getData().getFriends());
                    _listData.add(bean);
                    ((MyApplication)activity.getApplication()).setmSeeFriends(_listData);
                    //return Observable.fromArray((FriendsAndGroupsMsg.DataBean.GroupsBean[]) _listData.toArray());
                    return Observable.fromIterable(_listData);
                }else if(friendsAndGroupsMsg.getCode()==3){
                    activity.outLogin(friendsAndGroupsMsg.getMessage());
                    return Observable.fromIterable(_listData);
                }else
                {
                    locationView.error(friendsAndGroupsMsg.getMessage());
                    return Observable.fromIterable(_listData);
                }

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<FriendsAndGroupsMsg.DataBean.GroupsBean>(activity,this) {
                    @Override
                    protected void baseNext( FriendsAndGroupsMsg.DataBean.GroupsBean groupsBean) {
                        if(groupsBean != null )
                        {
                                for(FriendDetailInfo.DataBean dataBean:groupsBean.getFriends()){
                                    PositionInfoDto dto = new PositionInfoDto();
                                    dto.setPhoneNo(dataBean.getFriendPhoneNo());
                                    positionInfoDtos.add(dto);
                                }
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if(positionInfoDtos.isEmpty()) return;
                        locationView.showPeopleBeforeExit(positionInfoDtos);
                    }
                });
    }
}
