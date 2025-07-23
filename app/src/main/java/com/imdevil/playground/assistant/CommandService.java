package com.imdevil.playground.assistant;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.imdevil.assistant.Assistant;

public class CommandService extends Service {
    private static final String TAG = "CommandService";

    @Override
    public void onCreate() {
        super.onCreate();
        Assistant.getInstance().init();
        Assistant.getInstance().addHandler(new AssistantSeatHandler());
        Assistant.getInstance().addHandler(new AssistantWindowHandler());
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleIntent(Intent intent) {
        String json = intent.getStringExtra("json");
        Log.d(TAG, "handleIntent: " + json);
        Assistant.getInstance().dispatchJson(json);
    }
}
