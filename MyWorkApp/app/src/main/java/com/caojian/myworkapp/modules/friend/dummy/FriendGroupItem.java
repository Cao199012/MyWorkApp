package com.caojian.myworkapp.modules.friend.dummy;

/**
 * Created by CJ on 2017/9/10.
 */

public class FriendGroupItem {


    /**
     * code : 0
     * message : 成功
     * data : {"groupName":" ","groupAccreditStartTime":" ","groupAccreditEndTime":" ","isAccreditVisible":" "}
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

    public static class DataBean {
        /**
         * groupName :
         * groupAccreditStartTime :
         * groupAccreditEndTime :
         * isAccreditVisible :
         */

        private String groupName;
        private String groupAccreditStartTime;
        private String groupAccreditEndTime;
        private String isAccreditVisible;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupAccreditStartTime() {
            return groupAccreditStartTime;
        }

        public void setGroupAccreditStartTime(String groupAccreditStartTime) {
            this.groupAccreditStartTime = groupAccreditStartTime;
        }

        public String getGroupAccreditEndTime() {
            return groupAccreditEndTime;
        }

        public void setGroupAccreditEndTime(String groupAccreditEndTime) {
            this.groupAccreditEndTime = groupAccreditEndTime;
        }

        public String getIsAccreditVisible() {
            return isAccreditVisible;
        }

        public void setIsAccreditVisible(String isAccreditVisible) {
            this.isAccreditVisible = isAccreditVisible;
        }
    }
}
