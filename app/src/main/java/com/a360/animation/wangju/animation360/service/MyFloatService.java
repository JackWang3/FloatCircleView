package com.a360.animation.wangju.animation360.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.a360.animation.wangju.animation360.engine.FloatViewManager;

/**
 * Created by user on 2016/9/3.
 */
public class MyFloatService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        FloatViewManager floatViewManager = FloatViewManager.getInstance(this);
        floatViewManager.showFloatCircleView();
        super.onCreate();
    }
}
