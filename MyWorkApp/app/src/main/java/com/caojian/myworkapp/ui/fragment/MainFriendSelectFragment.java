package com.caojian.myworkapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.ui.adapter.ExpandableMainFriendsAdapter;
import com.caojian.myworkapp.ui.adapter.FriendSelectRecyclerViewAdapter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseFragment;
import com.caojian.myworkapp.ui.contract.FriendGroupContract;
import com.caojian.myworkapp.ui.presenter.GetGroupPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CJ on 2017/11/21.
 */

public class MainFriendSelectFragment extends MvpBaseFragment<FriendGroupContract.View,GetGroupPresenter>implements FriendGroupContract.View, ExpandableMainFriendsAdapter.ItemSelect {

    public static MainFriendSelectFragment newInstance(){
        MainFriendSelectFragment fragment = new MainFriendSelectFragment();
        return fragment;
    }
    @BindView(R.id.list)
    ExpandableListView mExpandableListView;
    List<FriendsAndGroupsMsg.DataBean.GroupsBean> mListData = new ArrayList<>();
    ExpandableMainFriendsAdapter mAdapter;
    GetGroupPresenter mPresenter;
    ChangeSelectFriends mChangeSelectFriends;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler,container,false);
        ButterKnife.bind(this,root);

        mPresenter.getFriendGroup();
        return root;
    }

    private void initRecy()
    {
        mAdapter = new ExpandableMainFriendsAdapter(mListData);

        mExpandableListView.setAdapter(mAdapter);
        mAdapter.setGroupSelect(this);
        for(int i = 0 ; i < mListData.size();i++) {
            mExpandableListView.expandGroup(i);
        }


    }


    @Override
    public void onSuccess(List<FriendsAndGroupsMsg.DataBean.GroupsBean> groups) {
        if(groups.isEmpty()) return;
        mListData.clear();
        mListData.addAll(groups);
        initRecy();
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

    @Override
    public boolean groupSelect(int groupPosition, boolean isChecked) {
        if(isChecked == true) //组选中 则全选组成员
        {
            for (FriendDetailInfo.DataBean dataBean : mListData.get(groupPosition).getFriends())
            {
                dataBean.setIsCheckBeforeExit(1);
                mChangeSelectFriends.addFriend(dataBean.getFriendPhoneNo());
            }
            mListData.get(groupPosition).setIsCheckBeforeExit(1);
        }else  //取消组 则全部取消组成员
        {
            for (FriendDetailInfo.DataBean dataBean : mListData.get(groupPosition).getFriends())
            {
                dataBean.setIsCheckBeforeExit(2);
                mChangeSelectFriends.removeFriend(dataBean.getFriendPhoneNo());
            }
            mListData.get(groupPosition).setIsCheckBeforeExit(2);
        }


        mExpandableListView.collapseGroup(groupPosition);
        mExpandableListView.expandGroup(groupPosition);
        return false;
    }

    @Override
    public boolean childSelect(int groupPosition, int childPosition, boolean isChecked) {
        if(isChecked == true)  //好友选择 全部
        {
            mListData.get(groupPosition).getFriends().get(childPosition).setIsCheckBeforeExit(1);
            mChangeSelectFriends.addFriend(mListData.get(groupPosition).getFriends().get(childPosition).getFriendPhoneNo());
            if(groupPosition + 1 >= mListData.size()){
            }else {
                for (FriendDetailInfo.DataBean dataBean : mListData.get(groupPosition).getFriends())
                {
                    if(dataBean.getIsCheckBeforeExit() == 2){
                        return false;
                    }
                }
                mListData.get(groupPosition).setIsCheckBeforeExit(1);
                mAdapter.notifyDataSetChanged();
            }
        }else
        {
            mChangeSelectFriends.removeFriend(mListData.get(groupPosition).getFriends().get(childPosition).getFriendPhoneNo());
            mListData.get(groupPosition).getFriends().get(childPosition).setIsCheckBeforeExit(2);
            if(groupPosition + 1 >= mListData.size()){

            }else {
                if(  mListData.get(groupPosition).getIsCheckBeforeExit() == 1){
                    mListData.get(groupPosition).setIsCheckBeforeExit(2);
                    mAdapter.notifyDataSetChanged();
                }
            }

        }
        return false;
    }

    public void setmChangeSelectFriends(ChangeSelectFriends mChangeSelectFriends) {
        this.mChangeSelectFriends = mChangeSelectFriends;
    }

    public interface ChangeSelectFriends{
        void addFriend(String friendId);
        void removeFriend(String friendId);
    }
}
