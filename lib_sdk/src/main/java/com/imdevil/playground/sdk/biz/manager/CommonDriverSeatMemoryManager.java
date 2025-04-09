package com.imdevil.playground.sdk.biz.manager;

import android.content.Context;
import android.os.IBinder;

import com.imdevil.playground.sdk.biz.seat.IDriverSeatMemoryManager;

public class CommonDriverSeatMemoryManager implements ICommonBaseManager {

    private IDriverSeatMemoryManager manager;

    public CommonDriverSeatMemoryManager(Context context, IBinder binder) {
        manager = IDriverSeatMemoryManager.Stub.asInterface(binder);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void resetBinder(IBinder service) {

    }
}
