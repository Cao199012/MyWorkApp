package com.caojian.myworkapp.model.response;

/**
 * Created by CJ on 2017/8/20.
 */

public class CheckMsg {

    /**
     * code : 0
     * message :  成功
     * data : {"token":""}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
