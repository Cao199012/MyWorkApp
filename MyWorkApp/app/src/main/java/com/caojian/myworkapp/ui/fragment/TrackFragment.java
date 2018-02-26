package com.caojian.myworkapp.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.trace.api.track.DistanceResponse;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TransportMode;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.model.response.RailHistoryMsg;
import com.caojian.myworkapp.services.DownLoadService;
import com.caojian.myworkapp.ui.activity.BuyVipActivity;
import com.caojian.myworkapp.ui.activity.FriendSelectActivity;
import com.caojian.myworkapp.ui.activity.MainActivity;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.activity.RailSelectActivity;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseFragment;
import com.caojian.myworkapp.ui.contract.RailSelectContract;
import com.caojian.myworkapp.ui.presenter.RailPresenter;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Constants;
import com.caojian.myworkapp.until.MapUtil;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.MyDialogFragment;
import com.caojian.myworkapp.widget.MyNewDialogFragment;
import com.caojian.myworkapp.widget.PersonalInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/*
*
* 轨迹回放
* */
public class TrackFragment extends MvpBaseFragment<RailSelectContract.View,RailPresenter> implements  RailSelectContract.View{

    public static TrackFragment newInstance() {
        TrackFragment fragment = new TrackFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    TextureMapView mTextureMapView;
    BaiduMap mBaiduMap;
    MyDialogFragment mDialogFragment;
    RailPresenter mPresenter;
    private MyApplication trackApp = null;

    /**
     * 查询轨迹的开始时间
     */
    private long startTime = 0;

    /**
     * 查询轨迹的结束时间
     */
    private long endTime = 0;

    /**
     * 轨迹点集合
     */
    private List<LatLng> trackPoints = new ArrayList<>();
    /**
     * 历史轨迹请求
     */
    private HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest();
    /**
     * 轨迹监听器（用于接收历史轨迹回调）
     */
    private OnTrackListener mTrackListener = null;

    /**
     * 轨迹排序规则
     */
    private SortType sortType = SortType.asc;

    private int pageIndex = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    private View mRootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_track, container, false);
        ButterKnife.bind(this,mRootView);
        mTextureMapView = (TextureMapView) mRootView.findViewById(R.id.track_map);
        mBaiduMap = mTextureMapView.getMap();
        trackApp = (MyApplication) getActivity().getApplication();
        mDialogFragment = MyDialogFragment.newInstance("暂无权限", Until.DILOG_MSG_TRACK,"暂不使用","立即开通");
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
       if (!checkType() && getActivity() != null){
         //  mTextureMapView.setVisibility(View.INVISIBLE);

            mDialogFragment.setCancelable(false);
           ActivityUntil.showDialogFragment(getActivity().getSupportFragmentManager(),mDialogFragment,"buy");
       }
      initListen();
       // queryHistoryTrack();

        return mRootView;
    }
    /**
     * 把页面分享到微信或朋友圈
     * */
    @OnClick(R.id.share_image)
    public void shareImage(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, view2BitmapUri(mTextureMapView));
        getActivity().startActivity(intent);
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
              //  mDialogFragment.show(getActivity().getSupportFragmentManager(),"buy");
                ActivityUntil.showDialogFragment(getActivity().getSupportFragmentManager(),mDialogFragment,"buy");
                return true;
            }
            if(mTextureMapView.getVisibility() == View.INVISIBLE){
                mTextureMapView.setVisibility(View.VISIBLE);
            }
            RailSelectActivity.go2RailSelectActivity((BaseTitleActivity) getActivity(),RailSelectActivity.FROM_TRACK);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkType(){
        PersonalMsg personalMsg =  PersonalInstance.getInstance().getPersonalMsg();
        if( personalMsg != null && personalMsg.getData() != null){
            if(personalMsg.getData().getTrajectoryService().equals("2")){
                return false;
            }
        }
        return true;
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
    List<LatLng> latLngPolygon= new ArrayList<LatLng>();
    public void drawStart( List<LatLng> latLngs) {
        mBaiduMap.clear();
        //向latLngPolygon中添加获取到的所有坐标点
        //获取起点和终点以及计算中心点
        if(latLngs.isEmpty() || latLngs.size() < 2) return;
        int end = ((latLngs.size()-1) > 0)?latLngs.size()-1:0;
        final double midlat = (latLngs.get(0).latitude + latLngs.get(end).latitude) / 2;
        final double midlon = (latLngs.get(0).longitude + latLngs.get(end).longitude) / 2;
        LatLng point = new LatLng(midlat, midlon);// 中点
        LatLng point1 = latLngs.get(0);// 起点
        LatLng point2 = latLngs.get(end);// 终点

        //地图缩放等级
        int zoomLevel[] = { 2000000, 1000000, 500000, 200000, 100000, 50000,
                25000, 20000, 10000, 5000, 2000, 1000, 500, 100, 50, 20, 0 };
        // 计算两点之间的距离，重新设定缩放值，让全部marker显示在屏幕中。
        int jl = (int) DistanceUtil.getDistance(point1, point2);

        int i;
        for (i = 0; i < 17; i++) {
            if (zoomLevel[i] < jl) {
                break;
            }
        }
        //根据起点和终点的坐标点计算出距离来对比缩放等级获取最佳的缩放值，用来得到最佳的显示折线图的缩放等级
        float zoom = i + 2;
        // 设置当前位置显示在地图中心
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, zoom);// 设置缩放比例
        mBaiduMap.animateMapStatus(u);

        /**
         * 创建自定义overlay
         */
        // 起点位置
        LatLng geoPoint = latLngs.get(0);
        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_start);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(geoPoint)
                .icon(bitmap).zIndex(8).draggable(true);

        // 终点位置
        LatLng geoPoint1 = latLngs.get(end);
        // 构建Marker图标
        BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_end);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option1 = new MarkerOptions().position(geoPoint1)
                .icon(bitmap1).zIndex(8).draggable(true);
//        // 在地图上添加终点Marker，并显示
        List<OverlayOptions> overlay = new ArrayList<OverlayOptions>();
        overlay.add(option);
        overlay.add(option1);
        mBaiduMap.addOverlays(overlay);
        //开始绘制
        drawMyRoute(latLngPolygon);
    }
   /**
     * 根据数据绘制轨迹
     *
     * @param points2
     */
    protected void drawMyRoute(List<LatLng> points2) {

        if(points2 == null || points2.isEmpty()){
            ((BaseTitleActivity)getActivity()).showToast("在此期间没有使用定位APP", Toast.LENGTH_SHORT);
        }
        // 绘制轨迹底图
        PolylineOptions polylineOptionBg = new PolylineOptions();
        polylineOptionBg.width(6); // 折线线宽， 默认为 5， 单位：像素
        polylineOptionBg.dottedLine(true); // 折线是否虚线
        polylineOptionBg.color(0xFF009966); // 折线颜色
        polylineOptionBg.points(points2); // 折线坐标点列表:[2,10000]，且不能包含null
        polylineOptionBg.keepScale(true); // 纹理宽、高是否保持原比例渲染
        OverlayOptions ooPolyline = polylineOptionBg;
        mBaiduMap.addOverlay(ooPolyline);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mTextureMapView.onDestroy();
       // mBaiduMap.setMyLocationEnabled(false);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RailSelectActivity.FROM_TRACK)
        {
            if(resultCode == getActivity().RESULT_OK)
            {
                String friend = data.getStringExtra("friend");
                try {
                    endTime = ActivityUntil.parseStringToDate(data.getStringExtra("endTime")+" 23:59","yyyy-MM-dd hh:mm").getTime()/1000;
                    startTime = ActivityUntil.parseStringToDate(data.getStringExtra("startTime")+" 00:00","yyyy-MM-dd hh:mm").getTime()/1000;
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                data.getStringExtra("endTime");
//                mPresenter.getRailHistory(friend,start+" 00:00",end+" 23:59");
                initQueryOpation(data);
            }
        }
            super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected RailPresenter createPresenter() {
        mPresenter = new RailPresenter((BaseTitleActivity) getActivity(),this);
        return mPresenter;
    }


    List<LatLng> latLngs = new LinkedList<>();
    @Override
    public void getSuccess(List<RailHistoryMsg.PositionsBean> positions) {
        latLngs.clear(); //清除上次的坐标点

        for (RailHistoryMsg.PositionsBean position : positions){
            LatLng latLng = new LatLng(Double.parseDouble(position.getLatitude()),Double.parseDouble(position.getLongitude()));
            latLngs.add(latLng);
        }
        latLngPolygon = latLngs;
        drawStart(latLngs);

    }

    @Override
    public void getError(String msg) {

    }

    private void initQueryOpation(Intent data){
        if (null == data) {
            return;
        }

        trackPoints.clear();
        pageIndex = 1;

//
        if(startTime == 0 || endTime == 0) {
            startTime = Until.getCurrentTime() - 12 * 60 * 60;
            endTime = Until.getCurrentTime();
        }
        ProcessOption processOption = new ProcessOption();
        if (data.hasExtra("radius") ) {
            processOption.setRadiusThreshold(data.getIntExtra("radius", Constants.DEFAULT_RADIUS_THRESHOLD));
        }
        processOption.setRadiusThreshold(Constants.DEFAULT_RADIUS_THRESHOLD);
        if (data.hasExtra("transportMode")) {
            processOption.setTransportMode(TransportMode.driving);
        }
        processOption.setTransportMode(TransportMode.driving);
        if (data.hasExtra("denoise")) { //去噪
            processOption.setNeedDenoise(data.getBooleanExtra("denoise", true));
        }
       // processOption.setNeedDenoise(true);
        if (data.hasExtra("vacuate")) {  //抽希
            processOption.setNeedVacuate(data.getBooleanExtra("vacuate", true));
        }
       // processOption.setNeedVacuate(true);
        if (data.hasExtra("mapmatch")) { //绑路
            processOption.setNeedMapMatch(data.getBooleanExtra("mapmatch", true));
        }
       // processOption.setNeedMapMatch(true);
        historyTrackRequest.setProcessOption(processOption);

        if (data.hasExtra("supplementMode")) {
            historyTrackRequest.setSupplementMode(SupplementMode.valueOf(data.getStringExtra("supplementMode")));
        }
        if (data.hasExtra("sortType")) {
            sortType = SortType.valueOf(data.getStringExtra("sortType"));
            historyTrackRequest.setSortType(sortType);
        }
        historyTrackRequest.setSortType(sortType);
        if (data.hasExtra("coordTypeOutput")) {
            historyTrackRequest.setCoordTypeOutput(CoordType.valueOf(data.getStringExtra("coordTypeOutput")));
        }
        if (data.hasExtra("processed")) {
            historyTrackRequest.setProcessed(data.getBooleanExtra("processed", true));
        }
      //  historyTrackRequest.setProcessed(true);
        //请求轨迹
        ((BaseTitleActivity)getActivity()).showProgress(getContext());
        queryHistoryTrack(data.getStringExtra("friend"));
    }
    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack(String entityName) {
        trackApp.initRequest(historyTrackRequest);
        historyTrackRequest.setServiceId(trackApp.serviceId);
        historyTrackRequest.setEntityName(entityName);
        historyTrackRequest.setStartTime(startTime);
        historyTrackRequest.setEndTime(endTime);
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(Constants.PAGE_SIZE);
        //查询历史轨迹
        trackApp.mClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
    }

    private void initListen(){
        mTrackListener = new OnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                int total = response.getTotal();
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    ((BaseTitleActivity)getActivity()).showToast( response.getMessage(),Toast.LENGTH_SHORT);
                } else if (0 == total) {
                    ((BaseTitleActivity)getActivity()).showToast("暂无坐标点",Toast.LENGTH_SHORT);
                } else {
                    List<TrackPoint> points = response.getTrackPoints();
                    if (null != points) {
                        for (TrackPoint trackPoint : points) {
                            if (!MapUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                    trackPoint.getLocation().getLongitude())) {
                                trackPoints.add(MapUtil.convertTrace2Map(trackPoint.getLocation()));
                            }
                        }
                    }
                }

                if (total > Constants.PAGE_SIZE * pageIndex) {
                    historyTrackRequest.setPageIndex(++pageIndex);
                    trackApp.mClient.queryHistoryTrack(historyTrackRequest, mTrackListener); //反复获取
                } else {
                    latLngPolygon = trackPoints;
                    ((BaseTitleActivity)getActivity()).hideProgress();
                    drawStart(trackPoints);
                }
            }

            @Override
            public void onDistanceCallback(DistanceResponse response) {
                super.onDistanceCallback(response);
            }

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                super.onLatestPointCallback(response);
            }
        };
    }

    /**
     *view生成图片文件，转化成呢分享地址
     * */
    private Uri view2BitmapUri(View ll){
        Bitmap bitmap  = loadBitmapFromView(ll);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(),"wifi/share.jpg");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        try {
            OutputStream outPutStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,90,outPutStream);
            outPutStream.flush();
            outPutStream.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri = FileProvider.getUriForFile(getContext(), "com.caojian.myworkapp.fileProvider", file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                return apkUri;
            } else {
                return Uri.fromFile(file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
    }
    /**
     *view生成图片（bitmap）
     * */
    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明  */
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }
}
