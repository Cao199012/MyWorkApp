package com.caojian.myworkapp.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
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
    RelativeLayout selectRange;
    @BindViews({R.id.first_point,R.id.second_point,R.id.third_point,R.id.fourth_point})
    TextView[] mPointShow;
    private static final int SELECTPOINT_REQUESTCODE = 1005;
    public static final int FROM_RAIL = 1;  //监测选择的
    public static final int FROM_TRACK = 2; // 轨迹选择
    int from_kind = FROM_RAIL;
    Unbinder unbinder;
    List<PoiInfo> mListPoi = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rail_select);
        unbinder = ButterKnife.bind(this);
        from_kind = getIntent().getIntExtra("from",FROM_RAIL);
        if(from_kind == FROM_TRACK)
        {
            selectRange.setVisibility(View.GONE);
        }
        String title = getResources().getStringArray(R.array.select_kind)[from_kind-1];
        mToolbar.setTitle(title);
    }
    //选择检测时间
    TimePickerDialog mTimePickerDialog;
    DatePickerDialog mDatePickerDialog;
    TextView mTimeShowView;
    public void showTimeSelect(View view)
    {
        if(from_kind == FROM_RAIL)  //围栏报警时间选择
        {
            if (mTimePickerDialog == null) {
                mTimePickerDialog = initTimeDialog();
                mTimePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTimeShowView.setText(mBaseTime);
                    }
                });
            }
            mTimePickerDialog.show();
        }
        mTimeShowView = (TextView) view;
        if(from_kind == FROM_TRACK) //查看轨迹
        {
           if(mDatePickerDialog == null){
               mDatePickerDialog = initDateDialog();
               mDatePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       mTimeShowView.setText(mBaseDate);
                   }
               });
           }
            mDatePickerDialog.show();
            return;
        }
    }

    private int selectNum = 0;
    //选择监测范围
    public void go2SelectPoint(View view)
    {
        switch (view.getId()){
            case R.id.first_point:
                selectNum = 0;
                break;
            case R.id.second_point:
                selectNum = 1;
                break;
            case R.id.third_point:
                selectNum = 2;
                break;
            case R.id.fourth_point:
                selectNum = 3;
                break;
        }
        SelectPointActivity.go2SelectPointActivity(RailSelectActivity.this,SELECTPOINT_REQUESTCODE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SELECTPOINT_REQUESTCODE:
                if(data != null){
                    PoiInfo poiInfo = new Gson().fromJson(data.getStringExtra("poiInfo"),PoiInfo.class);
                    mListPoi.add(selectNum,poiInfo);
                    mPointShow[selectNum].setText(poiInfo.address);
                }

                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
