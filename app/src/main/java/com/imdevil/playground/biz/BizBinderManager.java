package com.imdevil.playground.biz;

import android.os.IBinder;
import android.os.RemoteException;

import com.imdevil.playground.biz.seat.DriverSeatMemoryManger;
import com.imdevil.playground.sdk.biz.BizServiceConst;
import com.imdevil.playground.sdk.biz.IBizManagerService;

import java.util.HashMap;
import java.util.Map;

public class BizBinderManager extends IBizManagerService.Stub {

    private final Map<String, IBaseBizManager> mBizManagerMap = new HashMap<>();

    public BizBinderManager() {
        DriverSeatMemoryManger driverSeatMemoryManger = new DriverSeatMemoryManger();

        mBizManagerMap.put(BizServiceConst.DRIVER_SEAT_MEMORY_MANAGER, driverSeatMemoryManger);
    }

    public void init() {
        for (String name : mBizManagerMap.keySet()) {
            IBaseBizManager manager = mBizManagerMap.get(name);
            if (manager != null) {
                manager.init();
            }
        }
    }

    public void release() {
        for (String name : mBizManagerMap.keySet()) {
            IBaseBizManager manager = mBizManagerMap.get(name);
            if (manager != null) {
                manager.release();
            }
        }
    }

    @Override
    public IBinder getBizServiceManager(String serviceName) throws RemoteException {
        return (IBinder) mBizManagerMap.get(serviceName);
    }
}
