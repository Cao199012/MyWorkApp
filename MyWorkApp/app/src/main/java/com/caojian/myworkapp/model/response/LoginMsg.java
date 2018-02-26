package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

/**
 * Created by CJ on 2017/8/22.
 */

public class LoginMsg extends BaseResponseResult<LoginMsg.DataBean> {


    /**
     * code : 000000
     * message :  成功
     * data : {"token":""}
     */

    public static class DataBean extends BaseResponseResult.UpdateDataBean{
        /**
         * token :
         */
        private String token;
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
    }
}
