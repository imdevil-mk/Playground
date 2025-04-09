package com.imdevil.playground.sdk.biz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import com.imdevil.playground.sdk.biz.manager.CommonDriverSeatMemoryManager;
import com.imdevil.playground.sdk.biz.manager.ICommonBaseManager;

import java.util.HashMap;

public class BizServiceManger {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private IBizManagerService remoteService;
    private final Object mLock = new Object();

    private final HashMap<String, ICommonBaseManager> mManagerMap = new HashMap<>();
    private CommonDriverSeatMemoryManager commonDriverSeatMemoryManager;

    public static BizServiceManger createBizServiceManager(Context context, ServiceConnection connection) {
        return new BizServiceManger(context, connection, null);
    }

    private BizServiceManger(Context context, ServiceConnection connection, Handler handler) {
        mContext = context;
    }

    public Object getBizManger(String name) {
        IBizManagerService service = remoteService;
        synchronized (mLock) {
            ICommonBaseManager manager = mManagerMap.get(name);
            try {
                if (manager == null) {
                    IBinder binder = service.getBizServiceManager(name);
                    if (binder == null) {
                        return null;
                    }
                    manager = createCommonManager(name, binder);
                    if (manager == null) {
                        return null;
                    }
                    mManagerMap.put(name, manager);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return manager;
        }
    }

    private ICommonBaseManager createCommonManager(String name, IBinder binder) {
        ICommonBaseManager manager = null;
        switch (name) {
            case BizServiceConst.DRIVER_SEAT_MEMORY_MANAGER:
                if (commonDriverSeatMemoryManager == null) {
                    commonDriverSeatMemoryManager = new CommonDriverSeatMemoryManager(mContext, binder);
                }
                manager = commonDriverSeatMemoryManager;
            default:
                break;
        }
        return manager;
    }

    private void rebindServiceObject() {
        if (remoteService == null) {
            return;
        }
        for (String serviceKey : mManagerMap.keySet()) {
            ICommonBaseManager service = mManagerMap.get(serviceKey);
            if (service != null) {
                try {
                    IBinder binder = remoteService.getBizServiceManager(serviceKey);
                    service.resetBinder(binder);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
