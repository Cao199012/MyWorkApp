package com.caojian.myworkapp.check;

/**
 * Created by CJ on 2017/8/20.
 */

public class CheckContract {

    interface View{
        void checkResultError(String result);
        void checkResultSuccess();
    }

    interface Presenter{
        void checkPhone(String phone);
    }
}
