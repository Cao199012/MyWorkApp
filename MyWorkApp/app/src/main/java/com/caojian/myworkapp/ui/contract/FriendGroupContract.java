package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.data.FriendItem;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;

import java.util.List;

/**
 * Created by CJ on 2017/7/31.
 */

public class FriendGroupContract {
    public interface View{
        void onSuccess(List<FriendsAndGroupsMsg.DataBean.GroupsBean> Groups);
        void onFailed(String errorMsg);
    }

    public interface Presenter{
        void getFriendGroup();
    }
}
