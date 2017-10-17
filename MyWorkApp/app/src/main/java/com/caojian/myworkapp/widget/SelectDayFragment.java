package com.caojian.myworkapp.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.caojian.myworkapp.R;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AlertDialogStyle);
    }



//    @Override
//    public void onStart() {
//        super.onStart();
//        //设置dilogfragment的高宽
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            DisplayMetrics dm = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//    }

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

        for (int i = 0 ;i < days.length;i++)
        {
            mListData.add(days[i]);

        }

        VipAdapter vipAdapter = new VipAdapter();
        mRecy_day.addItemDecoration(new LineDecoration(getContext()));
        mRecy_day.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecy_day.setAdapter(vipAdapter);
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
//            holder.tv_buy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //点击购买按钮，通知acticiy 显示支付方式
//                    if (mListener != null)
//                    {
//                        mListener.showBuy(item);
//                    }
//                }
//            });
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
    public void setListener(FragmentDaySelectListener mListener) {
        this.mListener = mListener;
    }
    public interface FragmentDaySelectListener{

    }

}
