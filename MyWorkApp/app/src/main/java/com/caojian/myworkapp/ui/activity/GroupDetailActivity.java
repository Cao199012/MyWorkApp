package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.adapter.GroupMembersAdapter;
import com.caojian.myworkapp.model.data.FriendItem;
import com.caojian.myworkapp.widget.ChangeEditFragment;
import com.caojian.myworkapp.widget.SelectDayFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GroupDetailActivity extends BaseTitleActivity implements GroupMembersAdapter.ItemClick {
    public static void go2GroupDetailActivity(Context from, String detail) {
        Intent intent = new Intent(from, GroupDetailActivity.class);
        intent.putExtra("detail", detail);
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
    @BindView(R.id.select_day)
    LinearLayout mBodyDaySelect;
    private Unbinder unbinder;
    private List<FriendItem> mListData = new ArrayList<>();
    private GroupMembersAdapter mListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("好友群详情");
        intRecy();

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mBodyTimeSelect.setVisibility(View.VISIBLE);
                    mBodyDaySelect.setVisibility(View.VISIBLE);

                }else
                {
                    mBodyTimeSelect.setVisibility(View.GONE);
                    mBodyDaySelect.setVisibility(View.GONE);
                }

            }
        });
    }
    private void intRecy() {
        for (int i = 0; i < 10; i++)
        {
            mListData.add(new FriendItem());
        }
        mListAdapter = new GroupMembersAdapter(mListData,this,GroupDetailActivity.this);
        mRecy_member.setLayoutManager(new GridLayoutManager(GroupDetailActivity.this,5));
        mRecy_member.setAdapter(mListAdapter);
    }

    public void showTimeSelect(View view)
    {

        TimePickerDialog timePickerDialog = new TimePickerDialog(GroupDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            }
        },0,0,true);
        timePickerDialog.show();
    }

    ChangeEditFragment changeEditFragment;
    //修改好友昵称
    public void changeName(View view)
    {
        changeEditFragment = ChangeEditFragment.newInstance("修改组名","不能超过十个字","取消","确定");
        changeEditFragment.show(getSupportFragmentManager(),"edit");
    }

    SelectDayFragment selectDayFragment ;
    //选择监测周期
    @OnClick(R.id.select_day)
    public void selectDay()
    {
        selectDayFragment = SelectDayFragment.newInstance("");
        selectDayFragment.show(getSupportFragmentManager(),"select");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }



    @Override
    public void itemSlected(FriendItem item) {

    }

    @Override
    public void addItem() {
        FriendSelectActivity.go2FriendSelectActivity(GroupDetailActivity.this,FriendSelectActivity.SELECT_ADD,"添加好友");
    }

    @Override
    public void removeItem() {
        FriendSelectActivity.go2FriendSelectActivity(GroupDetailActivity.this,FriendSelectActivity.SELECT_DELETE,"删除好友");
    }
}
