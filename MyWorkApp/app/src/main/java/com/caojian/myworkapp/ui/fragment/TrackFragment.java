package com.caojian.myworkapp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.ui.activity.BuyVipActivity;
import com.caojian.myworkapp.ui.activity.FriendSelectActivity;
import com.caojian.myworkapp.ui.activity.MainActivity;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.activity.RailSelectActivity;
import com.caojian.myworkapp.widget.MyDialogFragment;
import com.caojian.myworkapp.widget.MyNewDialogFragment;
import com.caojian.myworkapp.widget.PersonalInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrackFragment extends Fragment {

    public static TrackFragment newInstance() {
        TrackFragment fragment = new TrackFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    TextureMapView mTextureMapView;
    BaiduMap mBaiduMap;
    MyDialogFragment mDialogFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_track, container, false);
        mTextureMapView = (TextureMapView) root.findViewById(R.id.track_map);
        mBaiduMap = mTextureMapView.getMap();
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
                BuyVipActivity.go2BuyVipActivity(getActivity(),1);
            }
        });
       if (!checkType()){
           mTextureMapView.setVisibility(View.INVISIBLE);
            mDialogFragment.setCancelable(false);
            mDialogFragment.show(getActivity().getSupportFragmentManager(),"buy");
          // ((MainActivity)getActivity()).showDialogFragment("暂无权限","此功能需要付费购买","暂不使用","去购买","buy");
       }
        return root;
    }

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
                mDialogFragment.show(getActivity().getSupportFragmentManager(),"buy");                return true;
            }
            FriendSelectActivity.go2FriendSelectActivity(getActivity(),FriendSelectActivity.SELECT_TRACK,"好友轨迹");
           //RailSelectActivity.go2RailSelectActivity(getActivity(),RailSelectActivity.FROM_TRACK);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkType(){
        PersonalMsg personalMsg =  PersonalInstance.getInstance().getPersonalMsg();
        if( personalMsg != null && personalMsg.getData() != null){
            if(personalMsg.getData().getFenceService().equals("1")){
                return true;
            }
        }
        return false;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        mTextureMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mTextureMapView.onPause();
    }
    List<LatLng> latLngPolygon = new ArrayList<LatLng>();
//    public void drawStart() throws JSONException {
//
//        //向latLngPolygon中添加获取到的所有坐标点
//
//        for (int i = 0; i < json_data.length(); i++) {
//            JSONObject jsonStart = json_data.getJSONObject(i);
//            String lngStart = jsonStart.getString("lng");
//            String latStart = jsonStart.getString("lat");
//            double latstart = Double.valueOf(latStart);
//            double lngstart = Double.valueOf(lngStart);
//            LatLng pt1 = new LatLng(latstart, lngstart);
//            latLngPolygon.add(pt1);
//        }
//        //获取起点和终点以及计算中心点
//        JSONObject jsonStart = json_data.getJSONObject(0);
//        String lngStart = jsonStart.getString("lng");
//        String latStart = jsonStart.getString("lat");
//        double latstart = Double.valueOf(latStart);
//        double lngstart = Double.valueOf(lngStart);
//        JSONObject jsonEnd = json_data.getJSONObject(json_data.length() - 1);
//        String lngEnd = jsonEnd.getString("lng");
//        String latEnd = jsonEnd.getString("lat");
//        double latend = Double.valueOf(latEnd);
//        double lngend = Double.valueOf(lngEnd);
//
//        final double midlat = (latstart + latend) / 2;
//        final double midlon = (lngstart + lngend) / 2;
//        LatLng point = new LatLng(midlat, midlon);// 中点
//        LatLng point1 = new LatLng(latstart, lngstart);// 起点
//        LatLng point2 = new LatLng(latend, lngend);// 终点
//
//        //地图缩放等级
//        int zoomLevel[] = { 2000000, 1000000, 500000, 200000, 100000, 50000,
//                25000, 20000, 10000, 5000, 2000, 1000, 500, 100, 50, 20, 0 };
//        // 计算两点之间的距离，重新设定缩放值，让全部marker显示在屏幕中。
//        int jl = (int) DistanceUtil.getDistance(point1, point2);
//
//        int i;
//        for (i = 0; i < 17; i++) {
//            if (zoomLevel[i] < jl) {
//                break;
//            }
//        }
//        //根据起点和终点的坐标点计算出距离来对比缩放等级获取最佳的缩放值，用来得到最佳的显示折线图的缩放等级
//        float zoom = i + 2;
//        // 设置当前位置显示在地图中心
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, zoom);// 设置缩放比例
//        mBaiduMap.animateMapStatus(u);
//
//        /**
//         * 创建自定义overlay
//         */
//        // 起点位置
//        LatLng geoPoint = new LatLng(latstart, lngstart);
//        // 构建Marker图标
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_st);
//        // 构建MarkerOption，用于在地图上添加Marker
//        OverlayOptions option = new MarkerOptions().position(geoPoint)
//                .icon(bitmap).zIndex(8).draggable(true);
//
//        // 终点位置
//        LatLng geoPoint1 = new LatLng(latend, lngend);
//        // 构建Marker图标
//        BitmapDescriptor bitmap1 = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_en);
//        // 构建MarkerOption，用于在地图上添加Marker
//        OverlayOptions option1 = new MarkerOptions().position(geoPoint1)
//                .icon(bitmap1).zIndex(8).draggable(true);
//        // 在地图上添加Marker，并显示
//
//        List<OverlayOptions> overlay = new ArrayList<OverlayOptions>();
//        overlay.add(option);
//        overlay.add(option1);
//        mBaiduMap.addOverlays(overlay);
//
//        //开始绘制
//        drawMyRoute(latLngPolygon);
//    }
   /**
     * 根据数据绘制轨迹
     *
     * @param points2
     */
    protected void drawMyRoute(List<LatLng> points2) {
        OverlayOptions options = new PolylineOptions().color(0xAAFF0000)
                .width(10).points(points2);
        mBaiduMap.addOverlay(options);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mTextureMapView.onDestroy();
       // mBaiduMap.setMyLocationEnabled(false);
    }



}
