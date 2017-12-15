package com.caojian.myworkapp.model.response;

/**
 * Created by CJ on 2017/10/31.
 */

public class FriendMsg {

    /**
     * code : 0
     * message :  成功
     * data : {"phoneNo":"","headPic":"","nickName":"","remarkName":""}
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
        /**
         * phoneNo :
         * headPic :
         * nickName :
         * remarkName :
         */

        private String phoneNo;
        private String headPic;
        private String nickName;
        private String remarkName;
        private int isFriend;

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getRemarkName() {
            return remarkName;
        }

        public void setRemarkName(String remarkName) {
            this.remarkName = remarkName;
        }

        public int getIsFriend() {
            return isFriend;
        }
    }
}
