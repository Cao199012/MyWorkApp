package com.caojian.myworkapp.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.ui.activity.BuyVipActivity;
import com.caojian.myworkapp.ui.activity.FriendSelectActivity;
import com.caojian.myworkapp.ui.activity.RailSelectActivity;
import com.caojian.myworkapp.widget.MyDialogFragment;
import com.caojian.myworkapp.widget.PersonalInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RailFragment extends Fragment {

    @BindView(R.id.img_location)
    ImageView mImg_location;
    private TextureMapView mapView;
    private BaiduMap mBaiduMap;
    MyDialogFragment mDialogFragment;

    public static RailFragment newInstance(String param1, String param2) {
        RailFragment fragment = new RailFragment();
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_rail,container,false);
        ButterKnife.bind(this,root);
        mapView = (TextureMapView) root.findViewById(R.id.map_rail);
        mBaiduMap = mapView.getMap();
        mDialogFragment = MyDialogFragment.newInstance("暂无权限","此功能需要付费购买","暂不使用","去购买");
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
        if (!checkType()){
            mapView.setVisibility(View.INVISIBLE);
            mDialogFragment.setCancelable(false);
            mDialogFragment.show(getActivity().getSupportFragmentManager(),"buy");
            // ((MainActivity)getActivity()).showDialogFragment("暂无权限","此功能需要付费购买","暂不使用","去购买","buy");
        }
        //initMap();
        return root;
    }

    private void initMap()
    {
        mBaiduMap.setMyLocationEnabled(true);
        // map_rai

        MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(14f);
        mBaiduMap.animateMapStatus(update);
        //获取定位的坐标
        BDLocation bdLocation = ((MyApplication)getActivity().getApplication()).getBdLocation();

        double lat = 39.91173;
        double lot= 116.357428;
        //定义多边形的四个顶点
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        LatLng pt2 = new LatLng(39.91923, 116.327428);
        LatLng pt3 = new LatLng(39.89923, 116.347428);
        LatLng pt4 = new LatLng(39.89923, 116.367428);
        if(bdLocation != null)
        {
            lat = bdLocation.getLatitude();
            lot= bdLocation.getLongitude();
            Log.i("marker",lat+"aaaaaa"+lot);
            //定义多边形的五个顶点
            pt1 = new LatLng(lat+0.02, lot+0.02);
            pt2 = new LatLng(lat+0.02, lot-0.02);
            pt3 = new LatLng(lat-0.02, lot-0.02);
            pt4 = new LatLng(lat-0.02, lot+0.02);
        }


        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                android.graphics.Point point = new android.graphics.Point((int) mImg_location.getX(), (int) mImg_location.getY());

                // 将像素坐标转为地址坐标
                LatLng latLng = mapView.getMap().getProjection().fromScreenLocation(point);
                Log.i("marker",latLng.toString());
            }
        });


        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
//构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(5, 0xAA00FF00))
                .fillColor(0xAAFFFF00);
//在地图上添加多边形Option，用于显示
        mBaiduMap.addOverlay(polygonOption);
        //定位到指定位置
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(lat,lot));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }
    private boolean checkType(){
        PersonalMsg personalMsg =  PersonalInstance.getInstance().getPersonalMsg();
        if( personalMsg != null && personalMsg.getData() != null){
            if(personalMsg.getData().getTrajectoryService().equals("1")){
                return true;
            }
        }
        return false;
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
            if (!checkType()){
                mDialogFragment.setCancelable(false);
                mDialogFragment.show(getActivity().getSupportFragmentManager(),"buy");
                return true;
            }
            RailSelectActivity.go2RailSelectActivity(getActivity(),RailSelectActivity.FROM_RAIL);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        mapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

}
