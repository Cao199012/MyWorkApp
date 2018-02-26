package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

import java.util.List;

/**
 * Created by CJ on 2017/11/10.
 */

public class DrawInfo extends BaseResponseResult<DrawInfo.DataBean> { ///体现记录

    /**
     * code : 0
     * message :  成功
     * data : {"money":"","detailInfo":"","createTime":""}
     */

    public static class DataBean extends BaseResponseResult.UpdateDataBean{
        private int pageCount;  //总页数
        private int pageNumber; //返回数据的格式
        List<DetailsBean> details;

        public int getPageCount() {
            return pageCount;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }
    }

    public static class DetailsBean {
        private String phoneNo;// 手机号
        private String bankName;// 银行名称
        private String bankNumber;// 银行卡号
        private String name;// 开户人姓名
        private String bankBranch;// 开户行支行
        private Double rewardScore;// 积分
        private String remark;// 备注
        private Integer status;// 状态 0:待处理,1:提现成功,2:提现失败
        private String createTime;

        public String getPhoneNo() {
            return phoneNo;
        }

        public String getBankName() {
            return bankName;
        }

        public String getBankNumber() {
            return bankNumber;
        }

        public String getName() {
            return name;
        }

        public String getBankBranch() {
            return bankBranch;
        }

        public Double getRewardScore() {
            return rewardScore;
        }

        public String getRemark() {
            return remark;
        }

        public Integer getStatus() {
            return status;
        }

        public String getCreateTime() {
            return createTime;
        }
    }
}
