package com.caojian.myworkapp.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.ui.activity.FriendDetailActivity;
import com.caojian.myworkapp.ui.activity.FriendGroupActivity;
import com.caojian.myworkapp.ui.adapter.FriendListAdapter;
import com.caojian.myworkapp.model.data.FriendItem;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseFragment;
import com.caojian.myworkapp.ui.contract.FriendContract;
import com.caojian.myworkapp.ui.presenter.FriendPresenter;
import com.caojian.myworkapp.ui.view.SideBar;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.SectionDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.caojian.myworkapp.ui.activity.FriendDetailActivity.go2FriendDetailActivity;
import static com.caojian.myworkapp.ui.activity.FriendAPPlyRecordActivity.go2FriendMessageActivity;
import static com.caojian.myworkapp.ui.activity.SearchByContactActivity.go2SearchByContactActivity;
import static com.caojian.myworkapp.ui.activity.SearchByPhoneActivity.go2SearchByPhoneActivity;

public class FriendFragment extends MvpBaseFragment<FriendContract.View,FriendPresenter> implements FriendListAdapter.ItemClick ,FriendContract.View{

    @BindView(R.id.recy_friend)
    RecyclerView mRecy_friend;
    @BindView(R.id.sidebar_friend)
    SideBar sideBar;
    @BindView(R.id.side_dialog)
    TextView mSide_dialog;
    private FriendListAdapter listAdapter;
    private List<FriendDetailInfo.DataBean> mListData = new ArrayList<>();
    private LinearLayoutManager manager;
    Unbinder unbinder;
   //mvp
    FriendPresenter mPresenter;
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//创建Menu必须设置
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friend, container, false);
        unbinder = ButterKnife.bind(this,root);
        initRecy();
        initSideVar();
        //请求好友数据
        mPresenter.getFriends();
//        //接收广播 更新列表
//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if(mPresenter != null) {
//                    mPresenter.getFriends();
//                }
//            }
//        };
//        getActivity().registerReceiver(receiver,new IntentFilter(Until.ACTION_FRIEND));
        return root;
    }

    private void initRecy() {
//        for (int i = 0; i < 5 ;i++){
//            FriendItem.DataBean.FriendsBean data = new FriendItem.DataBean.FriendsBean();
//            data.setFriendPhoneNo("qqqqq");
//            data.setHeadPic("");
//            data.setRemarkFirstLetter("A");
//            mListData.add(data);
//        }
        listAdapter = new FriendListAdapter(mListData,this,getActivity());
        //RecyclerView设置manager
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecy_friend.setLayoutManager(manager);
        mRecy_friend.addItemDecoration(new SectionDecoration(getActivity()) {
            @Override
            public String getGroupId(int position) {

                return mListData.get(position).getRemarkFirstLetter();
            }
        });
        mRecy_friend.setAdapter(listAdapter);
    }
    //初始化 侧边字母栏 点击跳转
    private void initSideVar() {
        sideBar.setDialogView(mSide_dialog);
        sideBar.setListen(new SideBar.SelectPosition() {
            @Override
            public void set2num(String num) {
                //点击字母，指定字母第一个item滑到头部
                int i = 0;
                for (;i < mListData.size();i++)
                {
                    if (mListData.get(i).getRemarkFirstLetter().equals(num)){
                        break;
                    }
                }
                manager.scrollToPositionWithOffset(i,0);
            }
        });
    }


    //跳转好友请信息求页面
    @OnClick(R.id.new_friend)
    public void goToMessage()
    {
        go2FriendMessageActivity(getActivity());
    }

    //跳转到好友群界面
    @OnClick(R.id.friend_group)
    public void go2Group()
    {
        FriendGroupActivity.go2FriendGroupActivity(getActivity());
    }


    //在头部添加 添加按钮
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.title_add,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.title_add)
        {
            PopUpMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void PopUpMenu() {
        PopupMenu popuMenu = new PopupMenu(getContext(),getActivity().findViewById(R.id.title_add));
        popuMenu.inflate(R.menu.friend_search);
        popuMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.search_phone:
                        // TODO: 2017/9/3 跳转手机号搜索页面
                        go2SearchByPhoneActivity(getActivity());
                        break;
                    case R.id.search_commect:
                        // TODO: 2017/9/3 跳转联系人添加
                        go2SearchByContactActivity(getActivity());
                        break;
                }
                return false;
            }
        });
        popuMenu.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected FriendPresenter createPresenter() {
        mPresenter = new FriendPresenter(this, (BaseTitleActivity) getActivity());
        return mPresenter;
    }

    //单击好友列表的好友跳转到详情
    @Override
    public void itemSelect(FriendDetailInfo.DataBean item) {
        // TODO: 2017/9/2 跳转到好友详情页面
        Gson gson = new Gson();
        String databean =  gson.toJson(item);
        go2FriendDetailActivity((BaseTitleActivity) getActivity(),databean, FriendDetailActivity.REQUESTCODE);
    }

    //接受Activity返回结果判断是否更新好友
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FriendDetailActivity.REQUESTCODE)
        {
            if(requestCode == getActivity().RESULT_OK)
            {
                mPresenter.getFriends();
            }
        }
    }

    //网络请求好友列表结果
    @Override
    public void onSuccess(List<FriendDetailInfo.DataBean> friends) {
        mListData.clear();
        mListData.addAll(friends);
        listAdapter.notifyDataSetChanged();
        ((MyApplication)getActivity().getApplication()).setFriendList(friends);
    }
    @Override
    public void onFailed(String errorMsg) {
        ((BaseTitleActivity)getActivity()).showToast(errorMsg, Toast.LENGTH_SHORT);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
