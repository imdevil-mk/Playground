package com.imdevil.playground.sdk.biz.manager;

import android.os.IBinder;

public interface ICommonBaseManager {
    void onDisconnected();

    void resetBinder(IBinder service);
}
