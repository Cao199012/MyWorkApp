package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

import java.util.List;

/**
 * Created by CJ on 2017/12/17.
 */

public class RailHistoryMsg extends BaseResponseResult<RailHistoryMsg.DataBean> {

    /**
     * code : 000000
     * message :  成功
     * data : {"longitude":"","latitude":""}
     */


    public static class DataBean extends BaseResponseResult.UpdateDataBean{
        private List<PositionsBean> positions;

        public List<PositionsBean> getPositions() {
            return positions;
        }
    }
   public static class PositionsBean {

        /**
         * longitude :
         * latitude :
         */

        private String longitude;
        private String latitude;

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }
    }
}
