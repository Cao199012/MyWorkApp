package com.caojian.myworkapp.modules.track;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.TextureMapView;
import com.caojian.myworkapp.MainActivity;
import com.caojian.myworkapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@lin interface
 * to handle interaction events.
 * Use the {@link TrackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackFragment extends Fragment {




    public static TrackFragment newInstance() {
        TrackFragment fragment = new TrackFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    TextureMapView mTextureMapView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((MainActivity)getActivity()).showDialogFragment("暂无权限","此功能需要付费购买","暂不使用","去购买");
        View root = inflater.inflate(R.layout.fragment_track, container, false);

        mTextureMapView = (TextureMapView) root.findViewById(R.id.track_map);
        return root;
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        mTextureMapView.onDestroy();
       // mBaiduMap.setMyLocationEnabled(false);
    }



}
