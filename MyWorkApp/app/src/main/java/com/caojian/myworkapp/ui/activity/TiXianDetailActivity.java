package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.RewardScoreDetailMsg;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.IntegralDetailContract;
import com.caojian.myworkapp.ui.presenter.IntegralDetailPresenter;
import com.caojian.myworkapp.widget.LineDecoration;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TiXianDetailActivity extends MvpBaseActivity<IntegralDetailContract.View,IntegralDetailPresenter>implements IntegralDetailContract.View {
    public static void go2TiXianDetailActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,TiXianDetailActivity.class);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recy_integral_detail)
    RecyclerView mRecy_detail;
    Unbinder unbinder;
    List<RewardScoreDetailMsg.DataBean.DetailsBean> mListData = new LinkedList<>();
    TiXianDetailAdapter mAdapter;
    IntegralDetailPresenter mPresenter;
    int nowPageNum = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_detail);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("提现记录");
        initRecy();
        mPresenter.getIntegralDetail(nowPageNum);
    }

    private void initRecy() {
        mRecy_detail.addItemDecoration(new LineDecoration(TiXianDetailActivity.this));
        mRecy_detail.setLayoutManager(new LinearLayoutManager(TiXianDetailActivity.this));
        mAdapter = new TiXianDetailAdapter();
        mRecy_detail.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public IntegralDetailPresenter createPresenter() {
        mPresenter = new IntegralDetailPresenter(this,TiXianDetailActivity.this);
        return mPresenter;
    }


    @Override
    public void getSuccess(RewardScoreDetailMsg.DataBean dataBean) {
        if(Integer.parseInt(dataBean.getPageNumber()) == nowPageNum)
        {
            mListData.clear();
        }
        mListData.addAll(dataBean.getDetails());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void error(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }


    class TiXianDetailAdapter extends RecyclerView.Adapter<TiXianDetailAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.intergral_detail_item,parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            RewardScoreDetailMsg.DataBean.DetailsBean item = mListData.get(position);
            holder.mTv_money.setText(item.getRewardScore());
            holder.mTv_from.setText(item.getDetailInfo());
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
