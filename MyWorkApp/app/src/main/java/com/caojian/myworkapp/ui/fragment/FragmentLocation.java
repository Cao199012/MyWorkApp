package com.caojian.myworkapp.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.map.offline.OfflineMapUtil;
import com.baidu.mapapi.model.LatLng;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.caojian.myworkapp.ui.activity.FriendSelectActivity;
import com.caojian.myworkapp.ui.activity.LocationDetailActivity;

import com.caojian.myworkapp.services.UpdateLocationService;
import com.caojian.myworkapp.ui.activity.MainActivity;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.until.ActivityUntil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 2017/8/26.
 */

public class FragmentLocation extends Fragment{

    public FragmentLocation newIntance()
    {

        return new FragmentLocation();
    }

//    @BindView(R.id.mapView)
    TextureMapView mapView;

    private Unbinder unbinder;

    private boolean isFirstLocate = true;
    private BaiduMap baiduMap;

    private LocationClient locationClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location,container,false);

        unbinder = ButterKnife.bind(this,root);
        mapView = (TextureMapView) root.findViewById(R.id.mapView);

        initMap();

        //验证地图和定位权限
        String[] permissions = ActivityUntil.myCheckPermission(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE
        ,Manifest.permission.WRITE_EXTERNAL_STORAGE});
        if(permissions.length > 0)
        {
            ActivityCompat.requestPermissions(getActivity(),permissions,1);
        }else
        {
            requestLocation();
        }
        setHasOptionsMenu(true);
        //启动后台服务
       // UpdateLocationService.startActionBaz(getContext(),"AAA","BBB");
        return root;
    }

    //初始化设置百度地图
    private void initMap()
    {
        MKOfflineMap mOffline = new MKOfflineMap();
        mOffline.init(new MKOfflineMapListener() {
            @Override
            public void onGetOfflineMapState(int i, int i1) {

            }
        });
        MKOLUpdateElement element = null;
        if(mOffline.getAllUpdateInfo() != null && mOffline.getAllUpdateInfo().size() > 0)
        {
            element = mOffline.getAllUpdateInfo().get(0);
        }

//        MapStatus.Builder builder = new MapStatus.Builder();
//
//        LatLng center = new LatLng(39.915071, 116.403907); // 默认 天安门
//        float zoom = 11.0f; // 默认 11级
//        if (null != element) {
//
//            center = new LatLng(element.geoPt.latitude,element.geoPt.longitude);
//            zoom = 11.0f;
//        }
//        builder.target(center).zoom(zoom);

        locationClient = new LocationClient(getActivity().getApplication());
        locationClient.registerLocationListener(new MyLocationListener());

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
//        option.setCoorType("bd0911");// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(10000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向

        locationClient.setLocOption(option);
        baiduMap = mapView.getMap();

        //开启定位
        baiduMap.setMyLocationEnabled(true);
    }
    private void requestLocation() {
        locationClient.start();
    }

    List<Marker> markers = new ArrayList<>();
    BDLocation bdLocation;



    private void navigateTo(BDLocation bdLocation) {

        MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(14f);
        baiduMap.animateMapStatus(update);
        this.bdLocation = bdLocation;
        //向application写入location
        ((MyApplication)getActivity().getApplication()).setBdLocation(bdLocation);
        //跳转到自己的位置
        jump2Location(bdLocation.getLatitude(),bdLocation.getLongitude());

        // 构建marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.location);

        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,bitmap);
        baiduMap.setMyLocationConfiguration(configuration);

    }

    //定位到指定坐标
    private void jump2Location(double lat, double lot)
    {
        LatLng ll = new LatLng(lat,lot);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);

        baiduMap.animateMapStatus(update);
        MyLocationData.Builder locationBuilde = new MyLocationData.Builder();
        locationBuilde.latitude(lat);
        locationBuilde.longitude(lot);
        MyLocationData locationData = locationBuilde.build();

        baiduMap.setMyLocationData(locationData);
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
                    requestLocation();
                }
                break;
            default:
        }
    }

    private class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
            currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
            currentPosition.append("定位方式：");
            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation)
            {
                currentPosition.append("GPS");
            }else if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation)
            {
                currentPosition.append("网络");
            }
            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation)
            {
                navigateTo(bdLocation);
                ((MainActivity)getActivity()).setLoacalCity(bdLocation.getCity(),Integer.parseInt(bdLocation.getCityCode()));
            }

//            mTv_position.setText(currentPosition);

        }

    }

    @OnClick(R.id.btn_select)
    public void selectFreind(View v)
    {
        FriendSelectActivity.go2FriendSelectActivity(getActivity(),102,"选择好友");
    }

    public void handleResult(int num) {

        if (bdLocation == null)
        {
            ((BaseTitleActivity)getActivity()).showToast("请开始定位设置",Toast.LENGTH_LONG);
            return;
        }
        for(Marker marker : markers){
            marker.remove();
        }
            for (int i = 0; i < num ;i++)
            {
                LatLng point = new LatLng(bdLocation.getLatitude()+0.03*(i+1),bdLocation.getLongitude()+0.05*i);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.location);
                OverlayOptions options = new MarkerOptions().position(point).icon(icon);
                //创建InfoWindow展示的view
//                TextView textView = new TextView(getActivity().getApplicationContext());
//                textView.setText("xxxxxx");
                View textView = View.inflate(getActivity(),R.layout.sample_my_view,null);

               Marker marker = (Marker) baiduMap.addOverlay(options);
                markers.add(marker);

                baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if(marker.isDraggable()){
                            return false;
                        }
                        baiduMap.hideInfoWindow();
                        InfoWindow mInfoWindow = new InfoWindow(textView, marker.getPosition(), -60);
                        baiduMap.showInfoWindow(mInfoWindow);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case LocationDetailActivity.REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK)
                {
                    double lat = data.getDoubleExtra("lat",bdLocation.getLatitude());
                    double lot = data.getDoubleExtra("l0t",bdLocation.getLongitude());
                    //jump2Location(lat,lot);
                    return;
                }
                if(requestCode == Activity.RESULT_CANCELED)
                {
                    // TODO: 2017/10/17 删除显示的好友 
                }
                break;

        }
       // super.onActivityResult(requestCode, resultCode, data);
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
        locationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
}
