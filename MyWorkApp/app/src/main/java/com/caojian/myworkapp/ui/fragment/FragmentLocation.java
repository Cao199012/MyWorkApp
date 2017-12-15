package com.caojian.myworkapp.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.location.c.a;
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
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.converter.gson.GsonConverterFactory;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.caojian.myworkapp.model.Request.PositionInfoDto;
import com.caojian.myworkapp.model.data.LocationItem;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.ui.activity.FriendSelectActivity;
import com.caojian.myworkapp.ui.activity.LocationDetailActivity;

import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseFragment;
import com.caojian.myworkapp.ui.contract.LocationContract;
import com.caojian.myworkapp.ui.presenter.LocationPresent;
import com.caojian.myworkapp.until.ActivityUntil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 2017/8/26.
 */

public class FragmentLocation extends MvpBaseFragment<LocationContract.View,LocationPresent>implements LocationContract.View{

    private static final int TIME_WIFI = 5000;
    private static final int TIME_GPS = 20000;
    private int netType;
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
    MyLocationConfiguration configuration;
    //地图覆盖点
    List<Marker> markers = new ArrayList<>();
    //记录当前定位信息
    BDLocation mBdLocation;
    MyApplication mApplication;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location,container,false);
        unbinder = ButterKnife.bind(this,root);
        mapView = (TextureMapView) root.findViewById(R.id.mapView);
        mApplication = (MyApplication) getActivity().getApplication();
        initMap();
        //验证地图和定位权限
        String[] permissions = ActivityUntil.myCheckPermission(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE
        ,Manifest.permission.WRITE_EXTERNAL_STORAGE});
        if(permissions.length > 0)
        {
            ActivityCompat.requestPermissions(getActivity(),permissions,1);
        }else
        {
            mLocationClient.start();//百度地图开启定位
        }
        setHasOptionsMenu(true);
        // 构建marker图标(本人定位图标)
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.location);
        configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,bitmap);
        mBaiduMap.setMyLocationConfiguration(configuration);
        //请求上次查看的好友
        mPresenter.getPositionsBeforeExit();
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
        int _type = ActivityUntil.getActiveNetWork(getActivity());
        if( _type != -1)
        {
            option = ActivityUntil.getDefaultOption(TIME_GPS*(_type+1));
        }else {
            option = ActivityUntil.getDefaultOption(TIME_GPS);
        }
        netType = _type; //记录当前网路类型和下次定位时比较
        mLocationClient.setLocOption(option);

    }

    //地图移动到指定位置
    private void jump2Location(double lat, double lot)
    {
        LatLng ll=new LatLng(lat,lot);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .accuracy(mBdLocation.getRadius())
                .latitude(ll.latitude)
                .longitude(ll.longitude)
                .build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                //要移动的点
                .target(ll)
                //放大地图到18倍
                .zoom(18)
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
                            ((BaseTitleActivity)getActivity()).showToast("必须同意所有权限才能使用", Toast.LENGTH_LONG);
                            return;
                        }
                    }
                    mLocationClient.start();
                }
                break;
            default:
        }
    }

    //请求获取的显示信息
    @Override
    public void showPeopleList(List<LocationItem> peopleList) {
        handleResult(peopleList);
    }

    //获取退出前查看的好友
    @Override
    public void showPeopleBeforeExit(List<PositionInfoDto> peopleList) {
        //记录检测好友的列表
        String str = new Gson().toJson(peopleList);
        mPresenter.getPeopleLocation(str);
    }

    //接口请求错误
    @Override
    public void error(String msg) {
        ((BaseTitleActivity)getActivity()).showToast(msg,Toast.LENGTH_SHORT);
    }

    //定位信息返回监听
    private class MyLocationListener extends com.baidu.location.a {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if(ActivityUntil.getActiveNetWork(getActivity()) != netType ) {
                LocationClientOption option = mLocationClient.getLocOption();
                option.setScanSpan(TIME_GPS*(netType+1));
                mLocationClient.setLocOption(option);
            }
            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation)
            {
                mBdLocation = bdLocation;
                //向application写入location
                ((MyApplication)getActivity().getApplication()).setBdLocation(bdLocation);
                //跳转到自己的位置()
                if(firstLocation == true)
                {
                    jump2Location(bdLocation.getLatitude(),bdLocation.getLongitude());
                }
            }
        }
    }
    @OnClick(R.id.btn_select)
    public void selectFriend(View v)
    {
        FriendSelectActivity.go2FriendSelectActivity(getActivity(),FriendSelectActivity.SELECT_MAIN,"选择好友");
    }

    //请求检测好友的位置 显示在地图上
    protected void handleResult(List<LocationItem> pListLocation) {
        if(pListLocation == null || pListLocation.size() < 1)
        {
            return;
        }
        if (mBdLocation == null)
        {
            ((BaseTitleActivity)getActivity()).showToast("请开始定位设置",Toast.LENGTH_LONG);
            return;
        }
        mApplication.setmSeeFriendLocation(pListLocation);
        for(Marker marker : markers){
            marker.remove();
        }
            for (int i = 0; i < pListLocation.size() ;i++)
            {
                LocationItem item = pListLocation.get(i);
                if(item.getLatitude().isEmpty()){
                    return;
                }
                LatLng point = new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongitude()));
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.location);
                OverlayOptions options = new MarkerOptions().position(point).icon(icon);
                View textView = View.inflate(getActivity(),R.layout.sample_my_view,null);
                Marker marker = (Marker) mBaiduMap.addOverlay(options);
                markers.add(marker);
                mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if(marker.isDraggable()){
                            return false;
                        }
                        mBaiduMap.hideInfoWindow();
                        InfoWindow mInfoWindow = new InfoWindow(textView, marker.getPosition(), -60);
                        mBaiduMap.showInfoWindow(mInfoWindow);
                        return false;
                    }
                });
            }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.title_locations,menu);
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
                    double lat = data.getDoubleExtra("lat", mBdLocation.getLatitude());
                    double lot = data.getDoubleExtra("lot", mBdLocation.getLongitude());
                    jump2Location(lat,lot);
                    //好友点击定位取消自己的自动定位
                    firstLocation = false;
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
                    List<PositionInfoDto> positionInfoDtos = new ArrayList<>();
                    for (String friendNo : _list){
                        PositionInfoDto dto = new PositionInfoDto();
                        dto.setPhoneNo(friendNo);
                    }
                    String str = new Gson().toJson(positionInfoDtos);
                    //mPresenter.getPeopleLocation(positionInfoDtos);
                    mPresenter.getPeopleLocation(str);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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
