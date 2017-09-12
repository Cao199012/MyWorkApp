package com.caojian.myworkapp.modules.update;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by CJ on 2017/8/17.
 */

public interface UpdateService {

    @GET("versionUpdate.do")
    Observable<UpdateMsg> getUpdateMsg(@Query("currentVersion") String version,@Query("terminalType") int type);
}
