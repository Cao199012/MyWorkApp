package com.caojian.myworkapp.model.response;

/**
 * Created by CJ on 2017/11/5.
 */

public class FriendDetailInfo {
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
        /** 手机号 */
        private String phoneNo;
        /** 好友手机号 */
        private String friendPhoneNo;
        /** 好友备注名 */
        private String friendRemarkName;
        /** 好友昵称 */
        private String friendNickName;
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
        /** 是否退出前查看 1:是,2:否 */
        private Integer isCheckBeforeExit;
        /** 头像ID */
        private String headPic;
        /** 首字母 */
        private String remarkFirstLetter;
        /** 是否同意，0：申请中、1：是、2：否 */
        private String isApprove;


        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getFriendPhoneNo() {
            return friendPhoneNo;
        }

        public void setFriendPhoneNo(String friendPhoneNo) {
            this.friendPhoneNo = friendPhoneNo;
        }

        public String getFriendRemarkName() {
            return friendRemarkName;
        }

        public void setFriendRemarkName(String friendRemarkName) {
            this.friendRemarkName = friendRemarkName;
        }

        public String getFriendNickName() {
            return friendNickName;
        }

        public void setFriendNickName(String friendNickName) {
            this.friendNickName = friendNickName;
        }

        public Integer getIsAccreditVisible() {
            return isAccreditVisible;
        }

        public void setIsAccreditVisible(Integer isAccreditVisible) {
            this.isAccreditVisible = isAccreditVisible;
        }

        public String getAccreditStartTime() {
            return accreditStartTime;
        }

        public void setAccreditStartTime(String accreditStartTime) {
            this.accreditStartTime = accreditStartTime;
        }

        public String getAccreditEndTime() {
            return accreditEndTime;
        }

        public void setAccreditEndTime(String accreditEndTime) {
            this.accreditEndTime = accreditEndTime;
        }

        public String getAccreditWeeks() {
            return accreditWeeks;
        }

        public void setAccreditWeeks(String accreditWeeks) {
            this.accreditWeeks = accreditWeeks;
        }

        public Integer getIsAuthorizedVisible() {
            return isAuthorizedVisible;
        }

        public void setIsAuthorizedVisible(Integer isAuthorizedVisible) {
            this.isAuthorizedVisible = isAuthorizedVisible;
        }

        public String getAuthorizedAccreditStartTime() {
            return authorizedAccreditStartTime;
        }

        public void setAuthorizedAccreditStartTime(String authorizedAccreditStartTime) {
            this.authorizedAccreditStartTime = authorizedAccreditStartTime;
        }

        public String getAuthorizedAccreditEndTime() {
            return authorizedAccreditEndTime;
        }

        public void setAuthorizedAccreditEndTime(String authorizedAccreditEndTime) {
            this.authorizedAccreditEndTime = authorizedAccreditEndTime;
        }

        public String getAuthorizedWeeks() {
            return authorizedWeeks;
        }

        public void setAuthorizedWeeks(String authorizedWeeks) {
            this.authorizedWeeks = authorizedWeeks;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public Integer getIsCheckBeforeExit() {
            return isCheckBeforeExit;
        }

        public void setIsCheckBeforeExit(Integer isCheckBeforeExit) {
            this.isCheckBeforeExit = isCheckBeforeExit;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getRemarkFirstLetter() {
            return remarkFirstLetter;
        }

        public void setRemarkFirstLetter(String remarkFirstLetter) {
            this.remarkFirstLetter = remarkFirstLetter;
        }

        public String getIsApprove() {
            return isApprove;
        }

        public void setIsApprove(String isApprove) {
            this.isApprove = isApprove;
        }
    }
}
