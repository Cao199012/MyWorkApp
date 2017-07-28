package com.caojian.myworkapp;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CJ on 2017/7/26.
 */

public class RxTest {

////    public static void asyncToSync() {
////        Function<Scheduler, Scheduler> schedulerFunc = new Function<Scheduler, Scheduler>() {
////            @Override
////            public Scheduler apply(@NonNull Scheduler scheduler) throws Exception {
////               // return Schedulers.immediate();
////            }
////
////        };
//
////        RxAndroidPlugins rxAndroidSchedulersHook = new RxAndroidPlugins() {
//////            @Override
//////            public Scheduler getMainThreadScheduler() {
//////                return Schedulers.immediate();
//////            }
////
////        };
//
//        RxJavaPlugins.reset();
//        RxJavaPlugins.setIoSchedulerHandler(schedulerFunc);
//        RxJavaPlugins.setComputationSchedulerHandler(schedulerFunc);
//
//        RxAndroidPlugins.reset();
//    }

}
