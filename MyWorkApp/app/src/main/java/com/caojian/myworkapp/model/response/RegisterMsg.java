package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

/**
 * Created by CJ on 2017/8/22.
 */

public class RegisterMsg extends BaseResponseResult<RegisterMsg.DataBean> {



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
