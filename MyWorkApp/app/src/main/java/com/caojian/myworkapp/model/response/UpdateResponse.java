package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

import java.io.Serializable;

/**
 * Created by CJ on 2017/8/17.
 */

public class UpdateResponse extends BaseResponseResult {

    /**
     * code : 000000
     * message : 成功
     * data : {"isUpdate":"1","mandatory":"1","downLoadAddr":"","comment":""}
     */



    private DataBean data;


//    public DataBean getData() {
//        return data;
//    }

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
