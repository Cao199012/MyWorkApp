package com.caojian.myworkapp.ui.contract;

/**
 * Created by CJ on 2017/10/26.
 */

public class RegisterContract {

    public interface View{
        void registerSuccess();
        void verityCodeSuccess();
        void registerError(String errorMsg);
    }

    public interface Presenter{
        void checkRegister(String phone,String verificationCode,String password,String invitationCode);
        void verityCode(String phone,String imgCode);
    }
}
