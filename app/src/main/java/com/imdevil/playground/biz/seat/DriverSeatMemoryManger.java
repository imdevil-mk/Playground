package com.imdevil.playground.biz.seat;

import android.os.RemoteException;

import com.imdevil.playground.biz.IBaseBizManager;
import com.imdevil.playground.sdk.biz.seat.DriverSeatMemoryBean;
import com.imdevil.playground.sdk.biz.seat.IDriverSeatMemoryListener;
import com.imdevil.playground.sdk.biz.seat.IDriverSeatMemoryManager;

import java.util.List;

public class DriverSeatMemoryManger extends IDriverSeatMemoryManager.Stub implements IBaseBizManager {

    private static DriverSeatMemoryManger sInstance;

    public static DriverSeatMemoryManger getInstance() {
        if (null == sInstance) {
            synchronized (DriverSeatMemoryManger.class) {
                sInstance = new DriverSeatMemoryManger();
            }
        }
        return sInstance;
    }

    @Override
    public void init() {

    }

    @Override
    public void release() {

    }

    @Override
    public String getMemoryName(int memory) throws RemoteException {
        return null;
    }

    @Override
    public int setMemoryName(int memory) throws RemoteException {
        return 0;
    }

    @Override
    public List<DriverSeatMemoryBean> getDriverSeatMemoryStatus() throws RemoteException {
        return null;
    }

    @Override
    public void registerDriverSeatMemoryListener(IDriverSeatMemoryListener listener) throws RemoteException {

    }

    @Override
    public void unregisterDriverSeatMemoryListener(IDriverSeatMemoryListener listener) throws RemoteException {

    }


}
