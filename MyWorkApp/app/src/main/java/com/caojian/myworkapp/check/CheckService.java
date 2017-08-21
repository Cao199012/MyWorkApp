package com.caojian.myworkapp.check;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by CJ on 2017/8/20.
 */

public interface CheckService {
    @GET("checkPhone.do")
    Observable<CheckMsg> checkPhone(@Query("phoneNo")String phone);
}
