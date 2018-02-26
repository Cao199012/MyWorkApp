package com.caojian.myworkapp.ui.presenter;

import android.util.ArrayMap;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.model.Request.PositionInfoDto;
import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.FriendPosition;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.model.response.UpdateResponse;
import com.caojian.myworkapp.ui.activity.UpdateActivity;
import com.caojian.myworkapp.ui.base.BaseNoProgressObserver;
import com.caojian.myworkapp.ui.base.BaseObserver;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.contract.LocationContract;
import com.caojian.myworkapp.ui.base.BasePresenter;
import com.caojian.myworkapp.until.ActivityUntil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
                .subscribe(new BaseNoProgressObserver<FriendPosition>(activity,this,1) {
                    @Override
                    protected void baseNext(FriendPosition friendPosition) {
                       locationView.showPeopleList(friendPosition.getData().getPositions());
                    }
                    @Override
                    protected void baseError(String msg) {
                      locationView.error(msg);
                    }
                });
    }

    @Override
    public void setFriendsVisibleBeforeExiting(String phoneNos) {
        Observable<CustomResult> observable = service.setFriendsVisibleBeforeExiting(ActivityUntil.getToken(activity),phoneNos);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObserver<BaseResponseResult>(activity,this) {
                    @Override
                    protected void baseNext(BaseResponseResult baseResponseResult) {
                        locationView.setFriendsVisibleBeforeExitingSuccess(baseResponseResult.getCode());
                    }

                    @Override
                    protected void baseError(String msg) {
                        locationView.error(msg);
                    }
                });
    }


    List<PositionInfoDto> positionInfoDtos = new ArrayList<>();
    Map<String,String> mapPic = new LinkedHashMap<>();
    Map<String,String> mapReMarkName = new LinkedHashMap<>();
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
                }else if(friendsAndGroupsMsg.getCode()==3 || friendsAndGroupsMsg.getCode()==2){
                    activity.outLogin(friendsAndGroupsMsg.getMessage());
                    return Observable.fromIterable(_listData);
                }else if(friendsAndGroupsMsg.getCode() == 4  || friendsAndGroupsMsg.getCode() == 5){
                    UpdateResponse.DataBean dataBean = new UpdateResponse.DataBean();
                    dataBean.setComment(friendsAndGroupsMsg.getData().getComment());
                    dataBean.setMandatory(friendsAndGroupsMsg.getData().getMandatory());
                    dataBean.setIsUpdate(friendsAndGroupsMsg.getData().getIsUpdate());
                    dataBean.setDownLoadAddr(friendsAndGroupsMsg.getData().getDownLoadAddr());
                    UpdateActivity.go2UpdateActivity(activity,dataBean,101);
                    if(friendsAndGroupsMsg.getCode() == 4){ //标记已经升级过一次
                        ((MyApplication)activity.getApplicationContext()).setNoForceFlag("1");
                    }
                    return Observable.fromIterable(_listData);
                }else if(friendsAndGroupsMsg.getCode()== 6)
                {
                    activity.showBuyVip();
                    return Observable.fromIterable(_listData);
                }else
                {
                    locationView.error(friendsAndGroupsMsg.getMessage());
                    return Observable.fromIterable(_listData);
                }

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<FriendsAndGroupsMsg.DataBean.GroupsBean>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FriendsAndGroupsMsg.DataBean.GroupsBean groupsBean) {
                        if(groupsBean != null )
                        {
                            for(FriendDetailInfo.DataBean dataBean:groupsBean.getFriends()){
                                mapPic.put(dataBean.getFriendPhoneNo(),dataBean.getHeadPic());
                                mapReMarkName.put(dataBean.getFriendPhoneNo(),dataBean.getFriendRemarkName());
                                PositionInfoDto dto = new PositionInfoDto();
                                dto.setPhoneNo(dataBean.getFriendPhoneNo());
                                positionInfoDtos.add(dto);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if(positionInfoDtos.isEmpty()) return;
                        locationView.showPeopleBeforeExit(positionInfoDtos,mapPic,mapReMarkName);
                    }
                });
    }
}
