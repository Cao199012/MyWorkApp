package com.caojian.myworkapp.api;

import com.caojian.myworkapp.model.base.BaseResponseResult;
import com.caojian.myworkapp.model.response.ApplyFriendsRecord;
import com.caojian.myworkapp.model.data.FriendItem;
import com.caojian.myworkapp.model.response.DrawInfo;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.FriendMsg;
import com.caojian.myworkapp.model.response.FriendPosition;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.model.response.GroupInfo;
import com.caojian.myworkapp.model.response.LoginMsg;
import com.caojian.myworkapp.model.response.OrderNumberMsg;
import com.caojian.myworkapp.model.response.PersonalMsg;
import com.caojian.myworkapp.model.response.RailHistoryMsg;
import com.caojian.myworkapp.model.response.RechargeInfo;
import com.caojian.myworkapp.model.response.RegisterMsg;
import com.caojian.myworkapp.model.response.RewardScoreDetailMsg;
import com.caojian.myworkapp.model.response.ValueAddedResult;
import com.caojian.myworkapp.model.response.CustomResult;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by caojian on 2017/9/28.
 */

public interface MyApi {
//    @Field，@FieldMap:Post方式传递简单的键值对,
//    需要添加@FormUrlEncoded表示表单提交
    //登录接口
    @FormUrlEncoded
    @POST("login.do")
    Observable<LoginMsg> checkLogin(@Field("phoneNo")String name, @Field("password")String password);

//    //版本更新接口
//    @FormUrlEncoded
//    @POST("versionUpdate.do")
//    Observable<UpdateResponse> getUpdateMsg(@Field("currentVersion") String version, @Field("terminalType") int type);

    //获取验证码接口 type:0:注册，1：忘记密码
    @FormUrlEncoded
    @POST("getVerificationCode.do")
    Observable<CustomResult> verityCode(@Field("phoneNo")String phone, @Field("imgCode")String imgCode, @Field("type")String type);

    //验证手机是否注册
    @FormUrlEncoded
    @POST("checkPhone.do")
    Observable<CustomResult> checkPhone(@Field("friendPhoneNo")String phone);

    //会员注册
    @FormUrlEncoded
    @POST("regist.do")
    Observable<RegisterMsg> register(@Field("phoneNo")String phone,@Field("verificationCode")String verificationCode,@Field("password")String password,@Field("invitationCode")String invitationCode,@Field("nickName")String nickName);

    //会员登录
    @FormUrlEncoded
    @POST("login.do")
    Observable<LoginMsg> login(@Field("phoneNo")String phone, @Field("password")String password);

    //重置密码
    @FormUrlEncoded
    @POST("resetPassword.do")
    Observable<RegisterMsg> resetPassword(@Field("phoneNo")String phone, @Field("verificationCode")String verificationCode, @Field("password")String password);

    //退出登录
    @FormUrlEncoded
    @POST("logout.do")
    Observable<RegisterMsg> outLogin(@Field("token")String token);

    //更改头像（二进制流）
    @Multipart
    @POST("uploadHeadPic.do")
    Observable<CustomResult> uploadHeadPic(@Part("token") RequestBody token, @Part MultipartBody.Part part);

    //更新定位坐标
    @FormUrlEncoded
    @POST("uploadPosition.do")
    Call<CustomResult> uploadPosition(@Field("token")String token, @Field("longitude")String longitude, @Field("latitude")String latitude);

    //获取好友位置
    @FormUrlEncoded
    @POST("getRealtimePositionByPhoneNo.do")
    //Observable<FriendPosition> getFriendPosition(@Field("token")String token, @Field("positionInfoDtos")List<PositionInfoDto> phoneNo);
    Observable<FriendPosition> getFriendPosition(@Field("token")String token, @Field("positionInfoDtos")String phoneNo);    //获取好友位置

    @FormUrlEncoded
    @POST("setFriendsVisibleBeforeExiting.do")
    Observable<CustomResult> setFriendsVisibleBeforeExiting(@Field("token")String token, @Field("friendPhoneNos")String phoneNo);

    //查询上次观察的好友位置
    @FormUrlEncoded
    @POST("getUserViewBeforeExit.do")
    Observable<FriendsAndGroupsMsg> getUserViewBeforeExit(@Field("token")String token);



    //取消查看组好友位置
    @FormUrlEncoded
    @POST("cancelGetGroupPosition.do")
    Observable<CustomResult> cancelGetGroupPosition(@Field("token")String token, @Field("groupId")String groupId);

    //查询联系人状态
    @FormUrlEncoded
    @POST("getPhoneContactStatus.do")
    Observable<FriendPosition> getPhoneContactStatus(@Field("token")String token, @Field("friendPhoneNo")String friendPhoneNo);

    //获取好友列表
    @FormUrlEncoded
    @POST("getFriendList.do")
    Observable<FriendItem> getFriendLsit(@Field("token")String token);

    //根据手机号搜索用户
    @FormUrlEncoded
    @POST("searchUserByPhoneNo.do")
    Observable<FriendMsg> searchUserByPhoneNo(@Field("token")String token, @Field("friendPhoneNo")String phone);

    //申请添加好友
    @FormUrlEncoded
    @POST("applyForAddFriend.do")
    Observable<CustomResult> applyForAddFriend(@Field("token")String token, @Field("friendPhoneNo")String phone, @Field("applyInfo")String applyInfo);
    //获取申请好友记录
    @FormUrlEncoded
    @POST("getApplicationRecordOfFriend.do")
    Observable<ApplyFriendsRecord> getApplicationRecordOfFriend(@Field("token")String token);

    //同意或拒绝添加好友 isApprove 1 同意 2 拒绝
    @FormUrlEncoded
    @POST("agreeToAddFriend.do")
    Observable<CustomResult> agreeToAddFriend(@Field("token")String token, @Field("friendPhoneNo")String friendPhoneNo, @Field("applicationId")String applicationId, @Field("isApprove")String isApprove);

    //同意或拒绝添加好友 isApprove 1 同意 2 拒绝
    @FormUrlEncoded
    @POST("getFriendInfo.do")
    Observable<FriendDetailInfo> getFriendInfo(@Field("token")String token, @Field("friendPhoneNo")String friendPhoneNo);

    //修改好友信息
    @FormUrlEncoded
    @POST("modifyFriendInfo.do")
    Observable<CustomResult> modifyFriendInfo(@Field("token")String token, @Field("friendPhoneNo")String friendPhoneNo, @Field("friendRemarkName")String friendRemarkName, @Field("accreditStartTime")String accreditStartTime, @Field("accreditEndTime")String accreditEndTime, @Field("isAccreditVisible")String isAccreditVisible, @Field("accreditWeeks")String weeks);

    //删除好友
    @FormUrlEncoded
    @POST("deleteFriend.do")
    Observable<CustomResult> deleteFriend(@Field("token")String token, @Field("friendPhoneNo")String friendPhoneNo);

    //获取好友以及好友群列表
    @FormUrlEncoded
    @POST("getFriendsAndGroups.do")
    Observable<FriendsAndGroupsMsg> getFriendsAndGroups(@Field("token")String token);

    //添加群
    @FormUrlEncoded
    @POST("addGroupInfo.do")
    Observable<CustomResult> addGroupInfo(@Field("token")String token, @Field("groupName")String groupName);

    //获取好友群信息
    @FormUrlEncoded
    @POST("getGroupInfo.do")
    Observable<GroupInfo> getGroupInfo(@Field("token")String token, @Field("groupId")String groupId);
    //修改好友群信息

    @FormUrlEncoded
    @POST("modifyGroupInfo.do")
    Observable<CustomResult> modifyGroupInfo(@Field("token")String token, @Field("groupId")String groupId, @Field("groupName")String groupName, @Field("accreditStartTime")String accreditStartTime, @Field("accreditEndTime")String accreditEndTime, @Field("isAccreditVisible")String isAccreditVisible, @Field("accreditWeeks")String weeks);

    //删除好友群信息
    @FormUrlEncoded
    @POST("deleteGroupInfo.do")
    Observable<CustomResult> deleteGroupInfo(@Field("token")String token, @Field("groupId")String groupName);

    //将好友添加到分组
    @FormUrlEncoded
    @POST("moveFriendToGroup.do")
    Observable<CustomResult> moveFriendToGroup(@Field("token")String token, @Field("groupId")String groupId, @Field("friendPhoneNos")String friendPhoneNo);

    //将好友从分组中删除
    @FormUrlEncoded
    @POST("removeFriendFromGroup.do")
    Observable<CustomResult> removeFriendFromGroup(@Field("token")String token, @Field("groupId")String groupId, @Field("friendPhoneNos")String friendPhoneNo);

    //获取个人信息
    @FormUrlEncoded
    @POST("getMemberInfo.do")
    Observable<PersonalMsg> getMemberInfo(@Field("token")String token);

    //修改个人信息
    @FormUrlEncoded
    @POST("modifyMemberInfo.do")
    Observable<CustomResult> modifyMemberInfo(@Field("token")String token, @Field("headPic")String headPic, @Field("nickName")String nickName, @Field("age")String age);

    //查询积分明细
    @FormUrlEncoded
    @POST("getRewardScoreDetail.do")
    Observable<RewardScoreDetailMsg> getRewardScoreDetail(@Field("token")String token, @Field("pageNumber")String pageNumber);

    //转赠积分
    @FormUrlEncoded
    @POST("transferRewardScoreToFriend.do")
    Observable<CustomResult> transferRewardScoreToFriend(@Field("token")String token, @Field("rewardScore")String rewardScore, @Field("friendPhoneNo")String friendPhoneNo, @Field("verificationCode")String verificationCode);

    //积分提现
    @FormUrlEncoded
    @POST("withdrawalRewardScore.do")
    Observable<CustomResult> withdrawalRewardScore(@Field("token")String token, @Field("rewardScore")int rewardScore
            , @Field("detailInfo")String detailInfo , @Field("bankName")String bankName , @Field("bankNumber")String bankNumber , @Field("name")String name, @Field("verificationCode")String verificationCode);

   //积分购买会员
    @FormUrlEncoded
    @POST("buyMemberByRewardScore.do")
    Observable<CustomResult> buyMemberByRewardScore(@Field("token")String token, @Field("validityDurationType")String type);

//    //现金购买会员
//    @FormUrlEncoded
//    @POST("buyMemberByRecharge.do")
//    Observable<CustomResult> buyMemberByRecharge(@Field("token")String token, @Field("validityDurationType")String type);
    //积分购买增值服务
    @FormUrlEncoded
    @POST("buyValueAddedServiceByRewardScore.do")
    Observable<CustomResult> buyValueAddedServiceByRewardScore(@Field("token")String token, @Field("type")String type); //type:1轨迹回放 2电子围栏
//    //现金购买增值服务
//    @FormUrlEncoded
//    @POST("buyValueAddedServiceByRecharge.do")
//    Observable<> buyValueAddedServiceByRecharge(@Field("token")String token, @Field("type")String type);

    //获取充值记录
    @FormUrlEncoded
    @POST("getRechargeInfo.do")
    Observable<RechargeInfo> getRechargeInfo(@Field("token")String token,@Field("pageNumber")String pageNumber);

    //获取提现记录
    @FormUrlEncoded
    @POST("getWithdrawalRewardScoreInfo.do")
    Observable<DrawInfo> getWithdrawalRewardScoreInfo(@Field("token")String token, @Field("pageNumber")String pageNumber);

    //获取订单号
    @FormUrlEncoded
    @POST("getOrderNumber.do")
    Observable<OrderNumberMsg> getOrderNumber(@Field("token")String token,@Field("type")String type,@Field("validityDurationType")String validityDurationType); //type:0 会员1轨迹回放 2电子围栏

    // 获取增值服务时间
    @FormUrlEncoded
    @POST("getMemberValueAddedTime.do")
    Observable<ValueAddedResult> getMemberValueAddedTime(@Field("token")String token, @Field("type")String type); //type:0 会员1轨迹回放 2电子围栏


    @FormUrlEncoded
    @POST("getMemberHistoryPositions.do")
    Observable<RailHistoryMsg> getMemberHistoryPositions(@Field("token")String token,@Field("friendPhoneNo")String friendPhoneNo,@Field("startTime")String startTime,@Field("endTime")String endTime);
}
