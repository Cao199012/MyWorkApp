package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.ui.adapter.ExpandableRecyclerviewAdapter;
import com.caojian.myworkapp.ui.adapter.FriendSelectRecyclerViewAdapter;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.FriendContract;
import com.caojian.myworkapp.ui.fragment.MainFriendSelectFragment;
import com.caojian.myworkapp.ui.presenter.FriendPresenter;
import com.caojian.myworkapp.widget.SectionDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendSelectActivity extends MvpBaseActivity<FriendContract.View,FriendPresenter> implements FriendContract.View
        , FriendSelectRecyclerViewAdapter.SelectListen,ExpandableRecyclerviewAdapter.GroupSelect {

    public static void go2FriendSelectActivity(Context from,int requestCode,String title){
        Intent intent = new Intent(from,FriendSelectActivity.class);
        intent.putExtra("requestCode",requestCode);
        intent.putExtra("title",title);
        ((Activity)from).startActivityForResult(intent,requestCode);
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_container)
    FrameLayout mContainer;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    FriendPresenter mPresenter;
    List<FriendDetailInfo.DataBean> mListdata = new ArrayList<>();
    FriendSelectRecyclerViewAdapter mAdapter;
    private Unbinder unbinder;
    public static final int SELECT_MAIN = 100;
    public static final int SELECT_ADD = 101;
    public static final int SELECT_DELETE = 102;
    public static final int SELECT_RAIL = 103;
    public static final int SELECT_TRACK = 104;
    public int selectKind = SELECT_MAIN;//0首页选择显示好友 1群组添加或删除好友 2选择监视好友 3查看好友轨迹
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_select);
        unbinder = ButterKnife.bind(this);
        toolbar.setTitle("选择好友");
       // ActivityUntil.initActionBar(toolbar,FriendSelectActivity.this,R.drawable.ic_arrow_back);
        selectKind = getIntent().getIntExtra("requestCode", SELECT_MAIN);
        if(selectKind == SELECT_MAIN){
            mContainer.removeView(mRecyclerView);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,MainFriendSelectFragment.newInstance()).commit();
        }else {
            initRecy();
        }
//        if(selectKind != SELECT_DELETE){
//            mPresenter.getFriends();
//        }
    }

    private void initRecy()
    {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(FriendSelectActivity.this));
        MyApplication application = (MyApplication) getApplication();
        if(selectKind == SELECT_DELETE){
            mListdata = application.getGroupFriends();
        }else {
            mListdata = application.getFriendList();
        }
        if(mListdata == null){
            mListdata = new ArrayList<>();
        }
        mAdapter = new FriendSelectRecyclerViewAdapter(mListdata,this,2);
        // TODO: 2017/10/8 根据不同的访问页面显示不同的数据
        mRecyclerView.setAdapter(mAdapter);
        if(selectKind == SELECT_MAIN){
            mRecyclerView.addItemDecoration(new SectionDecoration(FriendSelectActivity.this) {
                @Override
                public String getGroupId(int position) {
                    return mListdata.get(position).getRemarkFirstLetter();
                }
            });
        }
    }
    @Override
    public FriendPresenter createPresenter() {
        mPresenter = new FriendPresenter(this,FriendSelectActivity.this);
        return mPresenter;
    }

    //确定选择完成返回添加
    public void selectDone(View v)
    {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("location", (ArrayList<String>) mFriendSelectList);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onSuccess(List<FriendDetailInfo.DataBean> friends) {
//        mListdata.clear();
//        mListdata.addAll(friends);
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String errorMsg) {
        showToast(errorMsg, Toast.LENGTH_SHORT);
    }

    @Override
    public void onBackPressed() {
        finish();
        setResult(RESULT_CANCELED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    List<String> mFriendSelectList = new ArrayList<String>();
    List<String> mGroupSelectList = new ArrayList<String>();
    @Override
    public boolean checkSelect(String friendId) {

        if(mGroupSelectList.isEmpty()){
            if(mFriendSelectList.contains(friendId)){
                mFriendSelectList.remove(friendId);
            }else {
                mFriendSelectList.add(friendId);
            }
            return true;
        }else{
            showToast("好友和群组不能同时选择",Toast.LENGTH_SHORT);
            return  false;
        }
    }
    @Override
    public boolean groupSelect(FriendsAndGroupsMsg.DataBean.GroupsBean bean) {

        if(mFriendSelectList.isEmpty()){
            if(mGroupSelectList.contains(bean.getGroupId())){
                mGroupSelectList.remove(bean.getGroupId());
            }else {
                mGroupSelectList.add(bean.getGroupId());
            }
            return true;
        }else{
            showToast("好友和群组不能同时选择",Toast.LENGTH_SHORT);
            return  false;
        }
    }
}
