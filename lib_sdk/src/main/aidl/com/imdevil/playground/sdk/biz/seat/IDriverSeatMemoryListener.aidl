package com.imdevil.playground.sdk.biz.seat;
// Declare any non-default types here with import statements

import com.imdevil.playground.sdk.biz.seat.DriverSeatMemoryBean;

interface IDriverSeatMemoryListener {
    void onMemroyNameChanged(int memory, String oldName, String newName);

    void onDriverSeatMemoryStatusChanged(in List<DriverSeatMemoryBean> status);
}