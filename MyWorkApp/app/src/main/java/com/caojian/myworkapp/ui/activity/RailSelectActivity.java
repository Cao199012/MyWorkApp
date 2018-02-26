package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.Request.PositionInfoDto;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.until.ActivityUntil;
import com.google.gson.Gson;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RailSelectActivity extends BaseTitleActivity implements CompoundButton.OnCheckedChangeListener {
    public static void go2RailSelectActivity(BaseTitleActivity fromClass,int from_kind)
    {
        Intent intent = new Intent(fromClass,RailSelectActivity .class);
        intent.putExtra("from",from_kind);
        fromClass.startActivityForResult(intent,from_kind);
    }
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.select_range)
    RelativeLayout selectRange;
    @BindView(R.id.select_body)
    LinearLayout selectCheckBody;
    @BindView(R.id.select_name)
    TextView mFriendName;
    @BindView(R.id.tv_start)
    TextView mTvStartTime;
    @BindView(R.id.tv_end)
    TextView mTvEndTime;
    @BindViews({R.id.first_point,R.id.second_point,R.id.third_point,R.id.fourth_point})
    TextView[] mPointShow;
    private static final int SELECTPOINT_REQUESTCODE = 1005;
    public static final int FROM_RAIL = 1;  //监测选择的
    public static final int FROM_TRACK = 2; // 轨迹选择
    int from_kind = FROM_RAIL;
    private String mFriendId = ""; //检测好友id
    private List<PositionInfoDto> mPositionInfoDtos = new LinkedList<>();//查询好友位置信息
    Unbinder unbinder;
    Map mListPoi = new android.support.v4.util.ArrayMap();
    LatLng[] latLngs = new LatLng[4];
    MyApplication mApplication ;
    //轨迹查询选项
    private boolean isProcessed = true;
    private boolean isDenoise = false;
    private boolean isVacuate = false;
    private boolean isMapmatch = false;

    private CheckBox processedCBx = null;
    private CheckBox denoiseCBx = null;
    private CheckBox vacuateCBx = null;
    private CheckBox mapmatchCBx = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rail_select);
        unbinder = ButterKnife.bind(this);
        from_kind = getIntent().getIntExtra("from",FROM_RAIL);  //获取进入前的页面
        mApplication = (MyApplication) getApplication();
        if(from_kind == FROM_TRACK)  //轨迹选择 选点隐藏
        {
            selectRange.setVisibility(View.GONE);
            processedCBx = (CheckBox) findViewById(R.id.processed);
            denoiseCBx = (CheckBox) findViewById(R.id.denoise);
            vacuateCBx = (CheckBox) findViewById(R.id.vacuate);
            mapmatchCBx = (CheckBox) findViewById(R.id.mapmatch);

            processedCBx.setOnCheckedChangeListener(this);
            denoiseCBx.setOnCheckedChangeListener(this);
            vacuateCBx.setOnCheckedChangeListener(this);
            mapmatchCBx.setOnCheckedChangeListener(this);
            //之前选择过 显示显示上次选择的时间
            if(mApplication.getTrackStartTime() != null && mApplication.getTrackEndTime() != null){
                mTvStartTime.setText(mApplication.getTrackStartTime());
                mTvEndTime.setText(mApplication.getTrackEndTime());
            }else { //第一次进入m默认显示当天

                android.text.format.Time date = new android.text.format.Time();
                date.setToNow(); // 取得系统时间。
                int year = date.year;
                int month = date.month+1;
                int day = date.monthDay;
                String _time = year+"-"+((month < 10)?"0":"")+month+"-"+((day < 10)?"0":"")+day;
                mTvStartTime.setText(_time);
                mTvEndTime.setText(_time);
            }
        }else {   //围栏选择 初始化时间
            mTvStartTime.setText("09:00");
            mTvEndTime.setText("17:00");
            selectCheckBody.setVisibility(View.GONE);
        }
        String title = getResources().getStringArray(R.array.select_kind)[from_kind-1]; //设置标题
        mToolbar.setTitle(title);

    }
    //选择检测时间
    TimePickerDialog mTimePickerDialog; //小时选择器
    DatePickerDialog mDatePickerDialog; //日期选择器
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
                       DatePicker picker = mDatePickerDialog.getDatePicker();
                       int year = picker.getYear();
                       int month = picker.getMonth()+1;
                       int day = picker.getDayOfMonth();
                       mBaseDate = year+"-"+((month < 10)?"0":"")+month+"-"+((day < 10)?"0":"")+day;
                       mTimeShowView.setText(mBaseDate);
                   }
               });
           }
            mDatePickerDialog.show();
            return;
        }
    }

    @OnClick(R.id.friend_select)
    public void selectFriend()
    {
        if(from_kind == FROM_TRACK){
            FriendSelectActivity.go2FriendSelectActivity(RailSelectActivity.this,FriendSelectActivity.SELECT_TRACK,"选择好友");
        }else {
            FriendSelectActivity.go2FriendSelectActivity(RailSelectActivity.this,FriendSelectActivity.SELECT_RAIL,"选择好友");
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

        SelectPointActivity.go2SelectPointActivity(RailSelectActivity.this,SELECTPOINT_REQUESTCODE,new Gson().toJson(mListPoi),selectNum);
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
                    LatLng latLng = data.getParcelableExtra("latLng");
                    mListPoi.put(selectNum,latLng);
                    latLngs[selectNum] = latLng;
                    lat2Address(latLng.latitude,latLng.longitude,mPointShow[selectNum]);
                    //mPointShow[selectNum].setText("X:"+latLng.latitude+"\nY:"+latLng.longitude);
                }
                break;
            case FriendSelectActivity.SELECT_TRACK:
            case FriendSelectActivity.SELECT_RAIL:
                if(resultCode == Activity.RESULT_OK && data != null) {
                    List<String> _list = data.getStringArrayListExtra("location");
                    for (String friendNo : _list){
                        mFriendId = friendNo;
                        mFriendName.setText(mFriendId);
                    }

                }
                break;
        }
    }
    //提交围栏信息
    public void submitMsg(View view)
    {
        String startTime =  mTvStartTime.getText().toString().trim();
        String endTime =  mTvEndTime.getText().toString().trim();
        if(startTime.isEmpty())
        {
            showToast("请选择开始时间", Toast.LENGTH_SHORT);
            return;
        }else if(endTime.isEmpty())
        {
            showToast("请选择结束时间", Toast.LENGTH_SHORT);
            return;
        }else if(mFriendId.isEmpty())
        {
            showToast("请选择好友", Toast.LENGTH_SHORT);
            return;
        }
        if(from_kind == FROM_TRACK) {
            Intent intent = new Intent();
            intent.putExtra("startTime", startTime);
            intent.putExtra("endTime",endTime);
            try {
                 long _start = ActivityUntil.parseStringToDate(startTime,"yyyy-MM-dd").getTime();
                long _end  = ActivityUntil.parseStringToDate(endTime,"yyyy-MM-dd").getTime();

                if(_end < _start){
                    showToast("结束时间不能小于开始时间",Toast.LENGTH_SHORT);
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            intent.putExtra("friend", mFriendId);
            intent.putExtra("processed", isProcessed);
            intent.putExtra("denoise", isDenoise);
            intent.putExtra("vacuate", isVacuate);
            intent.putExtra("mapmatch", isMapmatch);
           mApplication.setTrackStartTime(startTime);
           mApplication.setTrackEndTime(endTime);
            setResult(RESULT_OK,intent);
            finish();
        }else if (from_kind == FROM_RAIL)
        {
            // TODO: 2017/12/18 存在本地文件
            if(mListPoi.isEmpty() || mListPoi.size() < 4)
            {
                showToast("请选择监测范围", Toast.LENGTH_SHORT);
                return;
            }

            ActivityUntil.saveRailMsg(RailSelectActivity.this,mFriendId,startTime,endTime,new Gson().toJson(latLngs),mApplication.getRailName());
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    /**
     * g根据选择的坐标显示地理位置
     * @param textView 显示的view
     * */
    private void lat2Address(double lat,Double lot,TextView textView){
        LatLng ll = new LatLng(lat,lot);
        GeoCoder geoCoder = GeoCoder.newInstance();
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    return;
                }
                textView.setText(result.getSematicDescription());
                geoCoder.destroy();
            }
            // 地理编码查询结果回调函数
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                geoCoder.destroy();
            }
        });
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.processed:
                isProcessed = isChecked;
                break;

            case R.id.denoise:
                isDenoise = isChecked;
                break;

            case R.id.vacuate:
                isVacuate = isChecked;
                break;

            case R.id.mapmatch:
                isMapmatch = isChecked;
                break;

            default:
                break;
        }
    }
}
