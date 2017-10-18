package com.caojian.myworkapp.ui.contract;

/**
 * Created by CJ on 2017/8/20.
 */

public class CheckContract {

    public interface View{
        void checkResultError(String result);
        void checkResultSuccess();
    }

    public interface Presenter{
        void checkPhone(String phone);
    }
}
