package com.caojian.myworkapp.widget;

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

public class MyNewDialogFragment extends AppCompatDialogFragment  {

    public static MyNewDialogFragment newInstance(String title, String comments, String cancel, String sure)
    {
        MyNewDialogFragment fragment = new MyNewDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("comments",comments);
        bundle.putString("newCancel",cancel);
        bundle.putString("sure",sure);
        fragment.setArguments(bundle);
        return fragment;
    }
    @BindView(R.id.dialog_title)
    TextView mDialog_title;
    @BindView(R.id.dialog_comments)
    TextView mDialog_comments;
    @BindView(R.id.btn_cancel)
    Button mDialog_cancel;
    @BindView(R.id.btn_sure)
    Button mDialog_sure;

    private FragmentDialogListener mListrner;
    private String title,comments,cancel,sure;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            title = getArguments().getString("title","");
            comments = getArguments().getString("comments","");
            cancel = getArguments().getString("newCancel","");
            sure = getArguments().getString("sure","");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        mDialog_title.setText(title);
        mDialog_comments.setText(comments);
        mDialog_cancel.setText(cancel);
        mDialog_sure.setText(sure);
        Log.i("title",title);
        mDialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListrner.newCancel();
            }
        });
        mDialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListrner.newSure();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        if(context instanceof FragmentDialogListener)
        {
            mListrner = (FragmentDialogListener) context;
        }else {
          //  throw new RuntimeException("activity必须implements接口");
        }
        super.onAttach(context);
    }



    public interface FragmentDialogListener{
        void newCancel();
        void newSure();
    }
}
