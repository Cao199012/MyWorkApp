package com.caojian.myworkapp.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.caojian.myworkapp.R;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by caojian on 2017/9/19.
 */

public class SelectDayFragment extends AppCompatDialogFragment {

    public static SelectDayFragment newInstance(String title)
    {
        SelectDayFragment fragment = new SelectDayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        fragment.setArguments(bundle);
        return fragment;
    }
    @BindView(R.id.recy_day)
    RecyclerView mRecy_day;
    FragmentDaySelectListener mListener;
    List<Integer> mListDay;
    VipAdapter mVipAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AlertDialogStyle);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_select_day,container,false);
        ButterKnife.bind(this,root);
        initRecy();
        return root;
    }
    private List<String> mListData = new LinkedList<>();
    private void initRecy() {
        String[] days = getActivity().getResources().getStringArray(R.array.day_list);
        mListData.clear();
        for (int i = 0 ;i < days.length;i++)
        {
            mListData.add(days[i]);
        }
        mVipAdapter = new VipAdapter();
        mRecy_day.addItemDecoration(new LineDecoration(getContext()));
        mRecy_day.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecy_day.setAdapter(mVipAdapter);
    }
   //两个按钮的点击事件
    @OnClick(R.id.btn_sure)
    public void sureButton(){
        mListener.sure();
    }
    @OnClick(R.id.btn_cancel)
    public void cancelButton(){
        mListener.cancel();
    }
    class VipAdapter extends RecyclerView.Adapter<VipAdapter.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_day_item,parent,false);
            return new ViewHolder(item);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
              holder.tv_day.setText(mListData.get(position));
              if(mListDay != null && mListDay.size() > 0)
              {
                  if(mListDay.contains(position+1)){
                      holder.check_day.setChecked(true);
                  }
              }
              holder.check_day.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                  @Override
                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                      //根据点击状态改变来记录
                      mListener.daySelectChange(position+1);
                  }
              });
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.day_checked)
            CheckBox check_day;
            @BindView(R.id.tv_day)
            TextView tv_day;
            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }

    public void setListDay(List<Integer> mListDay) {
        this.mListDay = mListDay;
        if(mVipAdapter != null){
            mVipAdapter.notifyDataSetChanged();
        }
    }

    public void setListener(FragmentDaySelectListener mListener) {
        this.mListener = mListener;
    }
    public interface FragmentDaySelectListener{
        void daySelectChange(Integer num);
        void sure();
        void cancel();
    }


}
