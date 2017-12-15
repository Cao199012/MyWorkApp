package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.GroupInfo;

/**
 * Created by CJ on 2017/11/1.
 */

public class ChangeGroupMsgContract {
    public interface View{
        void changeSuccess(int kindCode); //kindCode: 1:修改成功，2：添加成员：3：删除成员
        void deleteSuccess(String msg);
        void error(String msg);
        void getInfoSuccess(GroupInfo groupDetailInfo);
    }

    public interface Presenter{
        void changeMsg(GroupInfo.DataBean groupInfoBean);
        void deleteGroup(String groupId);
        void getGroupInfo(String groupId);
        void addFriend2Group(String groupId,String friendId);
        void removeFriendFromGroup(String groupId,String friendId);
    }
}
