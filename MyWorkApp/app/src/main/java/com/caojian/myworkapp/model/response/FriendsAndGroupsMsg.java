package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CJ on 2017/11/4.
 */

public class FriendsAndGroupsMsg extends BaseResponseResult<FriendsAndGroupsMsg.DataBean> {

    /**
     * code : 0
     * message :  成功
     * data : {"friends":[{"friendPhoneNo":"","headPic":""},{"friendPhoneNo":"","headPic":""}],"groups":[{"groupId":"","groupName":"","friends":[{"friendPhoneNo":"","headPic":""},{"friendPhoneNo":"","headPic":""}]},{"groupId":"","groupName":"","friends":[{"friendPhoneNo":"","headPic":""},{"friendPhoneNo":"","headPic":""}]}]}
     */


    public static class DataBean extends BaseResponseResult.UpdateDataBean{
        private List<FriendDetailInfo.DataBean> friends;
        private List<GroupsBean> groups;

        public List<FriendDetailInfo.DataBean> getFriends() {
            return friends;
        }

        public void setFriends(List<FriendDetailInfo.DataBean> friends) {
            this.friends = friends;
        }

        public List<GroupsBean> getGroups() {
            return groups;
        }

        public void setGroups(List<GroupsBean> groups) {
            this.groups = groups;
        }

        public static class GroupsBean implements Serializable {
            /**
             * groupId :
             * groupName :
             * friends : [{"friendPhoneNo":"","headPic":""},{"friendPhoneNo":"","headPic":""}]
             */

            private String groupId;
            private String groupName;
            private Integer isCheckBeforeExit;
            private List<FriendDetailInfo.DataBean> friends;

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

            public List<FriendDetailInfo.DataBean> getFriends() {
                return friends;
            }

            public void setFriends(List<FriendDetailInfo.DataBean> friends) {
                this.friends = friends;
            }

            public Integer getIsCheckBeforeExit() {
                return isCheckBeforeExit;
            }

            public void setIsCheckBeforeExit(Integer isCheckBeforeExit) {
                this.isCheckBeforeExit = isCheckBeforeExit;
            }
        }
    }
}
