package com.caojian.myworkapp.login;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by CJ on 2017/7/28.
 */

public interface LoginService {
    @GET("")
    Observable<String> checkLogin(@Query("username")String name,@Query("password")String password);
}
