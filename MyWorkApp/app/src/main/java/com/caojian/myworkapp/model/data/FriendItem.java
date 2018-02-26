package com.caojian.myworkapp.model.data;



import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.FriendDetailInfo;

import java.util.List;

/**
 * Created by CJ on 2017/7/31.
 */

public class FriendItem extends BaseResponseResult<FriendItem.DataBean>{

    /**
     * code : 0
     * message :  成功
     * data : {"friends":[{"friendPhoneNo":"","headPic":""},{"friendPhoneNo":"","headPic":""}]}
     */

    public static class DataBean extends BaseResponseResult.UpdateDataBean{
        private List<FriendDetailInfo.DataBean> friends;

        public List<FriendDetailInfo.DataBean> getFriends() {
            return friends;
        }

        public static class FriendsBean {
            /**
             * friendPhoneNo :
             * headPic :
             */
            private String friendPhoneNo;
            private String headPic;
            private String remarkFirstLetter;
            private String friendRemarkName; //备注
            private String friendNickName; //昵称

            public String getFriendPhoneNo() {
                return friendPhoneNo;
            }

            public void setFriendPhoneNo(String friendPhoneNo) {
                this.friendPhoneNo = friendPhoneNo;
            }
            public String getHeadPic() {
                return headPic;
            }
            public String getRemarkFirstLetter() {
                return remarkFirstLetter;
            }

            public void setRemarkFirstLetter(String remarkFirstLetter) {
                this.remarkFirstLetter = remarkFirstLetter;
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
        }
    }
}
