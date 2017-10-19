package com.caojian.myworkapp.api;

import com.caojian.myworkapp.model.response.CheckMsg;
import com.caojian.myworkapp.model.response.CoustomResult;
import com.caojian.myworkapp.model.response.LoginMsg;
import com.caojian.myworkapp.model.response.RegisterMsg;
import com.caojian.myworkapp.model.response.UpdateResponse;
import com.caojian.myworkapp.model.response.VerityCodeMsg;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by caojian on 2017/9/28.
 */

public interface MyApi {

    //登录接口
    @GET("login.do")
    Observable<String> checkLogin(@Query("phoneNo")String name, @Query("password")String password);

    //版本更新接口
    @GET("versionUpdate.do")
    Observable<UpdateResponse> getUpdateMsg(@Query("currentVersion") String version, @Query("terminalType") int type);

    //获取验证码接口
    @GET("getVerificationCode.do")
    Call<VerityCodeMsg> verityCode(@Query("phoneNo")String phone, @Query("imgCode")String imgCode);

    //验证手机是否注册
    @GET("checkPhone.do")
    Observable<CheckMsg> checkPhone(@Query("phoneNo")String phone);

    //会员注册
    @GET("regist.do")
    Observable<RegisterMsg> register(@Query("phoneNo")String phone,@Query("verificationCode")String verificationCode,@Query("password")String password,@Query("invitationCode")String invitationCode);

    //会员登录
    @GET("login.do")
    Observable<LoginMsg> login(@Query("phoneNo")String phone, @Query("password")String password);

    //重置密码
    @GET("resetPassword.do")
    Observable<CoustomResult> resetPassword(@Query("phoneNo")String phone, @Query("verificationCode")String verificationCode, @Query("password")String password);

    //退出登录
    @GET("logout.do")
    Observable<CoustomResult> outLogin(@Query("token")String token);

    //更改头像（二进制流）
    @GET("uploadHeadPic.do")
    Observable<CoustomResult> uploadHeadPic(@Query("token")String token,@Query("file")String pic);
}