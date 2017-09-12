package com.caojian.myworkapp.modules.update;

import java.io.Serializable;

/**
 * Created by CJ on 2017/8/17.
 */

public class UpdateMsg {

    /**
     * code : 000000
     * message : 成功
     * data : {"isUpdate":"1","mandatory":"1","downLoadAddr":"","comment":""}
     */

    private String code;
    private String message;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public static class DataBean implements Serializable{
        /**
         * isUpdate : 1
         * mandatory : 1
         * downLoadAddr :
         * comment :
         */

        private String isUpdate;
        private String mandatory;
        private String downLoadAddr;
        private String comment;

        public String getIsUpdate() {
            return isUpdate;
        }

        public void setIsUpdate(String isUpdate) {
            this.isUpdate = isUpdate;
        }

        public String getMandatory() {
            return mandatory;
        }

        public void setMandatory(String mandatory) {
            this.mandatory = mandatory;
        }

        public String getDownLoadAddr() {
            return downLoadAddr;
        }

        public void setDownLoadAddr(String downLoadAddr) {
            this.downLoadAddr = downLoadAddr;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}