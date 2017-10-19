package com.caojian.myworkapp.model.response;

/**
 * Created by CJ on 2017/8/22.
 */

public class RegisterMsg {

    /**
     * retcode : 0
     * retinfo :  成功
     * token : 1*********12
     * contactsflag :
     */

    private String retcode;
    private String retinfo;
    private String token;
    private String contactsflag;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContactsflag() {
        return contactsflag;
    }

    public void setContactsflag(String contactsflag) {
        this.contactsflag = contactsflag;
    }
}
