package com.caojian.myworkapp.model.response;

import com.caojian.myworkapp.model.base.BaseResponseResult;

import java.util.Date;

/**
 * Created by CJ on 2017/10/29.
 */

public class PersonalMsg extends BaseResponseResult<PersonalMsg.DataBean> {

    /**
     * code : 0
     * message :  成功
     * data : {"headPic":"","nickName":"","age":"","memberType":"","valueAddedService":"","rewardScore":""}
     */
    public static class DataBean extends BaseResponseResult.UpdateDataBean{

        /** 客户手机号 */
        private String phoneNo;
        /** 客户姓名 */
        private String nickName;
        /** 昵称拼音 */
        private String nickSpell;
        /** 性别1男,2女 */
        private String gender;
        /** 会员类型 1:非会员,2:正式会员,3:试用期会员 */
        private int memberType;
        /** 是否开通轨迹回放服务1是2否 */
        private String trajectoryService;
        /** 是否开通电子围栏服务1是2否 */
        private String fenceService;
        /** 年龄 */
        private String age;
        /** 头像ID */
        private String headPic;
        /** 会员开始时间 */
        private String memberStartTime;
        /** 会员结束时间 */
        private String memberEndTime;
        /** 积分 */
        private Double rewardScore;
        /** 邀请码 */
        private String invitationCode;
        /** 下级数 */
        private String nextLevelCount;
        /** 下下级数 */
        private String underNextLevelCount;


        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getNickSpell() {
            return nickSpell;
        }

        public String getGender() {
            return gender;
        }

        public int getMemberType() {
            return memberType;
        }

        public void setMemberType(int memberType) {
            this.memberType = memberType;
        }

        public String getTrajectoryService() {
            return trajectoryService;
        }

        public void setTrajectoryService(String trajectoryService) {
            this.trajectoryService = trajectoryService;
        }

        public String getFenceService() {
            return fenceService;
        }

        public void setFenceService(String fenceService) {
            this.fenceService = fenceService;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getMemberStartTime() {
            return memberStartTime;
        }

        public void setMemberStartTime(String memberStartTime) {
            this.memberStartTime = memberStartTime;
        }

        public String getMemberEndTime() {
            return memberEndTime;
        }

        public void setMemberEndTime(String memberEndTime) {
            this.memberEndTime = memberEndTime;
        }

        public Double getRewardScore() {
            return rewardScore;
        }

        public void setRewardScore(Double rewardScore) {
            this.rewardScore = rewardScore;
        }

        public String getInvitationCode() {
            return invitationCode;
        }

        public String getNextLevelCount() {
            return nextLevelCount;
        }

        public String getUnderNextLevelCount() {
            return underNextLevelCount;
        }
    }
}
