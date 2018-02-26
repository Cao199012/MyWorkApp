package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.data.LocationItem;

import java.util.List;

/**
 * Created by CJ on 2017/10/20.
 */

public class FriendPosition extends BaseResponseResult<FriendPosition.DataBean> {

    /**
     * code : 0
     * message :  成功
     * data : {"positions":[{"longitude":"","latitude":"","headPic":""},{"longitude":"","latitude":"","headPic":""}]}
     */

    public static class DataBean extends BaseResponseResult.UpdateDataBean{
        private List<LocationItem> positions;

        public List<LocationItem> getPositions() {
            return positions;
        }

        public void setPositions(List<LocationItem> positions) {
            this.positions = positions;
        }

        public static class PositionsBean {
            /**
             * longitude :
             * latitude :
             * headPic :
             */

            private String longitude;
            private String latitude;
            private String headPic;
            private String phoneNo;

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

            public String getHeadPic() {
                return headPic;
            }

            public void setHeadPic(String headPic) {
                this.headPic = headPic;
            }

            public String getPhoneNo() {
                return phoneNo;
            }

            public void setPhoneNo(String phoneNo) {
                this.phoneNo = phoneNo;
            }
        }
    }
}
