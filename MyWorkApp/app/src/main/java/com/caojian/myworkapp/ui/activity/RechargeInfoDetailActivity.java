package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.RechargeInfo;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.RechargeInfoDetailContract;
import com.caojian.myworkapp.ui.presenter.RechargeInfoDetailPresenter;
import com.caojian.myworkapp.widget.LineDecoration;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RechargeInfoDetailActivity extends MvpBaseActivity<RechargeInfoDetailContract.View,RechargeInfoDetailPresenter>implements RechargeInfoDetailContract.View {
    public static void go2RechargeInfoDetailActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,RechargeInfoDetailActivity.class);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recy_integral_detail)
    RecyclerView mRecy_detail;
    @BindView(R.id.swipe_integral)
    SwipeRefreshLayout mSwipeIntegral;
    Unbinder unbinder;
    List<RechargeInfo.DataBean.DetailsBean> mListData = new LinkedList<>();
    IntegralDetailAdapter mAdapter;
    RechargeInfoDetailPresenter mPresenter;
    int nowPageNum = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_detail);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("充值记录");
        initRecy();
        mPresenter.getRechargeInfoDetail(nowPageNum);
        mSwipeIntegral.setRefreshing(true);
        mSwipeIntegral.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getRechargeInfoDetail(nowPageNum);
            }
        });
    }

    private void initRecy() {
        mRecy_detail.addItemDecoration(new LineDecoration(RechargeInfoDetailActivity.this));
        mRecy_detail.setLayoutManager(new LinearLayoutManager(RechargeInfoDetailActivity.this));
        mAdapter = new IntegralDetailAdapter();
        mRecy_detail.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public RechargeInfoDetailPresenter createPresenter() {
        mPresenter = new RechargeInfoDetailPresenter(this,RechargeInfoDetailActivity.this);
        return mPresenter;
    }

//    @Override
//    public IntegralDetailPresenter createPresenter() {
//        mPresenter = new IntegralDetailPresenter(this,RechargeInfoDetailActivity.this);
//        return mPresenter;
//    }



    @Override
    public void getSuccess(RechargeInfo.DataBean dataBean) {
        if(mSwipeIntegral.isRefreshing())
        {
            mSwipeIntegral.setRefreshing(false);
        }
        if(dataBean.getPageNumber() == nowPageNum)
        {
            mListData.clear();
        }
        nowPageNum = dataBean.getPageNumber();
        mListData.addAll(dataBean.getDetails());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void error(String msg) {
        if(mSwipeIntegral.isRefreshing())
        {
            mSwipeIntegral.setRefreshing(false);
        }
        if(!msg.isEmpty()) showToast(msg, Toast.LENGTH_SHORT);
    }


    class IntegralDetailAdapter extends RecyclerView.Adapter<IntegralDetailAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.intergral_detail_item,parent,false);
            return new ViewHolder(itemView);
        }

        private  final String[] DETAILS = {"购买会员","购买轨迹回放","购买围栏报警"};
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            RechargeInfo.DataBean.DetailsBean item = mListData.get(position);
            holder.mTv_money.setText(item.getAmount());
            holder.mTv_from.setText(DETAILS[item.getDetailInfo()]);
            holder.mTv_time.setText(item.getCreateTime());
            // TODO: 2017/9/17 展示信息
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.tv_money)
            TextView mTv_money;
            @BindView(R.id.tv_from)
            TextView mTv_from;
            @BindView(R.id.tv_time)
            TextView mTv_time;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
}
