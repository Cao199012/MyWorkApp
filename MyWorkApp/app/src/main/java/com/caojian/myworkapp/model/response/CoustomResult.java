package com.caojian.myworkapp.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CJ on 2017/10/19.
 */

public class CoustomResult {


    @SerializedName("“retcode”")
    private String retcode; // FIXME check this code
    @SerializedName("“retinfo”")
    private String retinfo; // FIXME check this code

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getRetinfo() {
        return retinfo;
    }

    public void setRetinfo(String retinfo) {
        this.retinfo = retinfo;
    }
}
