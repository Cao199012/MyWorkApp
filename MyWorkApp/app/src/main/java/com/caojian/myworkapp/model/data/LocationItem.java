package com.caojian.myworkapp.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by caojian on 2017/8/31.
 */

public class LocationItem implements Parcelable{

    /**
     * longitude :
     * latitude :
     * headPic :
     */

    private String longitude;
    private String latitude;
    private String headPic;
    private String phoneNo;

    public LocationItem(){

    }
    protected LocationItem(Parcel in) {
        longitude = in.readString();
        latitude = in.readString();
        headPic = in.readString();
        phoneNo = in.readString();
    }

    public static final Creator<LocationItem> CREATOR = new Creator<LocationItem>() {
        @Override
        public LocationItem createFromParcel(Parcel in) {
            return new LocationItem(in);
        }

        @Override
        public LocationItem[] newArray(int size) {
            return new LocationItem[size];
        }
    };

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(headPic);
        dest.writeString(phoneNo);
    }
}
