package com.caojian.myworkapp.ui.contract;

/**
 * Created by CJ on 2017/10/31.
 */

public class AddByPhoneContrct {
    public interface View{
        void checkSuccess();
        void addSuccess(String msg);
        void error(String msg);
        void checkFail(String msg);
    }

    public interface Presenter{
        void checkPhone(String phone);
        void addFriend(String phone,String applyInfo);
    }
}
