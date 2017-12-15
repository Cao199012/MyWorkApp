package com.caojian.myworkapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.ui.adapter.ExpandableRecyclerviewAdapter;
import com.caojian.myworkapp.ui.adapter.FriendSelectRecyclerViewAdapter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseFragment;
import com.caojian.myworkapp.ui.contract.FriendContract;
import com.caojian.myworkapp.ui.contract.FriendGroupContract;
import com.caojian.myworkapp.ui.presenter.FriendPresenter;
import com.caojian.myworkapp.ui.presenter.GetGroupPresenter;
import com.caojian.myworkapp.ui.presenter.TrackSelectPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CJ on 2017/11/21.
 */

public class MainFriendSelectFragment extends MvpBaseFragment<FriendGroupContract.View,GetGroupPresenter>implements FriendGroupContract.View{

    public static MainFriendSelectFragment newInstance(){
        MainFriendSelectFragment fragment = new MainFriendSelectFragment();
        return fragment;
    }
    @BindView(R.id.list)
    RecyclerView recyclerView;
    List<FriendsAndGroupsMsg.DataBean.GroupsBean> mListdata = new ArrayList<>();
    ExpandableRecyclerviewAdapter mAdapter;
    GetGroupPresenter mPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler,container,false);
        ButterKnife.bind(this,root);
        initRecy();
        mPresenter.getFriendGroup();
        return root;
    }

    private void initRecy()
    {
        mAdapter = new ExpandableRecyclerviewAdapter(mListdata, (FriendSelectRecyclerViewAdapter.SelectListen) getActivity());
        // TODO: 2017/10/8 根据不同的访问页面显示不同的数据
        recyclerView.setAdapter(mAdapter);
        mAdapter.setmGroupSelect((ExpandableRecyclerviewAdapter.GroupSelect) getActivity());
    }


    @Override
    public void onSuccess(List<FriendsAndGroupsMsg.DataBean.GroupsBean> groups) {
        if(groups.isEmpty()) return;
        mListdata.clear();
        mListdata.addAll(groups);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onFailed(String errorMsg) {
        ((BaseTitleActivity) getActivity()).showToast(errorMsg,Toast.LENGTH_SHORT);
    }

    @Override
    protected GetGroupPresenter createPresenter() {
        mPresenter = new GetGroupPresenter((BaseTitleActivity) getActivity(),this);
        return mPresenter;
    }
}
