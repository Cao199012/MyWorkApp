package com.caojian.myworkapp.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.caojian.myworkapp.model.Request.PositionInfoDto;
import com.caojian.myworkapp.model.data.LocationItem;
import com.caojian.myworkapp.services.UpdateLocationService;
import com.caojian.myworkapp.ui.activity.FriendSelectActivity;
import com.caojian.myworkapp.ui.activity.LocationDetailActivity;

import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseFragment;
import com.caojian.myworkapp.ui.contract.LocationContract;
import com.caojian.myworkapp.ui.presenter.LocationPresent;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.ImageLoad;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.baidu.mapapi.map.InfoWindow.*;

/**
 * Created by caojian on 2017/8/26.
 */

public class FragmentLocation extends MvpBaseFragment<LocationContract.View,LocationPresent>implements LocationContract.View{

    private static final int TIME_GPS = 30000;
    public static FragmentLocation newInstance()
    {
        return new FragmentLocation();
    }
//    @BindView(R.id.mapView)
    TextureMapView mapView;
    private Unbinder unbinder;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private boolean firstLocation = true;
    LocationPresent mPresenter;
    //MyLocationConfiguration configuration;
    //地图覆盖点
    List<Marker> markers = new ArrayList<>();
    //记录当前定位信息
    BDLocation mBdLocation;
    MyApplication mApplication;
    String pepopleJsonMsg = null;//获取退出前好友id
    Runnable mRunnable = null; //定时查询好友位置
    Handler mHandler = new Handler();
    private List<LatLng> mLocationNumList = new ArrayList<>();
    //图片加载设置
    RequestOptions requestOptions = new RequestOptions().overrideOf(100,100).diskCacheStrategy(DiskCacheStrategy.ALL);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location,container,false);
        unbinder = ButterKnife.bind(this,root);
        mapView = (TextureMapView) root.findViewById(R.id.mapView);
        mApplication = (MyApplication) getActivity().getApplication();
        initMap();
        //验证地图和定位权限
        String[] permissions = ActivityUntil.myCheckPermission(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE});
        if(permissions.length > 0)
        {
            ActivityCompat.requestPermissions(getActivity(),permissions,1);
        }else
        {
            startLocation();//百度地图开启定位
        }
        setHasOptionsMenu(true);
        // 构建marker图标(本人定位图标)
        //BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.location);

        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,null));
        //请求上次查看的好友
        mPresenter.getPositionsBeforeExit();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(pepopleJsonMsg == null ||  pepopleJsonMsg.isEmpty() || pepopleJsonMsg.equals("[]")){
                    mHandler.removeCallbacks(mRunnable);
                    return;
                }
                mPresenter.getPeopleLocation(pepopleJsonMsg);
                mHandler.postDelayed(mRunnable,30000); //定时查询
            }
        };
        return root;
    }

    //初始化设置百度地图
    private void initMap()
    {
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true); //设置定位
        mLocationClient = new LocationClient(getActivity());
        mLocationClient.registerLocationListener(new MyLocationListener());
        LocationClientOption option ;
        option = ActivityUntil.getDefaultOption(TIME_GPS);
        mLocationClient.setLocOption(option);

    }


    //地图移动到指定位置
    private void jump2Location(double lat, double lot)
    {
        LatLng ll=new LatLng(lat,lot);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                //要移动的点
                .target(ll)
                //放大地图到18倍
                .zoom(16)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if(grantResults.length > 0)
                {
                    for (int result : grantResults)
                    {
                        if (result != PackageManager.PERMISSION_GRANTED)
                        {
                            ((BaseTitleActivity)getActivity()).showToast("必须同意权限才能使用", Toast.LENGTH_LONG);
                            return;
                        }
                    }
                    startLocation();
                }else {
                    startLocation();
                }
                break;
            default:
        }
    }

    //开始定位，并判断是否开启定位按钮
    private void startLocation()
    {
        if(!ActivityUntil.isOPen(getContext())){
            ActivityUntil.openGPS(getContext()); //强制打开定位
        }
        mLocationClient.start();
    }
    //请求获取的显示信息
    @Override
    public void showPeopleList(List<LocationItem> peopleList) {
        handleResult(peopleList);
    }

    //获取退出前查看的好友
    Map<String,String> mMapPic = new LinkedHashMap<>();  //手机号对应的 头像地址
    Map<String,String> mMapName = new LinkedHashMap<>();  //手机号对应的好友昵称
    @Override
    public void showPeopleBeforeExit(List<PositionInfoDto> peopleList, Map pMapPic, Map pMapName) {
        //记录检测好友的列表
        String str = new Gson().toJson(peopleList);
        pepopleJsonMsg = str;
        mHandler.postDelayed(mRunnable,0);
        mMapPic = pMapPic;  //好友头像地址
        mMapName = pMapName; //好友昵称

    }

    //设置监测好友成功
    @Override
    public void setFriendsVisibleBeforeExitingSuccess(int code) {

        mHandler.postDelayed(mRunnable,0);
        mPresenter.getPositionsBeforeExit();
    }

    //接口请求错误
    @Override
    public void error(String msg) {
        if(getActivity() == null) return;
        ((BaseTitleActivity)getActivity()).showToast(msg,Toast.LENGTH_SHORT);
    }

    //定位信息返回监听
    private class MyLocationListener extends com.baidu.location.a {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {


            if(bdLocation != null ) {
//
                if(bdLocation.getLocType() == BDLocation.TypeGpsLocation  || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation )
                    ((MyApplication) getActivity().getApplication()).setLatLng(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
                mBdLocation = bdLocation;
                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude())
                        .build();
                // 设置定位数据
                mBaiduMap.setMyLocationData(locData);  // 更新自己的位置坐标
                ((MyApplication) getActivity().getApplication()).setRailPonit(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));

                //跳转到自己的位置()
                if (firstLocation == true) {
                    firstLocation = false;
                    UpdateLocationService.startActionBaz(getActivity(), "AAA", "BBB"); //启动上传service
                    jump2Location(bdLocation.getLatitude(), bdLocation.getLongitude());  //跳转到当前位置
                }
            }
//
        }
    }
    //进入好友选择页面
    @OnClick(R.id.btn_select)
    public void selectFriend(View v)
    {
        FriendSelectActivity.go2FriendSelectActivity(getActivity(),FriendSelectActivity.SELECT_MAIN,"选择好友");
    }
    //点击定位按钮跳转到当前位置
    @OnClick(R.id.my_location)
    public void go2MyLocation(){
        if(mBdLocation == null) return;
        jump2Location(mBdLocation.getLatitude(),mBdLocation.getLongitude());
    }

    //请求检测好友的位置 显示在地图上
 //   private List<LocationItem> mListLocation = new ArrayList<>(); //记录上次的请求好友坐标
    private Map<String,Bitmap> mBitMap = new HashMap<>(); //记录上次的请求好友头像
    private List<String> mPhoneList = new ArrayList<>(); //记录上次的请求好友坐标

    protected void handleResult(List<LocationItem> pListLocation) {
        if(pListLocation == null || pListLocation.size() < 1)
        {
            return;
        }

        mApplication.setmSeeFriendLocation(pListLocation);
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
                if(mPhoneList.contains(item.getPhoneNo())){

                }else {
                    mPhoneList.add(item.getPhoneNo()); //标记加载图片
                    Bitmap bitmap = ImageLoad.getBitmap(mMapPic.get(item.getPhoneNo()));
                    if(bitmap != null){
                        mBitMap.put(item.getPhoneNo(), bitmap);
                    }else {

                        if (getActivity() != null && mMapPic.get(item.getPhoneNo()) != null && !mMapPic.get(item.getPhoneNo()).isEmpty()) { //头像地址不为空
                            Glide.with(getActivity()).asBitmap().apply(requestOptions).load(Until.HTTP_BASE_URL + "downLoadPic.do?fileId=" + mMapPic.get(item.getPhoneNo())).into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                                    ImageLoad.putBitmap(mMapPic.get(item.getPhoneNo()), resource);
//
                                    mBitMap.put(item.getPhoneNo(), ImageLoad.getBitmap(mMapPic.get(item.getPhoneNo())));


                                }
                            });
                        }
                    }
                }
                LatLng point = new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongitude()));

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
                marker.setTitle(mMapName.get(item.getPhoneNo()));

                markers.add(marker);

                mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if(marker.isDraggable()){
                            mBaiduMap.hideInfoWindow();
                            return false;

                        }
//                        mBaiduMap.hideInfoWindow();
                        if(getActivity() == null) return false;
                        TextView location = new TextView(getActivity());
                        location.setBackgroundResource(R.drawable.location_radius);
                        location.setPadding(20, 20, 20, 20);
                        location.setText(marker.getTitle());
                        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(location);
                        InfoWindow mInfoWindow = new InfoWindow(bitmapDescriptor, marker.getPosition(), -100, new OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick() {
                                //隐藏InfoWindow
                                mBaiduMap.hideInfoWindow();
                            }
                        });
                        mBaiduMap.showInfoWindow(mInfoWindow);
                        return false;
                    }
                });
            }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.title_locations,menu);
        MenuItem item = menu.findItem(R.id.title_drop);
        SpannableString spannableString = new SpannableString(item.getTitle());
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), 0);
        item.setTitle(spannableString);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.title_drop)
        {
            LocationDetailActivity.go2LocationDetailActivity(getActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //从选择好友页面返回
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case LocationDetailActivity.REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK && data != null)
                {
                    String lat = data.getStringExtra("lat");
                    String lot = data.getStringExtra("lot");
                    if(lat == null || lat.isEmpty() || lot == null || lot.isEmpty()) return;
                    jump2Location(Double.parseDouble(lat),Double.parseDouble(lot));
                    //好友点击定位取消自己的自动定位
                    //firstLocation = false;
                    return;
                }
                if(requestCode == Activity.RESULT_CANCELED)
                {
                    // TODO: 2017/10/17 删除显示的好友
                    mPresenter.getPositionsBeforeExit();
                }
                break;
            case FriendSelectActivity.SELECT_MAIN:
                if(resultCode == Activity.RESULT_OK && data != null) {
                    List<String> _list = data.getStringArrayListExtra("location");
                    if(_list == null){  //当list为null时 直接返回                        return;
                    }
                    List<PositionInfoDto> positionInfoDtos = new ArrayList<>();
                    for (String friendNo : _list){
                        PositionInfoDto dto = new PositionInfoDto();
                        dto.setPhoneNo(friendNo);
                        positionInfoDtos.add(dto);
                    }
                    String str = new Gson().toJson(positionInfoDtos);
                    pepopleJsonMsg = str;  //本地记录监测好友 查询坐标
                    //设置监测好友
                    mPresenter.setFriendsVisibleBeforeExiting(new Gson().toJson(_list));
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if(pepopleJsonMsg != null){
            mHandler.postDelayed(mRunnable,30000);
        }
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
        unbinder.unbind();
        mLocationClient.stop();
        mapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected LocationPresent createPresenter() {
        mPresenter = new LocationPresent((BaseTitleActivity) getActivity(),this);
        return mPresenter;
    }
}
