package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

import java.util.List;

/**
 * Created by CJ on 2017/11/4.
 */

public class GroupInfo extends BaseResponseResult<GroupInfo.DataBean> {

    /**
     * code : 0
     * message :  成功
     * data : {"groupName":"","groupAccreditStartTime":"","groupAccreditEndTime":"","isAccreditVisible":""}
     */


    public static class DataBean extends BaseResponseResult.UpdateDataBean{

        /** 是否授权可见 1:是,2:否 */
        private Integer isAccreditVisible;
        /** 授权开始时间 */
        private String accreditStartTime;
        /** 授权结束时间 */
        private String accreditEndTime;
        /** 授权日期 */
        private String accreditWeeks;
        /** 是否被授权可见 1:是,2:否 */
        private Integer isAuthorizedVisible;
        /** 被授权开始时间 */
        private String authorizedAccreditStartTime;
        /** 被授权结束时间 */
        private String authorizedAccreditEndTime;
        /** 被授权日期 */
        private String authorizedWeeks;
        /** 组id */
        private String groupId;
        /** 组名称 */
        private String groupName;
        private List<FriendDetailInfo.DataBean> friends;

        public Integer getIsAccreditVisible() {
            return isAccreditVisible;
        }

        public String getAccreditStartTime() {
            return accreditStartTime;
        }

        public String getAccreditEndTime() {
            return accreditEndTime;
        }

        public String getAccreditWeeks() {
            return accreditWeeks;
        }

        public Integer getIsAuthorizedVisible() {
            return isAuthorizedVisible;
        }

        public String getAuthorizedAccreditStartTime() {
            return authorizedAccreditStartTime;
        }

        public String getAuthorizedAccreditEndTime() {
            return authorizedAccreditEndTime;
        }

        public String getAuthorizedWeeks() {
            return authorizedWeeks;
        }

        public String getGroupId() {
            return groupId;
        }

        public String getGroupName() {
            return groupName;
        }
        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
        public List<FriendDetailInfo.DataBean> getFriends() {
            return friends;
        }

        public void setIsAccreditVisible(Integer isAccreditVisible) {
            this.isAccreditVisible = isAccreditVisible;
        }

        public void setAccreditStartTime(String accreditStartTime) {
            this.accreditStartTime = accreditStartTime;
        }

        public void setAccreditEndTime(String accreditEndTime) {
            this.accreditEndTime = accreditEndTime;
        }

        public void setAccreditWeeks(String accreditWeeks) {
            this.accreditWeeks = accreditWeeks;
        }

        public void setAuthorizedWeeks(String authorizedWeeks) {
            this.authorizedWeeks = authorizedWeeks;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public void setFriends(List<FriendDetailInfo.DataBean> friends) {
            this.friends = friends;
        }
    }
}
