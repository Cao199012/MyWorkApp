package com.caojian.myworkapp.friend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

import com.caojian.myworkapp.R;

/**
 * Created by CJ on 2017/7/31.
 */

public abstract class SectionDecoration extends RecyclerView.ItemDecoration {

    private Paint paint,textPaint;
    private int topHeight;
    public SectionDecoration(Context pContext){
        topHeight = 70;
        paint = new Paint();
        paint.setColor(pContext.getResources().getColor(R.color.colorPrimary));
        textPaint = new TextPaint();
        textPaint.setColor(pContext.getResources().getColor(R.color.colorAccent));
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(80);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.LEFT);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if(isGroupHead(position))
        {
            outRect.top = topHeight;
        }else
        {
            outRect.top = 0;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        float left = parent.getPaddingLeft();
        float right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View view = parent.getChildAt(i);
            float top = view.getTop();
            float bottom = view.getTop() - topHeight;
            int position = parent.getChildAdapterPosition(view);
            if(isGroupHead(position))
            {
                c.drawRect(left,top,right,bottom,paint);
                c.drawText(getGroupId(position),left,top,textPaint);
            }
        }


    }

    private boolean isGroupHead(int position)
    {
        if(position == 0)
        {
            return true;
        }else
        {
            String pre = getGroupId(position -1);
            String now = getGroupId(position);
            return !pre.equals(now);
        }
    }
  //  public static int
    public abstract String getGroupId(int position);
}
