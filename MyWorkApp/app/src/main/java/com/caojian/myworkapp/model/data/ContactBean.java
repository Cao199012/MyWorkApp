package com.caojian.myworkapp.model.data;

/**
 * Created by caojian on 2017/10/7.
 */

public class ContactBean {

    String name;
    String phonenum;
    int status; //0 不是好友 1 是好友

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
