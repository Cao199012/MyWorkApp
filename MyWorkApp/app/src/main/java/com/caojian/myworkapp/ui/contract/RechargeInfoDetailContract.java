package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.response.RechargeInfo;
import com.caojian.myworkapp.model.response.RewardScoreDetailMsg;

/**
 * Created by CJ on 2017/11/12.
 */

public class RechargeInfoDetailContract {
    public interface View{
        void getSuccess(RechargeInfo.DataBean dataBean);

        void error(String msg);
    }

    public interface Presenter{
        void getRechargeInfoDetail(int pageNum);
    }
}
