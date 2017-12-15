package com.caojian.myworkapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.caojian.myworkapp.model.data.LocationItem;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;

import java.util.List;

/**
 * Created by caojian on 2017/7/19.
 */

public class MyApplication extends Application{
    private Context context;
    private String token;
    private BDLocation bdLocation;

    private int updateTime = 60*1000;
    //修改组成员的时候用到
    private List<FriendDetailInfo.DataBean> groupFriends;
    //好友列表
    private List<FriendDetailInfo.DataBean> friendList;
    List<LocationItem> mSeeFriendLocation;
    List<FriendsAndGroupsMsg.DataBean.GroupsBean> mSeeFriends;
    String deviceId;
    @Override
    public void onCreate() {
        super.onCreate();
        // 加载系统默认设置，字体不随用户设置变化
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics());
        SDKInitializer.initialize(getApplicationContext());
        context = getApplicationContext();

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        if (tm.getDeviceId() != null) {
//            deviceId = tm.getDeviceId();
//        } else {
//            //android.provider.Settings;
            deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
      //  }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BDLocation getBdLocation() {
        return bdLocation;
    }

    public void setBdLocation(BDLocation bdLocation) {
        this.bdLocation = bdLocation;
    }
    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public List<FriendDetailInfo.DataBean> getGroupFriends() {
        return groupFriends;
    }

    public void setGroupFriends(List<FriendDetailInfo.DataBean> groupFriends) {
        this.groupFriends = groupFriends;
    }

    public List<FriendDetailInfo.DataBean> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<FriendDetailInfo.DataBean> friendList) {
        this.friendList = friendList;
    }

    public List<LocationItem> getmSeeFriendLocation() {
        return mSeeFriendLocation;
    }

    public void setmSeeFriendLocation(List<LocationItem> mSeeFriendLocation) {
        this.mSeeFriendLocation = mSeeFriendLocation;
    }

    public List<FriendsAndGroupsMsg.DataBean.GroupsBean> getmSeeFriends() {
        return mSeeFriends;
    }

    public void setmSeeFriends(List<FriendsAndGroupsMsg.DataBean.GroupsBean> mSeeFriends) {
        this.mSeeFriends = mSeeFriends;
    }
}
