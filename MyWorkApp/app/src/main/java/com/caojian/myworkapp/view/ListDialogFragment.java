package com.caojian.myworkapp.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.modules.buy.VipItem;
import com.caojian.myworkapp.until.recyutil.LineDecoration;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caojian on 2017/9/19.
 */

public class ListDialogFragment extends AppCompatDialogFragment {

    public static ListDialogFragment newInstance(String title)
    {
        ListDialogFragment fragment = new ListDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        fragment.setArguments(bundle);
        return fragment;
    }


    @BindView(R.id.recy_vip)
    RecyclerView mRecy_vip;




    FragmentBuyListener mListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AlertDialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置dilogfragment的高宽
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialog_list,container,false);
        ButterKnife.bind(this,root);

        initRecy();
        return root;
    }

    private List<VipItem> mListData = new LinkedList<>();
    private void initRecy() {

        for (int i = 0 ;i < 5;i++)
        {
            mListData.add(new VipItem());

        }

        VipAdapter vipAdapter = new VipAdapter();
        mRecy_vip.addItemDecoration(new LineDecoration(getContext()));
        mRecy_vip.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecy_vip.setAdapter(vipAdapter);
    }

    class VipAdapter extends RecyclerView.Adapter<VipAdapter.ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.vip_item,parent,false);
            return new ViewHolder(item);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            VipItem item = mListData.get(position);
            holder.tv_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击购买按钮，通知acticiy 显示支付方式
                    if (mListener != null)
                    {
                        mListener.showBuy(item);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.tv_kind)
            TextView tv_kind;
            @BindView(R.id.tv_price)
            TextView tv_price;
            @BindView(R.id.tv_buy)
            TextView tv_buy;
            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
    public void setmListener(FragmentBuyListener mListener) {
        this.mListener = mListener;
    }
    public interface FragmentBuyListener{
        void cancelBuy();
        void showBuy(VipItem item);
    }

}
