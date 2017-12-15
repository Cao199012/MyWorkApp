package com.caojian.myworkapp.ui.contract;

/**
 * Created by CJ on 2017/11/4.
 */

public class AddGroupContract {
    public interface View{
        void addSuccess(String msg);
        void addError(String msg);
    }

    public interface Presenter{
        void addGroup(String groupName);
    }
}
