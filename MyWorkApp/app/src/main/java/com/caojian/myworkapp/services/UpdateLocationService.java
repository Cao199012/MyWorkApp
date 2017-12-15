package com.caojian.myworkapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.utils.DistanceUtil;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.api.MyApi;
import com.caojian.myworkapp.manager.RetrofitManger;
import com.caojian.myworkapp.model.response.CustomResult;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.until.Until;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateLocationService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.caojian.myworkapp.services.action.FOO";
    private static final String ACTION_BAZ = "com.caojian.myworkapp.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.caojian.myworkapp.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.caojian.myworkapp.services.extra.PARAM2";

    MyApi myApi;
    Retrofit retrofit;
    MyApplication application ;
    BDLocation mPrelocation;
    public UpdateLocationService() {
        super("UpdateLocationService");
    }
    Handler handler;
    Runnable runnable;
        @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        runnable=new Runnable(){
            @Override
            public void run() {
                if(retrofit == null){
                    retrofit = RetrofitManger.getRetrofit(Until.HTTP_BASE_URL,UpdateLocationService.this);
                }
                if(myApi == null)
                {
                    myApi = retrofit.create(MyApi.class);
                }
                if(application == null){
                    application = (MyApplication)getApplicationContext();
                }
                //本地采集到坐标 上传（若没取到，则进入下次循环）
                if(application.getBdLocation() != null && getDistance(mPrelocation,application.getBdLocation()) > Until.MIN_DISTANCE){
                    Call<CustomResult> call = myApi.uploadPosition(ActivityUntil.getToken(UpdateLocationService.this),application.getBdLocation().getLongitude()+"",application.getBdLocation().getLatitude()+"");
                    call.enqueue(new Callback<CustomResult>() {
                        @Override
                        public void onResponse(Call<CustomResult> call, Response<CustomResult> response) {
                            if(response != null && response.code() == 200) {
                               mPrelocation = application.getBdLocation();
                            }
                        }

                        @Override
                        public void onFailure(Call<CustomResult> call, Throwable t) {
                            String a = t.toString();
                        }
                    });
                }

                // TODO Auto-generated method stub
                //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作

                handler.postDelayed(this, ((MyApplication)getApplicationContext()).getUpdateTime());
            }
        };
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UpdateLocationService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UpdateLocationService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
       // throw new UnsupportedOperationException("Not yet implemented");
        if(handler != null && runnable != null) {
            handler.postDelayed(runnable, ((MyApplication) getApplicationContext()).getUpdateTime());
        }
    }

    private double getDistance(BDLocation pre,BDLocation now){
        if(pre == null){
            return Until.MIN_DISTANCE + 1;
        }
        LatLng p1LL = new LatLng(pre.getLatitude(), pre.getLongitude());
        LatLng p2LL = new LatLng(now.getLatitude(), now.getLongitude());
        double distance = DistanceUtil.getDistance(p2LL, p1LL);
        return distance;
    }
}
