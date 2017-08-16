package com.caojian.myworkapp.login;

/**
 * Created by CJ on 2017/7/28.
 */

public class LoginContract {
    public interface View{
        void LoginSuccess();
        void LoginError(String errorMsg);
    }

    public interface Presenter{
        void checkLogin(String name,String password);
    }
}
