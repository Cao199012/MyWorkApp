package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.Request.PositionInfoDto;
import com.caojian.myworkapp.model.data.LocationItem;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;

import java.util.List;

/**
 * Created by caojian on 2017/8/31.
 */

public class LocationContract {
    public interface View{
        void showPeopleList(List<LocationItem> peopleList);
        void showPeopleBeforeExit( List<PositionInfoDto> peopleList);
        void error(String msg);
    }
    public interface Presenter{
        void getPeopleLocation(String phoneNo);
        //void getPeopleLocation(List<PositionInfoDto> phoneNo);
        void getGroupLocation(String groupId);
        void getPositionsBeforeExit();
    }
}
