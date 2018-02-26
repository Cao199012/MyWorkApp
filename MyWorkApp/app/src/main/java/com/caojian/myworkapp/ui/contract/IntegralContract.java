package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.response.PersonalMsg;

import java.io.File;

/**
 * Created by CJ on 2017/10/29.
 */

public class IntegralContract {
    public interface View{
        void getPersonalSuccess(PersonalMsg personalMsg);
        void transferRewardScoreSuccess(String msg);
        void error(String errorMsg);
    }

    public interface Presenter{
        void getPersonalInfo();
        void transferRewardScoreToFriend(String rewardScore,String friendPhoneNo);
    }
}
