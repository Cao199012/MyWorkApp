package com.caojian.myworkapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.LocationMode;
import com.caojian.myworkapp.model.data.ContactBean;
import com.caojian.myworkapp.model.data.LocationItem;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.widget.UncaughtHandler;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by caojian on 2017/7/19.
 */

public class MyApplication extends Application{
    private Context mContext;
    private String token;
    private LatLng latLng;
    private LatLng mRailPoint;
    private List<ContactBean> mListData; //存储联系人
    private int updateTime = 30*1000;
    private String noForceFlag = "";
    private String trackStartTime,trackEndTime; //记录选择回放轨迹的 的起始时间
    private String railPic,railName; //围栏的头像和名称
    //修改组成员的时候用到
    private List<FriendDetailInfo.DataBean> groupFriends;
    //好友列表
    private List<FriendDetailInfo.DataBean> friendList;
    List<LocationItem> mSeeFriendLocation;
    List<FriendsAndGroupsMsg.DataBean.GroupsBean> mSeeFriends;
    String deviceId;
    public SharedPreferences trackConf = null;
    /**
     * 轨迹客户端
     */
    public LBSTraceClient mClient = null;

    /**
     * 轨迹服务
     */
    public Trace mTrace = null;

    /**
     * 轨迹服务ID
     */
    public long serviceId = 158980;

    /**
     * Entity标识
     */
    public String entityName = "";
    @Override
    public void onCreate() {
        super.onCreate();
        // 加载系统默认设置，字体不随用户设置变化
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics());

        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        //打开缓存文件
        trackConf = getSharedPreferences("track_conf", MODE_PRIVATE);

        mContext = getApplicationContext();
        deviceId = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID); //生成手机唯一识别码

       //百度地图鹰眼追踪轨迹
        mClient = new LBSTraceClient(mContext);
        mClient.setLocationMode(LocationMode.Device_Sensors); //仅GPS定位
        entityName = ActivityUntil.getPhone(mContext);
        if(entityName != null)
        mTrace = new Trace(serviceId, entityName);
        clearTraceStatus();

        //崩溃记录
        UncaughtHandler crashHandler = new UncaughtHandler();
        // 注册crashHandler
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
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

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
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

    public List<ContactBean> getListData() {
        return mListData;
    }

    public void setListData(List<ContactBean> mListData) {
        this.mListData = mListData;
    }

    public String getNoForceFlag() {
        return noForceFlag;
    }

    public void setNoForceFlag(String noForceFlag) {
        this.noForceFlag = noForceFlag;
    }

    public String getTrackStartTime() {
        return trackStartTime;
    }

    public void setTrackStartTime(String trackStartTime) {
        this.trackStartTime = trackStartTime;
    }

    public String getTrackEndTime() {
        return trackEndTime;
    }

    public void setTrackEndTime(String trackEndTime) {
        this.trackEndTime = trackEndTime;
    }

    public String getRailPic() {
        return railPic;
    }

    public void setRailPic(String railPic) {
        this.railPic = railPic;
    }

    public String getRailName() {
        return railName;
    }

    public void setRailName(String railName) {
        this.railName = railName;
    }


    /**
     * 清除Trace状态：初始化app时，判断上次是正常停止服务还是强制杀死进程，根据trackConf中是否有is_trace_started字段进行判断。
     * <p>
     * 停止服务成功后，会将该字段清除；若未清除，表明为非正常停止服务。
     */
    private void clearTraceStatus() {
        if (trackConf.contains("is_trace_started") || trackConf.contains("is_gather_started")) {
            SharedPreferences.Editor editor = trackConf.edit();
            editor.remove("is_trace_started");
            editor.remove("is_gather_started");
            editor.apply();
        }
    }

    /**
     * 初始化请求公共参数
     *
     * @param request
     */
    public void initRequest(BaseRequest request) {
        request.setTag(getTag());
        request.setServiceId(serviceId);
    }
    private AtomicInteger mSequenceGenerator = new AtomicInteger();
    /**
     * 获取请求标识
     *
     * @return
     */
    public int getTag() {
        return mSequenceGenerator.incrementAndGet();
    }

    public LatLng getRailPonit() {
        return mRailPoint;
    }

    public void setRailPonit(LatLng mRailPonit) {
        this.mRailPoint = mRailPonit;
    }
}
