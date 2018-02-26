package com.caojian.myworkapp.model.Request;

import java.io.Serializable;

/**
 * Created by CJ on 2017/11/25.
 */

public class PositionInfoDto implements Serializable {
    private String phoneNo;
    private String modifyTime = null;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}
