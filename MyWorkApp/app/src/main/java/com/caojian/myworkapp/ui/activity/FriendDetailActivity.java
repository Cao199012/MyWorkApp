package com.caojian.myworkapp.ui.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.model.data.FriendItem;
import com.caojian.myworkapp.widget.ChangeEditFragment;
import com.caojian.myworkapp.widget.SelectDayFragment;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FriendDetailActivity extends BaseTitleActivity implements ChangeEditFragment.FragmentChangeListener{
    public static void go2FriendDetailActivity(Context fromClass,String databean)
    {
        Intent intent = new Intent(fromClass,FriendDetailActivity.class);
        intent.putExtra("databean",databean);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toggleButton)
    SwitchCompat mToggleButton;
    @BindView(R.id.time_select_body)
    LinearLayout mBodyTimeSelect;
    @BindView(R.id.select_day)
    LinearLayout mBodyDaySelect;
    private Unbinder unbinder;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        FriendItem.DataBean dataBean = new Gson().fromJson(getIntent().getStringExtra("databean"),FriendItem.DataBean.class);
        Log.i("databean",dataBean.toString());
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("好友详情");
        initView();
        c = Calendar.getInstance();

    }

    private void initView() {
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
    private Calendar c;
   // 然后对其进行实例化：

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showTimeSelect(View view)
    {
        c.setTimeInMillis(System.currentTimeMillis());
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(FriendDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.setTimeInMillis(System.currentTimeMillis());
                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                c.set(Calendar.MINUTE, minute);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
            }
        },hour,minute,true);
        timePickerDialog.show();
    }

    ChangeEditFragment changeEditFragment;
    //修改好友昵称
    public void changeName(View view)
    {
        changeEditFragment = ChangeEditFragment.newInstance("修改昵称","不能超过十个字","取消","确定");
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
    public void cancelEdit() {
        if (changeEditFragment != null)
        {
            changeEditFragment.dismiss();
        }
    }

    @Override
    public void sumbitEdit() {
        if (changeEditFragment != null)
        {
            changeEditFragment.dismiss();
        }
    }
}
