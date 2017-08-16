package com.caojian.myworkapp.until;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by CJ on 2017/7/28.
 */

public class ActivityUntil {
    private static Toast toast;

    /**
     * @param context
     * @param msg
     * @param duration
     */
    public static void showToast(Context context,String msg,int duration)
    {
        if (toast == null)
        {
            toast = Toast.makeText(context,msg,duration);
        }else {
            toast.setText(msg);
            toast.setDuration(duration);
        }
        toast.show();
    }

    public static void disToast()
    {
        if(toast != null)
        {
            toast.cancel();
            toast = null;
        }
    }
}
