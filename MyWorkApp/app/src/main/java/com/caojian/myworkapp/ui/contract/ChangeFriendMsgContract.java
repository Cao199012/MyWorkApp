package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.data.FriendItem;
import com.caojian.myworkapp.model.response.FriendDetailInfo;

/**
 * Created by CJ on 2017/11/1.
 */

public class ChangeFriendMsgContract {
    public interface View{
        void changeSuccess();
        void deleteSuccess(String msg);
        void error(String msg);
        void getFriendInfo(FriendDetailInfo friendDetailInfo);
    }

    public interface Presenter{
        void changeMsg(FriendDetailInfo.DataBean friendsBean);
        void deleteFriend(String phoneNum);
        void getFriendInfo(String phoneNum);
    }
}
