package com.caojian.myworkapp.password;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by CJ on 2017/8/21.
 */

public interface VerityService {
    @GET("getVerificationCode.do")
    Call<VerityCodeMsg> verityCode(@Query("phoneNo")String phone,@Query("imgCode")String imgCode);
}
