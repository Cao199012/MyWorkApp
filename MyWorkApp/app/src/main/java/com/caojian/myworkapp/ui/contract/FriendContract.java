package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.data.FriendItem;
import com.caojian.myworkapp.model.response.FriendDetailInfo;

import java.util.List;

/**
 * Created by CJ on 2017/7/31.
 */

public class FriendContract {
    public interface View{
        void onSuccess(List<FriendDetailInfo.DataBean> friends);
        void onFailed(String errorMsg);
    }

    public interface Presenter{
        void getFriends();
    }
}
