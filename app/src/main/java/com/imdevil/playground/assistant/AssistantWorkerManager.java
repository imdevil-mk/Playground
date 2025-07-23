package com.imdevil.playground.assistant;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.imdevil.assistant.IAssistantWorker;

public class AssistantWorkerManager {
    private static final String TAG = "AssistantWorkerManager";

    private static class AssistantWorkerManagerInstanceHolder {
        private final static AssistantWorkerManager INSTANCE = new AssistantWorkerManager();
    }

    private AssistantWorkerManager() {
    }

    public static AssistantWorkerManager getInstance() {
        return AssistantWorkerManagerInstanceHolder.INSTANCE;
    }

    private final static int DURATION_MSG_TIMEOUT = 500;
    private final static int MSG_WORKER_TIMEOUT = 1000;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            AssistantWorkerManager.this.handleMessage(msg);
        }
    };

    private void handleMessage(@NonNull Message msg) {
        if (msg.what == MSG_WORKER_TIMEOUT) {
            handleWorkerTimeout(msg);
            return;
        }
    }

    private void handleWorkerTimeout(@NonNull Message msg) {
        IAssistantWorker worker = (IAssistantWorker) msg.obj;
        Log.d(TAG, "handleWorkerTimeout: worker=" + worker);
        if (worker == null) {
            return;
        }
        worker.onTimeout();
    }

    public boolean hasWorkerRunning(IAssistantWorker worker) {
        return mHandler.hasMessages(MSG_WORKER_TIMEOUT, worker);
    }

    public void startWorkerTimeout(IAssistantWorker worker) {
        startWorkerTimeout(worker, DURATION_MSG_TIMEOUT);
    }

    public void startWorkerTimeout(IAssistantWorker worker, int mills) {
        Message msg = Message.obtain(mHandler, MSG_WORKER_TIMEOUT, worker);
        mHandler.sendMessageDelayed(msg, mills);
    }

    public boolean removeWorker(IAssistantWorker worker) {
        boolean has = hasWorkerRunning(worker);
        if (has) {
            mHandler.removeMessages(MSG_WORKER_TIMEOUT, worker);
        }
        return has;
    }
}
