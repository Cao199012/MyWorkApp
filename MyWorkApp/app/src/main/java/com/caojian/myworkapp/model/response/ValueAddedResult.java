package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

/**
 * Created by CJ on 2017/10/19.
 */

public class ValueAddedResult extends BaseResponseResult<ValueAddedResult.DataBean> {


    /**
     * code : 0
     * message :  成功
     * data : {}
     */


    public static class DataBean extends BaseResponseResult.UpdateDataBean{
        private String startTime;
        private String endTime;

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }
    }
}
