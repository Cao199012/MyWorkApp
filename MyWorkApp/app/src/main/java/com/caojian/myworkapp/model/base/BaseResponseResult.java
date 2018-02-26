package com.caojian.myworkapp.model.base;

import java.io.Serializable;

/**
 * Created by CJ on 2017/10/19.
 */

public class BaseResponseResult<T extends BaseResponseResult.UpdateDataBean> {  //通过泛型 来转接受类


    /**
     * code : 0//	0	成功 1	失败 2	Token失效--重新登录账号已经在其他设备登录3	token失效--登录超时
     //	4	版本升级--不需要强制升级
     //	5	版本升级--需要强制升级
     //	6	会员到期
     * message :  成功
     * data : {}
     */


    private int code;
    private String message;
    private T data;


    public int getCode() {
        return code
;
    }
    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static class UpdateDataBean {
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

        public String getMandatory() {
            return mandatory;
        }
        public String getDownLoadAddr() {
            return downLoadAddr;
        }

        public String getComment() {
            return comment;
        }
    }
}
