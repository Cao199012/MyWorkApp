package com.caojian.myworkapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.ui.adapter.FriendSelectRecyclerViewAdapter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseFragment;
import com.caojian.myworkapp.ui.contract.FriendContract;
import com.caojian.myworkapp.ui.presenter.TrackSelectPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CJ on 2017/11/21.
 */

public class TrackFriendSelectFragment extends MvpBaseFragment<FriendContract.View,TrackSelectPresenter> implements  FriendContract.View{

    public TrackFriendSelectFragment newInstance(){
        TrackFriendSelectFragment fragment = new TrackFriendSelectFragment();
        return fragment;
    }
    @BindView(R.id.list)
    RecyclerView recyclerView;
    TrackSelectPresenter mPresenter;
    List<FriendDetailInfo.DataBean> mListData = new ArrayList<>();
    FriendSelectRecyclerViewAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_friend_select,container,false);
        ButterKnife.bind(this,root);
        initRecy();
        return root;
    }

    private void initRecy()
    {
        if(mListData == null){
            mListData = new ArrayList<>();
        }
        mAdapter = new FriendSelectRecyclerViewAdapter(mListData, (FriendSelectRecyclerViewAdapter.SelectListen) getActivity(),3);
        // TODO: 2017/10/8 根据不同的访问页面显示不同的数据
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(List<FriendDetailInfo.DataBean> friends) {
        mListData.clear();
        mListData.addAll(friends);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String errorMsg) {
        ((BaseTitleActivity) getActivity()).showToast(errorMsg, Toast.LENGTH_SHORT);
    }



    @Override
    protected TrackSelectPresenter createPresenter() {
        mPresenter = new TrackSelectPresenter(this, (BaseTitleActivity) getActivity());
        return  mPresenter;
    }
}
