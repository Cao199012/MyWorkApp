package com.caojian.myworkapp.ui.contract;

/**
 * Created by CJ on 2017/10/26.
 */

public class PasswordContract {
    public interface View{
        void verityCodeSuccess();
        void changePasswordSuccess();
        void requestError(String errorMsg);
    }

    public interface Presenter{
        void checkPassword(String phone,String verificationCode,String password);
        void verityCode(String phone,String imgCode);
    }
}
