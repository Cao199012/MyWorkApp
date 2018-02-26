package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.Request.PositionInfoDto;
import com.caojian.myworkapp.model.data.LocationItem;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;

import java.util.List;
import java.util.Map;

/**
 * Created by caojian on 2017/8/31.
 */

public class LocationContract {
    public interface View{
        void showPeopleList(List<LocationItem> peopleList);
        void showPeopleBeforeExit( List<PositionInfoDto> peopleList,Map mapPic,Map mapReMarkName);
        void setFriendsVisibleBeforeExitingSuccess(int code);
        void error(String msg);
    }
    public interface Presenter{
        void getPeopleLocation(String phoneNo);
        //void getPeopleLocation(List<PositionInfoDto> phoneNo);
        void setFriendsVisibleBeforeExiting(String phoneNos);
        void getPositionsBeforeExit();
    }
}
