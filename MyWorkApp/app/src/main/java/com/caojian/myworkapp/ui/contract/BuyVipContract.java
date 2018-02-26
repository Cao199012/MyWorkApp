package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.response.PersonalMsg;

/**
 * Created by CJ on 2017/11/23.
 */

public class BuyVipContract {
    public interface View{
        void getOrderNumSuccess(String orderNum,String amount);
        void getOrderTimeSuccess(String startTime,String endtime);
        void getPersonalSuccess(PersonalMsg personalMsg);
        void getPersonalError(String msg);
        void buySuccess(String msg);
        void error(String errorMsg);
    }

    public interface Presenter{
        void getOrderNum(String vipType,String validityDurationType);
        void buyVipByJiFen(String vipType);
        void buyValueAddedServiceByRewardScore(String kindType); //积分购买增值
    }
}

