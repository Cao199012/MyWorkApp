package com.caojian.myworkapp.modules.friend.dummy;

/**
 * Created by CJ on 2017/7/31.
 */

public class FriendItem {
    private String name = "";
    /**
     * code : ‘0’
     * message : ‘成功’
     * data : {"phoneNo":"\u2018\u2019","headPic":"\u2018\u2019","remarkFirstLetter":"\u2018\u2019"}
     */

    private String code;
    private String message;
    private DataBean data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
         * phoneNo : ‘’
         * headPic : ‘’
         * remarkFirstLetter : ‘’
         */

        private String phoneNo = "";
        private String headPic = "";
        private String remarkFirstLetter = "";

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

        public String getRemarkFirstLetter() {
            return remarkFirstLetter;
        }

        public void setRemarkFirstLetter(String remarkFirstLetter) {
            this.remarkFirstLetter = remarkFirstLetter;
        }
    }
}
