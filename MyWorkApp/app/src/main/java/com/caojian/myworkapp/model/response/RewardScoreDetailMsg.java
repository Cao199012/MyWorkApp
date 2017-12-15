package com.caojian.myworkapp.model.response;

import java.util.List;

/**
 * Created by CJ on 2017/11/10.
 */

public class RewardScoreDetailMsg {

    /**
     * code : 0
     * message :  成功
     * data : {"totalRewardScore":"","pageCount":"","pageNumber":"","details":[{"rewardScoreType":"","detailInfo":"","rewardScore":""},{"rewardScoreType":"","detailInfo":"","rewardScore":""}]}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
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
            /**
             * rewardScoreType :
             * detailInfo :
             * rewardScore :
             */

            private String rewardScoreType;
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
