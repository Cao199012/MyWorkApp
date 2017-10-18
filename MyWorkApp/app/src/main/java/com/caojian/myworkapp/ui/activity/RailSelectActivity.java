package com.caojian.myworkapp.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RailSelectActivity extends BaseTitleActivity {
    public static void go2RailSelectActivity(Context fromClass,int from_kind)
    {
        Intent intent = new Intent(fromClass,RailSelectActivity .class);
        intent.putExtra("from",from_kind);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.select_range)
    RelativeLayout selecRange;

    public static final int FROM_RAIL = 1;  //监测选择的
    public static final int FROM_TRACK = 2; // 轨迹选择

    int from_kind = FROM_RAIL;

    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rail_select);
        unbinder = ButterKnife.bind(this);

        from_kind = getIntent().getIntExtra("from",FROM_RAIL);
        if(from_kind == FROM_TRACK)
        {
            selecRange.setVisibility(View.GONE);
        }

        String title = getResources().getStringArray(R.array.select_kind)[from_kind-1];
        mToolbar.setTitle(title);
    }

    //选择检测时间
    public void showTimeSelect(View view)
    {


        if(from_kind == FROM_RAIL)
        {
            TimePickerDialog timePickerDialog = new TimePickerDialog(RailSelectActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                }
            },0,0,true);
            timePickerDialog.show();
            return;
        }

        if(from_kind == FROM_TRACK)
        {
            DatePickerDialog datePickerDialog = new DatePickerDialog(RailSelectActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                }
            },2017,9,20);

            datePickerDialog.show();
            return;
        }

    }

    //选择监测范围
    public void go2SelectPoint(View view)
    {
        SelectPointActivity.go2SelectPointActivity(RailSelectActivity.this);
    }

    public void selectRailFriend(View view)
    {
        if(from_kind == FROM_RAIL)
        {
            FriendSelectActivity.go2FriendSelectActivity(RailSelectActivity.this,FriendSelectActivity.SELECT_RAIL,"选择监测好友");
            return;
        }

        if(from_kind == FROM_TRACK)
        {
            FriendSelectActivity.go2FriendSelectActivity(RailSelectActivity.this,FriendSelectActivity.SELECT_TRACK,"选择查看好友");
            return;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
