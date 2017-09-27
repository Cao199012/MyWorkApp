package com.caojian.myworkapp.modules.rail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.Point;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RailFragment extends Fragment {

    @BindView(R.id.img_location)
    ImageView mImg_location;
    private TextureMapView mapView;
    private BaiduMap mBaiduMap;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RailFragment newInstance(String param1, String param2) {
        RailFragment fragment = new RailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_rail,container,false);

        ButterKnife.bind(this,root);
        mapView = (TextureMapView) root.findViewById(R.id.map_rail);

        mBaiduMap = mapView.getMap();
        initMap();
        return root;
    }

    private void initMap()
    {
        mBaiduMap.setMyLocationEnabled(true);
        // map_rail


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
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
