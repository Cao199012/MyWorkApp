package com.caojian.myworkapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.caojian.myworkapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CJ on 2017/9/8.
 */

public class BuyModelFragment extends AppCompatDialogFragment  {

    public static BuyModelFragment newInstance()
    {
        BuyModelFragment fragment = new BuyModelFragment();

       // fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_model_list,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {

    }


    @Override
    public void onAttach(Context context) {
//        if(context instanceof FragmentDialogListener)
//        {
//            mListrner = (FragmentDialogListener) context;
//        }else {
//            throw new RuntimeException("activity必须implements接口");
//        }
         super.onAttach(context);
    }


}
