package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.GroupInfo;
import com.caojian.myworkapp.ui.adapter.GroupMembersAdapter;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.ChangeGroupMsgContract;
import com.caojian.myworkapp.ui.presenter.ChangeGroupPresenter;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.widget.ChangeEditFragment;
import com.caojian.myworkapp.widget.MyDialogFragment;
import com.caojian.myworkapp.widget.SelectDayFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.caojian.myworkapp.until.ActivityUntil.numList2Str;

public class GroupDetailActivity extends MvpBaseActivity<ChangeGroupMsgContract.View,ChangeGroupPresenter> implements GroupMembersAdapter.ItemClick,ChangeGroupMsgContract.View
        ,ChangeEditFragment.FragmentChangeListener,MyDialogFragment.FragmentDialogListener{
    public static void go2GroupDetailActivity(Context from, String groupId) {
        Intent intent = new Intent(from, GroupDetailActivity.class);
        intent.putExtra("groupId", groupId);
        ((Activity) from).startActivityForResult(intent, 102);
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recy_group_member)
    RecyclerView mRecy_member;
    @BindView(R.id.toggleButton)
    SwitchCompat mToggleButton;
    @BindView(R.id.time_select_body)
    LinearLayout mBodyTimeSelect;
    @BindView(R.id.tv_start)
    TextView mStartTime;
    @BindView(R.id.tv_end)
    TextView mEndTime;
    @BindView(R.id.select_day)
    LinearLayout mBodyDaySelect;
    @BindView(R.id.show_day)
    TextView mTv_showDay;
    @BindView(R.id.tv_name)
    TextView mTvName;
    private Unbinder unbinder;
    private List<FriendDetailInfo.DataBean> mListData = new ArrayList<>();
    private GroupMembersAdapter mListAdapter;
    String groupId;
    ChangeGroupPresenter mPresenter;
    ChangeEditFragment changeEditFragment;
    List<Integer> mListDaySelected = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        unbinder = ButterKnife.bind(this);
        toolbar.setTitle("好友群详情");
        //传输的数据转换成类
        groupId = getIntent().getStringExtra("groupId");
        //初始化空间
        intView();
        //获取组信息
        mPresenter.getGroupInfo(groupId);
    }
    private void intView() {
        //展示好友列表
        mListAdapter = new GroupMembersAdapter(mListData,this,GroupDetailActivity.this);
        mRecy_member.setLayoutManager(new GridLayoutManager(GroupDetailActivity.this,5));
        mRecy_member.setAdapter(mListAdapter);
        //授权开关按钮
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mBodyTimeSelect.setVisibility(View.VISIBLE);
                    mBodyDaySelect.setVisibility(View.VISIBLE);
                    groupInfo.getData().setIsAccreditVisible(1);
                }else
                {
                    mBodyTimeSelect.setVisibility(View.GONE);
                    mBodyDaySelect.setVisibility(View.GONE);
                    groupInfo.getData().setIsAccreditVisible(2);
                }

            }
        });
    }
    //点击选择时间（开始或结束）
    TimePickerDialog mTimePickerDialog;
    TextView mTimeShowView;
    public void showTimeSelect(View view)
    {
        if(mTimePickerDialog == null)
        {
            mTimePickerDialog = initTimeDialog();
            mTimePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mTimeShowView.setText(mBaseTime);
                }
            });
        }
        mTimeShowView = (TextView) view;
        mTimePickerDialog.show();
    }
    SelectDayFragment selectDayFragment ;
    //选择监测周期
    @OnClick(R.id.select_day)
    public void selectDay()
    {
        if(selectDayFragment == null){
            selectDayFragment = SelectDayFragment.newInstance("");
            selectDayFragment.setListener(new SelectDayFragment.FragmentDaySelectListener() {
                @Override
                public void daySelectChange(Integer num) {
                    if(mListDaySelected.contains(num)){
                        mListDaySelected.remove(num);
                    }else {
                        mListDaySelected.add(num);
                    }
                }
                @Override
                public void sure() {
                    selectDayFragment.dismiss();
                    // TODO: 2017/11/10 更新显示选择的日期
                    mTv_showDay.setText(numList2Str(mListDaySelected)[1]);
                }
                @Override
                public void cancel() {
                    selectDayFragment.dismiss();
                }
            });
        }
        selectDayFragment.show(getSupportFragmentManager(),"select");
        selectDayFragment.setListDay(mListDaySelected);
    }

    //修改好友昵称
    public void changeName(View view)
    {
        changeEditFragment = ChangeEditFragment.newInstance("修改组名","不能超过十个字","取消","确定");
        changeEditFragment.show(getSupportFragmentManager(),"edit");
    }
    @Override
    public void cancelEdit() {
        changeEditFragment.dismiss();
    }
    @Override
    public void submitEdit(String msg) {
        changeEditFragment.dismiss();
        groupInfo.getData().setGroupName("msg");
        mPresenter.changeMsg(groupInfo.getData());
    }
    //删除组信息
    MyDialogFragment dialogFragment;
    public void deleteGroup(View view)
    {
        if(dialogFragment == null)
        {
            dialogFragment = MyDialogFragment.newInstance("删除群组","你确定要删除此群组","取消","确定");
        }
        dialogFragment.show(getSupportFragmentManager(),"delete");
    }

    @Override
    public void cancel() {
        dialogFragment.dismiss();
    }

    @Override
    public void sure() {
        dialogFragment.dismiss();
        mPresenter.deleteGroup(groupId);
    }
    //返回presenter
    @Override
    public ChangeGroupPresenter createPresenter() {
        mPresenter = new ChangeGroupPresenter(GroupDetailActivity.this,this);
        return mPresenter;
    }

    //群组好友点击
    @Override
    public void itemSelected(FriendDetailInfo.DataBean item) {
        // TODO: 2017/11/6 跳转好友详情
    }
    //添加按钮
    @Override
    public void addItem() {
        if(groupInfo != null) {
            ((MyApplication) getApplication()).setGroupFriends(groupInfo.getData().getFriends());
        }
        FriendSelectActivity.go2FriendSelectActivity(GroupDetailActivity.this,FriendSelectActivity.SELECT_ADD,"添加好友");
    }
    //删除按钮
    @Override
    public void removeItem() {
        ((MyApplication)getApplication()).setGroupFriends(groupInfo.getData().getFriends());
        FriendSelectActivity.go2FriendSelectActivity(GroupDetailActivity.this,FriendSelectActivity.SELECT_DELETE,"删除好友");
    }

    //请求结果的处理接口
    //修改信息成功
    @Override
    public void changeSuccess(int kindCode) {
        switch (kindCode){
            case 1:
                mPresenter.getGroupInfo(groupId);
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
    //删除群组成功
    @Override
    public void deleteSuccess(String msg) {
        // TODO: 2017/11/12
        showToast(msg, Toast.LENGTH_SHORT);
        finish();
    }
    @Override
    public void error(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }
    //记录群组详情
    GroupInfo groupInfo = null;
    @Override
    public void getInfoSuccess(GroupInfo groupDetailInfo) {
        if(groupDetailInfo == null) return;
        groupInfo = groupDetailInfo;
        initData(groupDetailInfo.getData());
    }
    //更新好友群组详情
    private void initData( GroupInfo.DataBean dataBean){
        mTvName.setText(dataBean.getGroupName());
        // TODO: 2017/11/14 更新显示数据
        if(dataBean.getIsAccreditVisible() == 1){
            mToggleButton.setChecked(true);
            mBodyTimeSelect.setVisibility(View.VISIBLE);
            mBodyDaySelect.setVisibility(View.VISIBLE);
        }else {
            mToggleButton.setChecked(false);
        }
        mStartTime.setText(dataBean.getAccreditStartTime());
        mEndTime.setText(dataBean.getAccreditEndTime());
        String weeks = dataBean.getAccreditWeeks();
        if(weeks != null && weeks.length() > 0) {
            String[] week = weeks.split(",");
            mListDaySelected.clear();
            for (String num : week) {
                mListDaySelected.add(Integer.parseInt(num));
            }
            mTv_showDay.setText(numList2Str(mListDaySelected)[1]);
        }
        mListData.clear();
        mListData.addAll(dataBean.getFriends());
        mListAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //// TODO: 2017/11/11 接收好友选择的去处理
        ArrayList<String> friends = new ArrayList<>();
        if(resultCode == RESULT_OK) {
           friends = data.getStringArrayListExtra("location");
        }else {
            return;
        }

        switch (requestCode){
            case FriendSelectActivity.SELECT_ADD:
                if(friends.isEmpty()) return;
                // TODO: 2017/11/14 批量请求
                mPresenter.addFriend2Group(groupId,friends.get(0));
               // mPresenter.
                break;
            case FriendSelectActivity.SELECT_DELETE:
                if(friends.isEmpty()) return;
                mPresenter.removeFriendFromGroup(groupId,friends.get(0));
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void changeTimeMessage(View view)
    {
        GroupInfo.DataBean dataBean = groupInfo.getData();
        if(mListDaySelected.isEmpty())
        {
            dataBean.setIsAccreditVisible(2);
        }else
        {
            dataBean.setAccreditWeeks(numList2Str(mListDaySelected)[0]);
            if(mStartTime.getText().toString().trim().isEmpty()){
                showToast("请选择开始时间",Toast.LENGTH_SHORT);
                return;
            }
            dataBean.setAccreditStartTime(mStartTime.getText().toString().trim());
            if(mEndTime.getText().toString().trim().isEmpty()){
                showToast("请选择结束时间",Toast.LENGTH_SHORT);
                return;
            }
            dataBean.setAccreditEndTime(mEndTime.getText().toString().trim());
        }
        mPresenter.changeMsg(dataBean);
    }
}
