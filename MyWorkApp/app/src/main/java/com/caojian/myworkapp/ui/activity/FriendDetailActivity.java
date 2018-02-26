package com.caojian.myworkapp.ui.activity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.ChangeFriendMsgContract;
import com.caojian.myworkapp.ui.presenter.ChangeFriendPresenter;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.ChangeEditFragment;
import com.caojian.myworkapp.widget.MyDialogFragment;
import com.caojian.myworkapp.widget.SelectDayFragment;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.caojian.myworkapp.until.ActivityUntil.numList2Str;

public class FriendDetailActivity extends MvpBaseActivity<ChangeFriendMsgContract.View,ChangeFriendPresenter> implements ChangeEditFragment.FragmentChangeListener,ChangeFriendMsgContract.View,MyDialogFragment.FragmentDialogListener{
    public static void go2FriendDetailActivity(BaseTitleActivity fromClass, String databean,int request)
    {
        Intent intent = new Intent(fromClass,FriendDetailActivity.class);
        intent.putExtra("databean",databean);
        fromClass.startActivityForResult(intent,request);
    }
    public static final int REQUESTCODE = 1003;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toggleButton)
    SwitchCompat mToggleButton;
    @BindView(R.id.time_select_body)
    LinearLayout mBodyTimeSelect;
    @BindView(R.id.select_day)
    LinearLayout mBodyDaySelect;
    @BindView(R.id.detail_phone)
    TextView mFriend_phone;
    @BindView(R.id.detail_name)
    TextView mFriend_name;
    @BindView(R.id.tv_start)
    TextView mStartTime;
    @BindView(R.id.tv_end)
    TextView mEndTime;
    @BindView(R.id.show_day)
    TextView mTv_showDay;
    @BindView(R.id.detail_head)
    ImageView mDetail_head;
    private Unbinder unbinder;
    ChangeFriendPresenter mPresenter;
    FriendDetailInfo.DataBean mFriendDataBean;
    FriendDetailInfo.DataBean mFriendInfo;
    MyDialogFragment dialogFragment;//提示弹出框
    List<Integer> mListDaySelected = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        mFriendDataBean = new Gson().fromJson(getIntent().getStringExtra("databean"),FriendDetailInfo.DataBean.class);
        unbinder = ButterKnife.bind(this);
        toolbar.setTitle("好友详情");
        initView();
        mPresenter.getFriendInfo(mFriendDataBean.getFriendPhoneNo());
    }

    private void initView() {
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mBodyTimeSelect.setVisibility(View.VISIBLE);
                    mBodyDaySelect.setVisibility(View.VISIBLE);
                    mFriendInfo.setIsAccreditVisible(1);
                }else
                {
                    mBodyTimeSelect.setVisibility(View.GONE);
                    mBodyDaySelect.setVisibility(View.GONE);
                    mFriendInfo.setIsAccreditVisible(2);
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

    ChangeEditFragment changeEditFragment;
    //修改好友昵称
    public void changeName(View view)
    {
        changeEditFragment = ChangeEditFragment.newInstance("修改好友备注","不能超过十个字","取消","确定");
        ActivityUntil.showDialogFragment(getSupportFragmentManager(),changeEditFragment,"edit");
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
                    mTv_showDay.setText(numList2Str(mListDaySelected)[1]);
                }
                @Override
                public void cancel() {
                    selectDayFragment.dismiss();
                }
            });
        }
        selectDayFragment.setListDay(mListDaySelected);
        //selectDayFragment.show(getSupportFragmentManager(),"select");
        ActivityUntil.showDialogFragment(getSupportFragmentManager(),selectDayFragment,"select");


    }

    @Override
    public ChangeFriendPresenter createPresenter() {
        mPresenter = new ChangeFriendPresenter(FriendDetailActivity.this,this);
        return mPresenter;
    }

    //取消修改
    @Override
    public void cancelEdit() {
        if (changeEditFragment != null)
        {
            changeEditFragment.dismiss();
        }
    }
    //修改备注名
    @Override
    public void submitEdit(String msg) {
        if (changeEditFragment != null)
        {
            changeEditFragment.dismiss();
        }
        mFriendInfo.setFriendRemarkName(msg);
        mPresenter.changeMsg(mFriendInfo);
    }

    //修改成功
    @Override
    public void changeSuccess() {
        // TODO: 2017/11/1 标记更改
        mFriend_name.setText(mFriendInfo.getFriendRemarkName());
        //删除成功好友列表发生变化，要刷新列表
        setResult(RESULT_OK);
        showToast("修改成功",Toast.LENGTH_SHORT);
       // mPresenter.getFriendInfo(mFriendDataBean.getFriendPhoneNo());
    }

    //删除好友成功
    @Override
    public void deleteSuccess(String msg) {
        showToast(msg,Toast.LENGTH_SHORT);
//        Intent intent = new Intent(Until.ACTION_FRIEND);
        //删除成功好友列表发生变化，要刷新列表
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void error(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void getFriendInfo(FriendDetailInfo friendDetailInfo) {
        mFriendInfo = friendDetailInfo.getData();
        initData(friendDetailInfo);
    }

    //向View 填充数据
    private void initData(FriendDetailInfo friendDetailInfo) {
        FriendDetailInfo.DataBean dataBean = friendDetailInfo.getData();
        mFriend_name.setText(friendDetailInfo.getData().getFriendRemarkName());
        mFriend_phone.setText(friendDetailInfo.getData().getFriendPhoneNo());
        if(!friendDetailInfo.getData().getHeadPic().equals(""))
        {
            Glide.with(FriendDetailActivity.this).load(Until.HTTP_BASE_URL+"downLoadPic.do?fileId="+dataBean.getHeadPic()).into(mDetail_head);

        }
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
    }

    //删除好友
    public void deleteFriend(View view)
    {
        if(dialogFragment == null)
        {
            dialogFragment = MyDialogFragment.newInstance("删除好友","你确定要删除此好友","取消","确定");
        }
       // dialogFragment.show(getSupportFragmentManager(),"delete");
        ActivityUntil.showDialogFragment(getSupportFragmentManager(),dialogFragment,"delete");
    }

    @Override
    public void cancel() {
        dialogFragment.dismiss();
    }

    @Override
    public void sure() {
        dialogFragment.dismiss();
        mPresenter.deleteFriend(mFriendDataBean.getFriendPhoneNo());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void changeTimeMessage(View view)
    {
        if(mListDaySelected.isEmpty())
        {
            mFriendInfo.setIsAccreditVisible(2);
        }else
        {
            mFriendInfo.setAccreditWeeks(numList2Str(mListDaySelected)[0]);
            if(mStartTime.getText().toString().trim().isEmpty()){
                showToast("请选择开始时间",Toast.LENGTH_SHORT);
                return;
            }
            mFriendInfo.setAccreditStartTime(mStartTime.getText().toString().trim());
            if(mEndTime.getText().toString().trim().isEmpty()){
                showToast("请选择结束时间",Toast.LENGTH_SHORT);
                return;
            }
            mFriendInfo.setAccreditEndTime(mEndTime.getText().toString().trim());
        }
        mPresenter.changeMsg(mFriendInfo);
    }


}
