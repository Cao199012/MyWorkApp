package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.response.ApplyFriendsRecord;

import java.util.List;

/**
 * Created by CJ on 2017/10/31.
 */

public class ApplyRecordContract {
    public interface View{
        void getRecords(List<ApplyFriendsRecord.DataBean.RecordsBean> recordsBeanList);
        void agreeSuccess();
        void error(String msg);
    }

    public interface Presenter{
        void getRecords();
        void agreeApply(ApplyFriendsRecord.DataBean.RecordsBean recordsBean);
    }


}
