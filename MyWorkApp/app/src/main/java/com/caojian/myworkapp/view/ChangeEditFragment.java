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

public class ChangeEditFragment extends AppCompatDialogFragment  {

    public static ChangeEditFragment newInstance(String title, String comments, String cancel, String sure)
    {
        ChangeEditFragment fragment = new ChangeEditFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("comments",comments);
        bundle.putString("cancel",cancel);
        bundle.putString("sure",sure);
        fragment.setArguments(bundle);
        return fragment;
    }
    @BindView(R.id.title_change_fragment)
    TextView mChange_title;
    @BindView(R.id.comments_change_fragment)
    TextView mChange_comments;
    @BindView(R.id.btn_cancel)
    TextView mChange_cancel;
    @BindView(R.id.btn_sure)
    TextView mChange_sure;

    private FragmentChangeListener mListrner;
    private String title,comments,cancel,sure;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            title = getArguments().getString("title","");
            comments = getArguments().getString("comments","");
            cancel = getArguments().getString("cancel","");
            sure = getArguments().getString("sure","");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_dialog,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        mChange_title.setText(title);
        mChange_comments.setText(comments);
        mChange_cancel.setText(cancel);
        mChange_sure.setText(sure);
        Log.i("title",title);
        mChange_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListrner.cancelEdit();
            }
        });
        mChange_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListrner.sumbitEdit();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        if(context instanceof FragmentChangeListener)
        {
            mListrner = (FragmentChangeListener) context;
        }else {
            throw new RuntimeException("activity必须implements接口");
        }
        super.onAttach(context);
    }

    public interface FragmentChangeListener{
        void cancelEdit();
        void sumbitEdit();
    }
}
