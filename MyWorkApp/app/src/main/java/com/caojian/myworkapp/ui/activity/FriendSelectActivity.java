package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.ui.adapter.FriendSelectRecyclerViewAdapter;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.FriendContract;
import com.caojian.myworkapp.ui.fragment.MainFriendSelectFragment;
import com.caojian.myworkapp.ui.presenter.FriendPresenter;
import com.caojian.myworkapp.widget.LineDecoration;
import com.caojian.myworkapp.widget.PersonalInstance;
import com.caojian.myworkapp.widget.SectionDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendSelectActivity extends MvpBaseActivity<FriendContract.View,FriendPresenter> implements FriendContract.View
        , FriendSelectRecyclerViewAdapter.SelectListen, MainFriendSelectFragment.ChangeSelectFriends {

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
    List<FriendDetailInfo.DataBean> mListData = new ArrayList<>();
    FriendSelectRecyclerViewAdapter mAdapter;
    MainFriendSelectFragment mainFriendSelectFragment;

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
            mainFriendSelectFragment = MainFriendSelectFragment.newInstance();

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,mainFriendSelectFragment).commit();
            mainFriendSelectFragment.setmChangeSelectFriends(this);
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
        mRecyclerView.addItemDecoration(new LineDecoration(FriendSelectActivity.this));
        MyApplication application = (MyApplication) getApplication();

        if(application.getFriendList() == null || application.getFriendList().isEmpty()){
            mPresenter.getFriends();
        }else {
            initListData();
        }
//        if(mListData == null){
//            mPresenter.getFriends();
//        }else{  //去除已是组成员的号码
//
//        }
        mAdapter = new FriendSelectRecyclerViewAdapter(mListData,this,3);
        // TODO: 2017/10/8 根据不同的访问页面显示不同的数据
        mRecyclerView.setAdapter(mAdapter);
        if(selectKind == SELECT_MAIN){
            mRecyclerView.addItemDecoration(new SectionDecoration(FriendSelectActivity.this) {
                @Override
                public String getGroupId(int position) {
                    return mListData.get(position).getRemarkFirstLetter();
                }
            });
        }
    }
    /**
     * 初始化 list data
     * */
    private void initListData(){
        MyApplication application = (MyApplication) getApplication();
        mListData.clear();
        if(selectKind == SELECT_DELETE){
            mListData.addAll(application.getGroupFriends());
            // mListData = application.getGroupFriends();
        }else {
            if(selectKind == SELECT_TRACK){  //查看轨迹 添加自己
                FriendDetailInfo.DataBean dataBean = new FriendDetailInfo.DataBean();
                PersonalMsg personalMsg = PersonalInstance.getInstance().getPersonalMsg();
                dataBean.setFriendPhoneNo(personalMsg.getData().getPhoneNo());
                dataBean.setFriendRemarkName("本人");
                dataBean.setHeadPic(personalMsg.getData().getHeadPic());
                mListData.add(dataBean);
            }

            mListData.addAll(application.getFriendList());
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
       initListData();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String errorMsg) {
        showToast(errorMsg, Toast.LENGTH_SHORT);
    }


    List<String> mFriendSelectList = new ArrayList<String>();
    /**
     *
     * 好友勾选  加入list
     * */
    private AppCompatCheckBox mPreCheckBox = null;
    @Override
    public boolean checkSelect(FriendDetailInfo.DataBean friendId, AppCompatCheckBox checkBox) {
        if(selectKind == SELECT_RAIL){
            ((MyApplication)getApplication()).setRailName(friendId.getFriendRemarkName());
            ((MyApplication)getApplication()).setRailPic(friendId.getHeadPic());
        }
        if(selectKind == SELECT_RAIL || selectKind == SELECT_TRACK){
            if(mPreCheckBox != null){
                if(mPreCheckBox.isChecked() && mPreCheckBox!=checkBox){
                    mPreCheckBox.setChecked(false);
                }
            }
            mPreCheckBox = checkBox;
            if(mFriendSelectList.contains(friendId.getFriendPhoneNo())){
                mFriendSelectList.remove(friendId.getFriendPhoneNo());
            }else {
                mFriendSelectList.clear();
                mFriendSelectList.add(friendId.getFriendPhoneNo());
            }
        }else {
            if (mFriendSelectList.contains(friendId.getFriendPhoneNo())) {
                mFriendSelectList.remove(friendId.getFriendPhoneNo());
            } else {
                mFriendSelectList.add(friendId.getFriendPhoneNo());
            }
        }
        return true;
    }

    //首页选择 好友选择好友
    @Override
    public void addFriend(String friendId) {
        if(mFriendSelectList.contains(friendId)) {
            return;
        }

        mFriendSelectList.add(friendId);
    }

    @Override
    public void removeFriend(String friendId) {
        if(!mFriendSelectList.contains(friendId)) {
            return;
        }
        mFriendSelectList.remove(friendId);
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
}
