package com.imdevil.playground.sdk.biz;
// Declare any non-default types here with import statements

interface IBizManagerService {
    IBinder getBizServiceManager(in String serviceName);
}