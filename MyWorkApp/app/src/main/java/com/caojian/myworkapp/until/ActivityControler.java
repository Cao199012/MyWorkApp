package com.caojian.myworkapp.until;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录Activity，保证可以随时退出应用并，finish所有Activity
 * Created by CJ on 2017/8/18.
 */

public class ActivityControler {
    private static List<Activity> activityList = new ArrayList<Activity>();

    public static void addActivty(Activity activity)
    {
        if(activityList.contains(activity))
        {
            return;
        }else
        {
            activityList.add(activity);
        }

    }

    public static void removeActivity(Activity activity)
    {
        if(activityList.contains(activity))
        {
            activityList.remove(activity);
        }
    }

    /**
     * 退出应用时调用，不用一个一个后退键返回
     */
    public static void finishActivity(){
        for (Activity activity :
                activityList) {
            activityList.remove(activity);
            activity.finish();
        }
    }

}
