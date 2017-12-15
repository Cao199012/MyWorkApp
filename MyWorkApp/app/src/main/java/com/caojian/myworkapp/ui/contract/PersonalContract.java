package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.response.PersonalMsg;

import java.io.File;

/**
 * Created by CJ on 2017/10/29.
 */

public class PersonalContract {
    public interface View{
        void getPersonalSuccess(PersonalMsg personalMsg);
        void changeMsgSuccess(String msg);
        void error(String errorMsg);
    }

    public interface Presenter{
        void getPersonalInfo();
        void changePersonal(PersonalMsg.DataBean personalMsg);
        void uploadHeadPic(File file); //二进制流
    }
}
