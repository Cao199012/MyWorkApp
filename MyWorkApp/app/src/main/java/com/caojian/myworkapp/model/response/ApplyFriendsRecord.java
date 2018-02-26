package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

import java.util.List;

/**
 * Created by CJ on 2017/10/30.
 */

public class ApplyFriendsRecord extends BaseResponseResult<ApplyFriendsRecord.DataBean> {

    /**
     * code : 0
     * data : {"records":[{"accreditEndTime":"","accreditStartTime":"","authorizedAccreditEndTime":"","authorizedAccreditStartTime":"","createTime":null,"friendNickName":"15651010839","friendPhoneNo":"15651010839","friendRemarkName":"","groupCreateTime":null,"groupId":"","groupName":"","headPic":"","id":0,"isAccreditVisible":0,"isApprove":"0","isAuthorizedVisible":0,"isCheckBeforeExit":0,"modifyTime":null,"phoneNo":"","remarkFirstLetter":""}]}
     * message : 操作成功
     */

    public static class DataBean extends BaseResponseResult.UpdateDataBean{
        private List<RecordsBean> records;

        public List<RecordsBean> getRecords() {
            return records;
        }

        public static class RecordsBean {
            /**
             * accreditEndTime :
             * accreditStartTime :
             * authorizedAccreditEndTime :
             * authorizedAccreditStartTime :
             * createTime : null
             * friendNickName : 15651010839
             * friendPhoneNo : 15651010839
             * friendRemarkName :
             * groupCreateTime : null
             * groupId :
             * groupName :
             * headPic :
             * id : 0
             * isAccreditVisible : 0
             * isApprove : 0
             * isAuthorizedVisible : 0
             * isCheckBeforeExit : 0
             * modifyTime : null
             * phoneNo :
             * remarkFirstLetter :
             */

            private String accreditEndTime;
            private String accreditStartTime;
            private String authorizedAccreditEndTime;
            private String authorizedAccreditStartTime;
            private Object createTime;
            private String friendNickName;
            private String friendPhoneNo;
            private String friendRemarkName;
            private Object groupCreateTime;
            private String groupId;
            private String groupName;
            private String headPic;
            private int id;
            private int isAccreditVisible;
            private String isApprove;
            private int isAuthorizedVisible;
            private int isCheckBeforeExit;
            private Object modifyTime;
            private String phoneNo;
            private String remarkFirstLetter;
            private String applicationId;

            public String getAccreditEndTime() {
                return accreditEndTime;
            }

            public void setAccreditEndTime(String accreditEndTime) {
                this.accreditEndTime = accreditEndTime;
            }

            public String getAccreditStartTime() {
                return accreditStartTime;
            }

            public void setAccreditStartTime(String accreditStartTime) {
                this.accreditStartTime = accreditStartTime;
            }

            public String getAuthorizedAccreditEndTime() {
                return authorizedAccreditEndTime;
            }

            public void setAuthorizedAccreditEndTime(String authorizedAccreditEndTime) {
                this.authorizedAccreditEndTime = authorizedAccreditEndTime;
            }

            public String getAuthorizedAccreditStartTime() {
                return authorizedAccreditStartTime;
            }

            public void setAuthorizedAccreditStartTime(String authorizedAccreditStartTime) {
                this.authorizedAccreditStartTime = authorizedAccreditStartTime;
            }

            public Object getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Object createTime) {
                this.createTime = createTime;
            }

            public String getFriendNickName() {
                return friendNickName;
            }

            public void setFriendNickName(String friendNickName) {
                this.friendNickName = friendNickName;
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

            public Object getGroupCreateTime() {
                return groupCreateTime;
            }

            public void setGroupCreateTime(Object groupCreateTime) {
                this.groupCreateTime = groupCreateTime;
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

            public String getHeadPic() {
                return headPic;
            }

            public void setHeadPic(String headPic) {
                this.headPic = headPic;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIsAccreditVisible() {
                return isAccreditVisible;
            }

            public void setIsAccreditVisible(int isAccreditVisible) {
                this.isAccreditVisible = isAccreditVisible;
            }

            public String getIsApprove() {
                return isApprove;
            }

            public void setIsApprove(String isApprove) {
                this.isApprove = isApprove;
            }

            public int getIsAuthorizedVisible() {
                return isAuthorizedVisible;
            }

            public void setIsAuthorizedVisible(int isAuthorizedVisible) {
                this.isAuthorizedVisible = isAuthorizedVisible;
            }

            public int getIsCheckBeforeExit() {
                return isCheckBeforeExit;
            }

            public void setIsCheckBeforeExit(int isCheckBeforeExit) {
                this.isCheckBeforeExit = isCheckBeforeExit;
            }

            public Object getModifyTime() {
                return modifyTime;
            }

            public void setModifyTime(Object modifyTime) {
                this.modifyTime = modifyTime;
            }

            public String getPhoneNo() {
                return phoneNo;
            }

            public void setPhoneNo(String phoneNo) {
                this.phoneNo = phoneNo;
            }

            public String getRemarkFirstLetter() {
                return remarkFirstLetter;
            }

            public void setRemarkFirstLetter(String remarkFirstLetter) {
                this.remarkFirstLetter = remarkFirstLetter;
            }

            public String getApplicationId() {
                return applicationId;
            }

            public void setApplicationId(String applicationId) {
                this.applicationId = applicationId;
            }

        }
    }
}
