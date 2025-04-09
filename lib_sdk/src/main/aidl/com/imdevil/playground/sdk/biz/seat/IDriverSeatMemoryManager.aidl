package com.imdevil.playground.sdk.biz.seat;
// Declare any non-default types here with import statements

import com.imdevil.playground.sdk.biz.seat.DriverSeatMemoryBean;
import com.imdevil.playground.sdk.biz.seat.IDriverSeatMemoryListener;

interface IDriverSeatMemoryManager {
    String getMemoryName(int memory);
    int setMemoryName(int memory);
    List<DriverSeatMemoryBean> getDriverSeatMemoryStatus();

     void registerDriverSeatMemoryListener(IDriverSeatMemoryListener listener);
     void unregisterDriverSeatMemoryListener(IDriverSeatMemoryListener listener);
}