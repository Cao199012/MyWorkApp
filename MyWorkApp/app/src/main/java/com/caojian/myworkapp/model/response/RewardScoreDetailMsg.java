package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

import java.util.List;

/**
 * Created by CJ on 2017/11/10.
 */

public class RewardScoreDetailMsg extends BaseResponseResult<RewardScoreDetailMsg.DataBean> {

    /**
     * code : 0
     * message :  成功
     * data : {"totalRewardScore":"","pageCount":"","pageNumber":"","details":[{"rewardScoreType":"","detailInfo":"","rewardScore":""},{"rewardScoreType":"","detailInfo":"","rewardScore":""}]}
     */


    public static class DataBean extends BaseResponseResult.UpdateDataBean{
        /**
         * totalRewardScore :
         * pageCount :
         * pageNumber :
         * details : [{"rewardScoreType":"","detailInfo":"","rewardScore":""},{"rewardScoreType":"","detailInfo":"","rewardScore":""}]
         */

        private String totalRewardScore;
        private String pageCount;
        private String pageNumber;
        private List<DetailsBean> details;

        public String getTotalRewardScore() {
            return totalRewardScore;
        }

        public void setTotalRewardScore(String totalRewardScore) {
            this.totalRewardScore = totalRewardScore;
        }

        public String getPageCount() {
            return pageCount;
        }

        public void setPageCount(String pageCount) {
            this.pageCount = pageCount;
        }

        public String getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(String pageNumber) {
            this.pageNumber = pageNumber;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

        public static class DetailsBean {

            private String rewardScoreType;   //1:购买会员,2:推荐奖励,3:提现,4:转增,5:获赠
            private String detailInfo;
            private String rewardScore;
            private String createTime;

            public String getRewardScoreType() {
                return rewardScoreType;
            }

            public void setRewardScoreType(String rewardScoreType) {
                this.rewardScoreType = rewardScoreType;
            }

            public String getDetailInfo() {
                return detailInfo;
            }

            public void setDetailInfo(String detailInfo) {
                this.detailInfo = detailInfo;
            }

            public String getRewardScore() {
                return rewardScore;
            }

            public void setRewardScore(String rewardScore) {
                this.rewardScore = rewardScore;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }
    }
}
