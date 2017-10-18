package com.caojian.myworkapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.caojian.myworkapp.R;

/**
 * Created by CJ on 2017/9/9.
 */

public class LineDecoration extends RecyclerView.ItemDecoration{

    private int lineHeight = 0;
    private int marignLeft = 0;
    public LineDecoration(Context context){
        lineHeight = context.getResources().getDimensionPixelSize(R.dimen.recyclerView_line_height);
        marignLeft = context.getResources().getDimensionPixelSize(R.dimen.text_margin);
    }
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //outRect =
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = lineHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }
}
