package com.caojian.myworkapp.ui.contract;

/**
 * Created by CJ on 2017/10/26.
 */

public class TixianContract {

    public interface View{
        void submitSuccess();
        void verityCodeSuccess();
        void error(String errorMsg);
    }

    public interface Presenter{
        void submitMsg(int rewardScore, String cardName, String cardNum, String bankName,String bankBranch);
        void verityCode(String phone, String imgCode);
    }
}
