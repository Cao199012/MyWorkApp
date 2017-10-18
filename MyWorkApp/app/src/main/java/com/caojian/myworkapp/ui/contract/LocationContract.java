package com.caojian.myworkapp.ui.contract;

import com.caojian.myworkapp.model.data.LocationItem;

import java.util.List;

/**
 * Created by caojian on 2017/8/31.
 */

public class LocationContract {
    public interface View{
        void showPeople(List<LocationItem> peopleList);

    }

    public interface Presenter{
        void updateLoxation(long la,long lo);
        void getPeopleLocation();
    }
}
