package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;

import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.widget.LineDecoration;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SelectPointActivity extends BaseTitleActivity {
    public static void go2SelectPointActivity(BaseTitleActivity fromClass,int requestCode)
    {
        Intent intent = new Intent(fromClass,SelectPointActivity .class);
        fromClass.startActivityForResult(intent,requestCode);
    }
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.img_location)
    ImageView mImg_location;
    @BindView(R.id.recy_point)
    RecyclerView mRecy_point;
    private TextureMapView mapView;
    private BaiduMap mBaiduMap;
    PointAdapter adapter;
    GeoCoder geoCoder;
    //根据坐标反编译地址
    List<PoiInfo> poiInfos = new ArrayList<>();
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_point);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("选择地点");
        mapView = (TextureMapView) findViewById(R.id.map_select);
        mBaiduMap = mapView.getMap();
        initRecy();
        initMap();
    }

    private void initRecy() {
        adapter = new PointAdapter();
        mRecy_point.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecy_point.addItemDecoration(new LineDecoration(getBaseContext()));
        mRecy_point.setAdapter(adapter);
    }

    private void initMap()
    {
        mBaiduMap.setMyLocationEnabled(true);
        MyApplication application = (MyApplication) getApplication();
        LatLng ll = new LatLng(application.getBdLocation().getLatitude(),application.getBdLocation().getLongitude());
        //定位到指定坐标
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll,14f);
        mBaiduMap.setMapStatus(update);
        //获取定位的坐标
        //BDLocation bdLocation = ((MyApplication)getActivity().getApplication()).getBdLocation();
        mBaiduMap.setOnMapStatusChangeListener(new MyMapStatusChangeListener() {
            @Override
            void onChangeFinish() {
                getData(); //拖拽完成定位坐标
            }
        });

        geoCoder = GeoCoder.newInstance();
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    Toast.makeText(SelectPointActivity.this, "抱歉，未能找到结果",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                poiInfos = (ArrayList<PoiInfo>) result.getPoiList();
                adapter.notifyDataSetChanged();
            }
            // 地理编码查询结果回调函数
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                }
            }
        });
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
    }

    private void getData() {
        android.graphics.Point point = new android.graphics.Point((int) mImg_location.getX(), (int) mImg_location.getY());
        // 将像素坐标转为地址坐标
        LatLng latLng = mapView.getMap().getProjection().fromScreenLocation(point);
        Log.i("marker",latLng.toString());
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }
    //point 选择list的
    class PointAdapter extends RecyclerView.Adapter<PointAdapter.ViewHold>{

        @Override
        public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.point_msg_item,parent,false);
            return new ViewHold(item);
        }

        @Override
        public void onBindViewHolder(ViewHold holder, int position) {
            PoiInfo poiInfo = poiInfos.get(position);
            holder.mTv_name.setText(poiInfo.name);
            holder.mTv_address.setText(poiInfo.address);
            holder.mBtn_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2017/9/26 xuan dian
                    choseItem(poiInfo);
                }
            });
        }

        @Override
        public int getItemCount() {
            return poiInfos.size();
        }

        class ViewHold extends RecyclerView.ViewHolder{
            @BindView(R.id.point_name)
            TextView mTv_name;
            @BindView(R.id.point_address)
            TextView mTv_address;
            @BindView(R.id.btn_select)
            TextView mBtn_select;
            public ViewHold(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }

    private void choseItem(PoiInfo poiInfo) {
        String result = new Gson().toJson(poiInfo);
        Intent intent = new Intent();
        intent.putExtra("poiInfo",result);
        setResult(RESULT_OK,intent);
        finish();
    }

    abstract class MyMapStatusChangeListener implements BaiduMap.OnMapStatusChangeListener{

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
            onChangeFinish();
        }
        abstract void onChangeFinish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mapView.onDestroy();
    }
}
