package com.caojian.myworkapp.check;

/**
 * Created by CJ on 2017/8/20.
 */

public class CheckMsg {

    /**
     * retcode : 000000
     * retinfo :  成功
     * data : {}
     */

    private String retcode;
    private String retinfo;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
    }
}
