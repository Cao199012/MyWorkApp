package com.caojian.myworkapp.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.caojian.myworkapp.R;

/**
 * Created by caojian on 2017/10/10.
 */

public class SelectHvFragment extends DialogFragment {

    SelectHvKind selectHvKind;
    public static SelectHvFragment newInstance()
    {
        SelectHvFragment fragment = new SelectHvFragment();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.select_hv,null);
        builder.setView(view);
        initView(view);
        return builder.create();
    }

    private void initView(View view) {
        TextView gallery = (TextView) view.findViewById(R.id.select_gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectHvKind.selectGallery();
            }
        });
        TextView camera = (TextView) view.findViewById(R.id.select_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectHvKind.selectCamera();
            }
        });
    }

    public void setSelectHvKind(SelectHvKind kind)
    {
        selectHvKind = kind;
    }

    public interface SelectHvKind{
        void selectGallery();
        void selectCamera();
    }

    public static void showSelectDialog(FragmentManager fm, SelectHvFragment dialogFragment, String tag){
        // SignDialogFragment dialog = SignDialogFragment.newInstance(checkInfo);
        //这里直接调用show方法会报java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        //因为show方法中是通过commit进行的提交(通过查看源码)
        //这里为了修复这个问题，使用commitAllowingStateLoss()方法
        //注意：DialogFragment是继承自android.app.Fragment，这里要注意同v4包中的Fragment区分，别调用串了
        //DialogFragment有自己的好处，可能也会带来别的问题
        //dialog.show(getFragmentManager(), "SignDialog");
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(dialogFragment, tag);
        ft.commitAllowingStateLoss();
    }
}
