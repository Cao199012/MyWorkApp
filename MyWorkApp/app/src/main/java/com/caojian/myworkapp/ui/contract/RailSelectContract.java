package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.response.RailHistoryMsg;

import java.util.List;

/**
 * Created by CJ on 2017/12/17.
 */

public class RailSelectContract {
    public interface View{
        void getSuccess(List<RailHistoryMsg.PositionsBean> positions);
        void getError(String msg);
    }

    public interface Presenter{
        void getRailHistory(String phoneNo,String startTime,String endTime);
    }
}
