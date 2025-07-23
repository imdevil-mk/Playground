package com.imdevil.assistant;

public interface IAssistantWorker {
    void startWork();

    void onTimeout();

    void tearDown();
}
