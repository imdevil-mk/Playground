package com.imdevil.playground.assistant;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class FakePropertyManager {
    private static final String TAG = "FakePropertyManager";

    private static class FakePropertyManagerInstanceHolder {
        private final static FakePropertyManager INSTANCE = new FakePropertyManager();
    }

    private FakePropertyManager() {

    }

    public static FakePropertyManager getInstance() {
        return FakePropertyManagerInstanceHolder.INSTANCE;
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            FakePropertyManager.this.handleMessage(msg);
        }
    };

    public static final int MST_PROPERTY_EMIT = 1000;

    private final List<OnPropertyListener> mListeners = new ArrayList<>();

    private void handleMessage(@NonNull Message msg) {
        if (msg.what == MST_PROPERTY_EMIT) {
            handlePropertyEmit(msg);
        }
    }

    private void handlePropertyEmit(@NonNull Message msg) {
        int id = msg.arg1;
        Object value = msg.obj;
        notifyListener(id, value);
    }

    public void setProperty(int id, int value) {
        if (id == 1001 && value == 1) {
            mHandler.postDelayed(() -> {
                notifyListener(2001, 1);
            }, 300);
        }
    }

    public void setProperty(int id, Object value) {
        Log.d(TAG, "setProperty: id=" + id + " value=" + value);
    }

    public void addListener(OnPropertyListener listener) {
        mListeners.add(listener);
        Log.d(TAG, "addListener: " + listener + " " + mListeners.size());
    }

    public void removeListener(OnPropertyListener listener) {
        mListeners.remove(listener);
        Log.d(TAG, "removeListener: " + listener + " " + mListeners.size());
    }

    private void notifyListener(int id, Object value) {
        for (OnPropertyListener listener : mListeners) {
            listener.onPropertyChanged(id, value);
        }
    }

    public void emitFakeProperty(int id, Object value, int mills) {
        Message msg = Message.obtain(mHandler, MST_PROPERTY_EMIT, id, id, value);
        mHandler.sendMessageDelayed(msg, mills);
    }

    public interface OnPropertyListener {
        void onPropertyChanged(int id, Object value);
    }
}
