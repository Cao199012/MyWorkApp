package com.caojian.myworkapp.modules.location;

import android.Manifest;
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
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.caojian.myworkapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.caojian.myworkapp.services.UpdateLocationService;
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
    MapView mapView;

    private Unbinder unbinder;

    private boolean isFirstLocate = true;
    private BaiduMap baiduMap;

    private LocationClient locationClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location,container,false);

        unbinder = ButterKnife.bind(this,root);


        locationClient = new LocationClient(getActivity().getApplication());
        locationClient.registerLocationListener(new MyLocationListener());

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
//        option.setCoorType("bd0911");// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向

        locationClient.setLocOption(option);
        mapView = (MapView) root.findViewById(R.id.mapView);
        baiduMap = mapView.getMap();

        //开启定位
        baiduMap.setMyLocationEnabled(true);

        List<String> permission = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(!permission.isEmpty())
        {
            String[] permissions = permission.toArray(new String[permission.size()]);
            ActivityCompat.requestPermissions(getActivity(),permissions,1);
        }else
        {
            requestLocation();
        }
        setHasOptionsMenu(true);
        //启动后台服务
        UpdateLocationService.startActionBaz(getContext(),"AAA","BBB");
        return root;
    }

    private void requestLocation() {
        locationClient.start();
    }

    List<Marker> markers = new ArrayList<>();
    BDLocation bdLocation;

    private void navigateTo(BDLocation bdLocation) {
        if(isFirstLocate)
        {
            MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(14f);
            baiduMap.animateMapStatus(update);
            LatLng ll = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
            this.bdLocation = bdLocation;
        }
        MyLocationData.Builder locationBuilde = new MyLocationData.Builder();
        locationBuilde.latitude(bdLocation.getLatitude());
        locationBuilde.longitude(bdLocation.getLongitude());
        MyLocationData locationData = locationBuilde.build();

        baiduMap.setMyLocationData(locationData);
        // 构建marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.location);

       // MyLocationConfiguration.LocationMode locationMode = new
        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,bitmap);
        baiduMap.setMyLocationConfiguration(configuration);
//        MyLocationConfiguration configuration = new MyLocationConfiguration(
//                MyLocationConfiguration.LocationMode.FOLLOWING, true, geo);
//        baiduMap.setMyLocationConfigeration(configuration);// 设置定位模式
        baiduMap.setMyLocationEnabled(true);// 打开定位图层
        //baidumap.
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
                            ActivityUntil.showToast(getActivity(),"必须同意所有权限才能使用", Toast.LENGTH_LONG);
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
            }

//            mTv_position.setText(currentPosition);

        }

    }

    @OnClick(R.id.btn_select)
    public void selectFreind(View v)
    {
        FriendSelectActivity.go2FriendSelectActivity(getActivity());
    }

    public void handleResult(int num) {

        if (bdLocation == null)
        {
            ActivityUntil.showToast(getActivity(),"请开始定位设置",Toast.LENGTH_LONG);
            return;
        }
            for (int i = 0; i < num ;i++)
            {
                LatLng point = new LatLng(bdLocation.getLatitude()+0.03*i,bdLocation.getLongitude()+0.05*i);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.location);
                OverlayOptions options = new MarkerOptions().position(point).icon(icon);
                //创建InfoWindow展示的view
//                TextView textView = new TextView(getActivity().getApplicationContext());
//                textView.setText("xxxxxx");
                View textView = View.inflate(getActivity(),R.layout.sample_my_view,null);
                //定义用于显示该InfoWindow的坐标点
                //LatLng pt = new LatLng(39.86923, 116.397428);
                //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                //显示InfoWindow
               Marker marker = (Marker) baiduMap.addOverlay(options);
                markers.add(marker);
                baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
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
        inflater.inflate(R.menu.title_add,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.title_add)
        {
            LocationDetailActivity.go2LocationDetailActivity(getActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
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
