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
        void submitTixianMsg(int rewardScore, String cardName, String cardNum, String bankName, String verCode);
        void submitGiveMsg(String rewardScore, String friendNo, String verCode);
        void verityCode(String phone, String imgCode,int type);
    }
}
