package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.response.RewardScoreDetailMsg;

/**
 * Created by CJ on 2017/11/12.
 */

public class IntegralDetailContract {
    public interface View{
        void getSuccess(RewardScoreDetailMsg.DataBean dataBean);

        void error(String msg);
    }

    public interface Presenter{
        void getIntegralDetail(int pageNum);
    }
}
