package com.imdevil.playground.biz;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BizService extends Service {

    private BizBinderManager bizBinderManager;

    @Override
    public void onCreate() {
        super.onCreate();

        bizBinderManager = new BizBinderManager();
        bizBinderManager.init();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bizBinderManager;
    }
}
