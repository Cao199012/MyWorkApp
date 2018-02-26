package com.caojian.myworkapp.ui.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.Request.PositionInfoDto;
import com.caojian.myworkapp.model.data.LocationItem;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.ui.activity.BuyVipActivity;
import com.caojian.myworkapp.ui.activity.MainActivity;
import com.caojian.myworkapp.ui.activity.RailSelectActivity;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseFragment;
import com.caojian.myworkapp.ui.contract.LocationContract;
import com.caojian.myworkapp.ui.presenter.LocationPresent;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.MyDialogFragment;
import com.caojian.myworkapp.widget.PersonalInstance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.NOTIFICATION_SERVICE;

/*
*
* 围栏报警
* */

public class FenceAlarmFragment extends MvpBaseFragment<LocationContract.View,LocationPresent> implements LocationContract.View {
    @BindView(R.id.text_location)
    TextView mImg_location;  //未到监测时间、超出时间 文字提示
    private TextureMapView mapView;
    private BaiduMap mBaiduMap;
    MyDialogFragment mDialogFragment;
    MyDialogFragment mDialogFragmentCancel;
    LocationPresent mPresent;
    Handler mHandler = new Handler();
    Runnable mRunnable ;
    String startTime,endTime;  //记录监测的开始和结束时间
    List<PositionInfoDto> mPositionInfoDtos = new ArrayList<>();
    private String mFriendName = "";
    //图片加载设置
    RequestOptions requestOptions = new RequestOptions().overrideOf(100,100).circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL);
    public static FenceAlarmFragment newInstance(String param1, String param2) {
        FenceAlarmFragment fragment = new FenceAlarmFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        setHasOptionsMenu(true);//创建Menu必须设置
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rail,container,false);
        ButterKnife.bind(this,root);
        mapView = (TextureMapView) root.findViewById(R.id.map_rail);
        mBaiduMap = mapView.getMap();
        mDialogFragment = MyDialogFragment.newInstance("暂无权限", Until.DILOG_MSG_RAIL,"暂不使用","立即开通");
        mDialogFragment.setListener(new MyDialogFragment.FragmentDialogListener() {
            @Override
            public void cancel() {
                mDialogFragment.setCancelable(true);
                mDialogFragment.dismiss();
            }
            @Override
            public void sure() {
                mDialogFragment.setCancelable(true);
                mDialogFragment.dismiss();
                BuyVipActivity.go2BuyVipActivity(getActivity(),2);
            }
        });

        mDialogFragmentCancel = MyDialogFragment.newInstance("已有监测信息","是否取消上次监测信息","继续监测","取消监测");
        mDialogFragmentCancel.setListener(new MyDialogFragment.FragmentDialogListener() {
            @Override
            public void cancel() {
                mDialogFragmentCancel.setCancelable(true);
                mDialogFragmentCancel.dismiss();
            }
            @Override
            public void sure() {  //清除记录  再次选择
                mDialogFragmentCancel.setCancelable(true);
                mDialogFragmentCancel.dismiss();
                mPositionInfoDtos.clear(); //清除本地存放的 坐标点
                ActivityUntil.clearRailMsg(getActivity()); //
                mImg_location.setVisibility(View.INVISIBLE);
                mBaiduMap.clear(); //清楚地图上画的位置
                notificationManager = null; //清除弹出框，可以再次震动
                RailSelectActivity.go2RailSelectActivity((BaseTitleActivity) getActivity(),RailSelectActivity.FROM_RAIL);
            }
        });
        if (!checkType()){  //判断是否开通权限
           // mapView.setVisibility(View.INVISIBLE);
            mDialogFragment.setCancelable(false);
           // mDialogFragment.show(getActivity().getSupportFragmentManager(),"buy");
            ActivityUntil.showDialogFragment(getActivity().getSupportFragmentManager(),mDialogFragment,"buy");
        }
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(!outNowTime(startTime) || outNowTime(endTime)){ //判断当前时间是否在                         {
                    ((BaseTitleActivity)getActivity()).showToast("超出监测时间",Toast.LENGTH_SHORT);
                    mImg_location.setText("超出监测时间");
                    mImg_location.setVisibility(View.VISIBLE);
                    return;
                }
                mPresent.getPeopleLocation(new Gson().toJson(mPositionInfoDtos));

                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable,30000);
            }
        };
        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        Observable observable = new Observable() {
            @Override
            protected void subscribeActual(Observer observer) {
                SharedPreferences file =  getActivity().getApplicationContext().getSharedPreferences("railMsg",Context.MODE_PRIVATE);
                if(file != null)
                {
                    if(file.getString("friendId","").isEmpty()){
                        return;
                    }else {
                        Map map = new ArrayMap();
                        map.put("friendId", file.getString("friendId",""));
                        map.put("startTime",file.getString("startTime",""));
                        map.put("endTime",file.getString("endTime",""));
                        map.put("location",file.getString("location",""));
                        map.put("name",file.getString("name",""));
                        observer.onNext(map);
                    }
                }

            }
        };

        if(mPositionInfoDtos.size() > 0){

            mHandler.postDelayed(mRunnable,100);
            return;  //不用每次都操作
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(Map value) {
                        startTime = (String) value.get("startTime");
                        endTime = (String) value.get("endTime");
                        mFriendName = (String) value.get("name");
                        //取出监测好友，定时请求好友位置
                        PositionInfoDto dto = new PositionInfoDto();
                        dto.setPhoneNo((String) value.get("friendId"));
                        mPositionInfoDtos.add(dto);
                        if(!outNowTime(startTime) || outNowTime(endTime)){ //判断当前时间是否在                         {
                            ((BaseTitleActivity)getActivity()).showToast("未到监测时间",Toast.LENGTH_SHORT);
                            mImg_location.setText("未在监测时间内");
                            mImg_location.setVisibility(View.VISIBLE);
                            return;
                        }
                        //取出监测位置
                        String loacation = (String) value.get("location");
                        initMap(new Gson().fromJson(loacation, new TypeToken<List<LatLng>>(){}.getType()));
                        //取出监测好友，定时请求好友位置
                        mHandler.postDelayed(mRunnable,1000);
                    }


                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
    List<LatLng> pts = new ArrayList<LatLng>(); //记录多边形的点
    //根据本地的记录信息 绘画围栏
    private void initMap(ArrayList latList)
    {
        mBaiduMap.setMyLocationEnabled(true);
        pts.clear(); //每次初始化 要清除上次的数据
        MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(14f);
        mBaiduMap.animateMapStatus(update);
        //获取定位的坐标
        //定义多边形的四个顶点
        LatLng pt1 = ((LatLng)latList.get(0));
        LatLng pt2 = ((LatLng)latList.get(1));
        LatLng pt3 = ((LatLng)latList.get(2));
        LatLng pt4 = ((LatLng)latList.get(3));
        double lat = (pt1.latitude+pt2.latitude+pt3.latitude+pt4.latitude)/4;
        double lot= (pt1.longitude+pt2.longitude+pt3.longitude+pt4.longitude)/4;
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
        setSortPoint(pts);

//构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(4, 0xAA3F51B5))
                .fillColor(0xAA999999);
//在地图上添加多边形Option，用于显示
        mBaiduMap.addOverlay(polygonOption);
        //定位到指定位置
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(lat,lot));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }
    /**
     * 根据跟人信息 判断是否已经购买此增值服务
     * */
    private boolean checkType(){
        PersonalMsg personalMsg =  PersonalInstance.getInstance().getPersonalMsg();
        if( personalMsg != null && personalMsg.getData() != null){
            if(personalMsg.getData().getFenceService().equals("2")){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.title_add,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.title_add)
        {
            if (!checkType()){ //每次使用判断权限
                mDialogFragment.setCancelable(false);
              //  mDialogFragment.show(getActivity().getSupportFragmentManager(),"buy");
                ActivityUntil.showDialogFragment(getActivity().getSupportFragmentManager(),mDialogFragment,"buy");
                return true;
            }
            if(mPositionInfoDtos != null  && mPositionInfoDtos.size() > 0){ //判断是否存在记录
                mDialogFragmentCancel.setCancelable(false);
                if(getActivity() != null)
              //  mDialogFragmentCancel.showDialog(getActivity().getSupportFragmentManager(),mDialogFragmentCancel,"buy");
                ActivityUntil.showDialogFragment(getActivity().getSupportFragmentManager(),mDialogFragmentCancel,"buy");
                return true;
            }
            if(mapView.getVisibility() == View.INVISIBLE){
                mapView.setVisibility(View.VISIBLE);
            }
            RailSelectActivity.go2RailSelectActivity((BaseTitleActivity) getActivity(),RailSelectActivity.FROM_RAIL);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    //判断 当前时间 是否大于传入的时间
    private boolean outNowTime(String time)
    {
        Date date = new Date();
        int hour = date.getHours();
        int min = date.getMinutes();
        String [] _times = time.split(":");
        int _time = Integer.parseInt(_times[0]);
        if(hour > _time)
        {
            return true;
        }else if(hour == _time){  //如果小时相等
            if(min > Integer.parseInt(_times[1]))
            {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected LocationPresent createPresenter() {
        mPresent = new LocationPresent((BaseTitleActivity) getActivity(),this);
        return mPresent;
    }

    @Override
    public void showPeopleList(List<LocationItem> peopleList) {
        handleResult(peopleList);
    }

    @Override
    public void showPeopleBeforeExit(List<PositionInfoDto> peopleList,Map map,Map _map) {

    }

    @Override
    public void setFriendsVisibleBeforeExitingSuccess(int code) {

    }

    @Override
    public void error(String msg) {
        ((BaseTitleActivity)getActivity()).showToast(msg,Toast.LENGTH_SHORT);
    }

    //请求检测好友的位置 显示在地图上
    List<Marker> markers = new LinkedList<>();
    NotificationManager notificationManager = null;
    private Map<String,Bitmap> mBitMap = new HashMap<>(); //记录上次的请求好友头像
    private List<String> mPhoneList = new ArrayList<>(); //记录上次的请求好友坐标
    protected void handleResult(List<LocationItem> pListLocation) {

        if(pListLocation == null || pListLocation.size() < 1)
        {
            return;
        }

        for(Marker marker : markers){
            marker.remove();
        }
        markers.clear();
        for (int i = 0; i < pListLocation.size() ;i++)
        {
            LocationItem item = pListLocation.get(i);
            if(item.getLatitude().isEmpty()){
                continue;
            }
//            if(mPhoneList.contains(item.getPhoneNo())){
//
//            }else {
//                mPhoneList.add(item.getPhoneNo());
//                if(getActivity() != null) {
//                    Glide.with(getActivity()).asBitmap().apply(requestOptions).load(Until.HTTP_BASE_URL + "downLoadPic.do?fileId=" + mApplication.getRailPic()).into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                            mBitMap.put(item.getPhoneNo(), resource);
//                        }
//                    });
//                }
//            }
            LatLng point = new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongitude()));
            if(!SpatialRelationUtil.isPolygonContainsPoint(pts,point)){ //判断点是否在检测区域范围
                ((BaseTitleActivity)getActivity()).showToast("已超出监测范围",Toast.LENGTH_LONG);
                if(notificationManager == null){
                    notificationManager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(1,getNotification());
                }
            }
            if (getActivity() == null) return;
            BitmapDescriptor icon;
            if(mBitMap.get(item.getPhoneNo()) == null) {
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.location);
            }else {
                icon = BitmapDescriptorFactory.fromBitmap(mBitMap.get(item.getPhoneNo()));
            }
            OverlayOptions options = new MarkerOptions().position(point).icon(icon);
            // new MarkerOptions().icon(bdOpen_iv);
            Marker marker = (Marker) mBaiduMap.addOverlay(options);
            marker.setTitle(mFriendName);

            markers.add(marker);
            TextView location = new TextView(getActivity());
            location.setBackgroundResource(R.drawable.location_radius);
            location.setPadding(15, 15, 15, 15);
            location.setText(marker.getTitle());
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(location);
            InfoWindow mInfoWindow = new InfoWindow(bitmapDescriptor, marker.getPosition(), -70, new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {
                    //隐藏InfoWindow
                   // mBaiduMap.hideInfoWindow();
                }
            });
            mBaiduMap.showInfoWindow(mInfoWindow);
//            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    if(marker.isDraggable()){
//                        mBaiduMap.hideInfoWindow();
//                        return false;
//
//                    }
////                        mBaiduMap.hideInfoWindow();
//                    if(getActivity() == null) return false;
//                    TextView location = new TextView(getActivity());
//                    location.setBackgroundResource(R.color.background_color);
//                    location.setPadding(15, 15, 15, 15);
//                    location.setText(marker.getTitle());
//                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(location);
//                    InfoWindow mInfoWindow = new InfoWindow(bitmapDescriptor, marker.getPosition(), -70, new InfoWindow.OnInfoWindowClickListener() {
//                        @Override
//                        public void onInfoWindowClick() {
//                            //隐藏InfoWindow
//                            mBaiduMap.hideInfoWindow();
//                        }
//                    });
//                    mBaiduMap.showInfoWindow(mInfoWindow);
//                    return false;
//                }
//            });
        }
    }

    private void setSortPoint(List<LatLng> _latLngs) {
        int len = _latLngs.size();
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (SetSortRule(_latLngs.get(j), _latLngs.get(j + 1))) {
                    LatLng tmp = _latLngs.get(j);
                    _latLngs.set(j,_latLngs.get(j + 1));
                    _latLngs.set(j+1,tmp);
                }
            }
        }
        if((_latLngs.get(0).longitude > _latLngs.get(1).longitude) == (_latLngs.get(2).longitude > _latLngs.get(3).longitude)){
            LatLng tmp = _latLngs.get(2);
            _latLngs.set(2,_latLngs.get(3));
            _latLngs.set(3,tmp);
        }
     //   console.log(arry);
    }

    //两个坐标比较大小
    private boolean SetSortRule(LatLng latLng1, LatLng latLng2) {
        if (latLng1.latitude > latLng2.latitude) {
            return true;
        }
        else if (latLng1.latitude == latLng2.latitude) {
            return (latLng1.longitude > latLng2.longitude);
        }
        else {
            return false;
        }
    }

    private Notification getNotification(){
        Notification.Builder builder = new Notification.Builder(getContext());
        builder.setContentText("监测好友不在围栏范围");
        builder.setContentTitle("围栏通知");
        builder.setSmallIcon(R.mipmap.logo_launcher);
        builder.setTicker("新消息");
        builder.setAutoCancel(true);

        builder.setWhen(System.currentTimeMillis());//设置时间，设置为系统当前的时间
        Intent intent = new Intent(getActivity(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        /**
         * vibrate属性是一个长整型的数组，用于设置手机静止和振动的时长，以毫秒为单位。
         * 参数中下标为0的值表示手机静止的时长，下标为1的值表示手机振动的时长， 下标为2的值又表示手机静止的时长，以此类推。
         */
        long[] vibrates = { 0, 1000, 1000, 1000 };
        notification.vibrate = vibrates;

        /**
         * 手机处于锁屏状态时， LED灯就会不停地闪烁， 提醒用户去查看手机,下面是绿色的灯光一 闪一闪的效果
         */
        notification.ledARGB = Color.GREEN;// 控制 LED 灯的颜色，一般有红绿蓝三种颜色可选
        notification.ledOnMS = 1000;// 指定 LED 灯亮起的时长，以毫秒为单位
        notification.ledOffMS = 1000;// 指定 LED 灯暗去的时长，也是以毫秒为单位
        notification.flags = Notification.FLAG_SHOW_LIGHTS;// 指定通知的一些行为，其中就包括显示

        return  notification;
    }
}
