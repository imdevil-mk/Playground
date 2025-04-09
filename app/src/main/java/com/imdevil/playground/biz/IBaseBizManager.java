package com.imdevil.playground.biz;

public interface IBaseBizManager {
    /**
     * service is started. All necessary initialization should be done and service should be
     * functional after this.
     */
    void init();

    /**
     * service should stop and all resources should be released.
     */
    void release();
}
