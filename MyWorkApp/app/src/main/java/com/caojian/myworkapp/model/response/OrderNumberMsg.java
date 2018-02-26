package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

/**
 * Created by CJ on 2017/11/10.
 */

public class OrderNumberMsg extends BaseResponseResult<OrderNumberMsg.DataBean> {


    /**
     * code : 0
     * message :  成功
     * data : {"orderNumber":""}
     */


    public static class DataBean extends BaseResponseResult.UpdateDataBean{
        /**
         * orderNumber :
         */
        private String amount;
        private String orderNumber;

        public String getOrderNumber() {
            return orderNumber;
        }

        public String getAmount() {
            return amount;
        }
    }
}
