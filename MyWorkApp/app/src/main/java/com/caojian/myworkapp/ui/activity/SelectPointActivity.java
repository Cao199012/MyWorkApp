package com.caojian.myworkapp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
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
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.caojian.myworkapp.until.ActivityUntil.myCheckPermission;

public class SelectPointActivity extends BaseTitleActivity {
    public static void go2SelectPointActivity(BaseTitleActivity fromClass,int requestCode,String points,int position)
    {
        Intent intent = new Intent(fromClass,SelectPointActivity .class);
        intent.putExtra("points",points);
        intent.putExtra("position",position);
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
    private int mPosition; //当前点的位置
   private Map mPoints;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_point);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("选择地点");
        mapView = (TextureMapView) findViewById(R.id.map_select);
        mPosition = getIntent().getIntExtra(("position"),-1);
         mPoints = new Gson().fromJson(getIntent().getStringExtra("points"),new TypeToken<Map>(){}.getType());
        mBaiduMap = mapView.getMap();
        String[] pers = myCheckPermission(SelectPointActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        if(pers.length > 0)
        {
            ActivityCompat.requestPermissions(SelectPointActivity.this,pers,1);
        }else
        {
            initRecy();
            initMap();
        }

    }

    private void initRecy() {
        adapter = new PointAdapter();
        mRecy_point.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecy_point.addItemDecoration(new LineDecoration(getBaseContext()));
        mRecy_point.setAdapter(adapter);
    }

    //初始化地图显示
    private void initMap()
    {
        LatLng ll = null;
        double _lat = 0;
        double _lot = 0;
        if(mPoints != null && mPoints.size() > 0){   //已经选择坐标点了
            for (int i = 0; i < 4;i++){
                if(mPoints.get(i+"") != null)
                {
                    Map poiInfo = (Map) (mPoints.get(i+""));
                    double lat = Double.parseDouble(poiInfo.get("latitude").toString());
                    double lot =Double.parseDouble(poiInfo.get("longitude").toString());
                    _lat+= lat;
                    _lot+= lot;
                    if(i != mPosition){

                        //将BitmapDescriptor设置marker的icon:
                        BitmapDescriptor bdOpen_iv=
                                BitmapDescriptorFactory.fromView(getView(i+""));
                        // BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.location);
                        OverlayOptions options = new MarkerOptions().position(new LatLng(lat,lot)).icon(bdOpen_iv);
                        // new MarkerOptions().icon(bdOpen_iv);
                        Marker marker = (Marker) mBaiduMap.addOverlay(options);
                    }
                }
            }
            ll = new LatLng(_lat/mPoints.size(),_lot/mPoints.size());

        }else {
            //获取定位的坐标
            MyApplication application = (MyApplication) getApplication();
            if(application.getRailPonit() != null)  //定位坐标不为空
            {
                ll = new LatLng(application.getRailPonit().latitude, application.getRailPonit().longitude);
            }
        }

        mBaiduMap.setMyLocationEnabled(true);  //t跳转到指定位置

        //定位到指定坐标
        if(ll != null) {
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll, 16f);
            mBaiduMap.setMapStatus(update);
        }



        mBaiduMap.setOnMapStatusChangeListener(new MyMapStatusChangeListener() {
            @Override
            void onChangeFinish() {
               // getData(); //拖拽完成定位坐标
            }
        });
    }

    //确定选择的点
    public void selectPoint(View view){
        android.graphics.Point point = new android.graphics.Point((int) mImg_location.getX(), (int) mImg_location.getY());
        // 将像素坐标转为地址坐标
        LatLng latLng = mapView.getMap().getProjection().fromScreenLocation(point);
        Intent intent = new Intent();
        intent.putExtra("latLng",latLng);
        setResult(RESULT_OK,intent);
        finish();
    }
    private void getData() {
        android.graphics.Point point = new android.graphics.Point((int) mImg_location.getX(), (int) mImg_location.getY());
        // 将像素坐标转为地址坐标
        LatLng latLng = mapView.getMap().getProjection().fromScreenLocation(point);

//        Log.i("marker",latLng.toString());
//        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }
    View view;
    private View getView(String str) {
        // View textView = View.inflate(SelectPointActivity.this,R.layout.sample_my_view,null);
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(SelectPointActivity.this,
                    R.layout.sample_my_view, null);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView)
                    view.findViewById(R.id.head_img);
            viewHolder.tv = (TextView)
                    view.findViewById(R.id.mark_content_tv);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //Glide.with(SelectPointActivity.this).load(R.mipmap.logo_launcher).into(viewHolder.iv);
        viewHolder.tv.setText(str);
        return view;
    }
    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }
    private static class ViewHolder {
        public TextView tv;
        public ImageView iv;
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

     //确定选择的点 返回
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1)
        {
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                initRecy();
                initMap();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mapView.onDestroy();
    }
}
