package com.caojian.myworkapp.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
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
}
