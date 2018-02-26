package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

import java.util.List;

/**
 * Created by CJ on 2017/11/10.
 */

public class RechargeInfo extends BaseResponseResult<RechargeInfo.DataBean> {

    /**
     * code : 0
     * message :  成功
     * data : {"amount":"","detailInfo":"","createTime":""}
     */


    public static class DataBean extends BaseResponseResult.UpdateDataBean{
        /**
         * amount :
         * detailInfo :
         * createTime :
         */
        private int pageCount;  //总页数
        private int pageNumber; //返回数据的格式
        private List<DetailsBean> details;

        public int getPageCount() {
            return pageCount;
        }
        public int getPageNumber() {
            return pageNumber;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public static class DetailsBean {
            private String amount;
            private int type; //详情
            private String createTime;


            public String getAmount() {
                return amount;
            }
            public int getDetailInfo() {
                return type;
            }
            public String getCreateTime() {
                return createTime;
            }
        }
    }


}
