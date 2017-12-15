package com.caojian.myworkapp.ui.contract;

/**
 * Created by CJ on 2017/11/12.
 */

public class LocationDetailContact {
    public interface View{
        void cancelSuccess(String msg);
        void error(String msg);
    }

    public interface Presenter {
        void cancelLocation(String phoneNo);
    }
}
