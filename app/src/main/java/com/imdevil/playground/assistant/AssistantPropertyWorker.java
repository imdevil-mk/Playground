package com.imdevil.playground.assistant;

import android.util.Log;

import androidx.annotation.NonNull;

import com.imdevil.assistant.IAssistantWorker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AssistantPropertyWorker implements IAssistantWorker, FakePropertyManager.OnPropertyListener {
    private static final String TAG = "AssistantPropertyWorker";
    private final Map<Integer, List<Object>> mWriteMap = new LinkedHashMap<>();
    private final Map<Integer, List<Object>> mSuccessMap = new HashMap<>();
    private final Map<Integer, AssistantResultChecker> mSuccessCheckers = new HashMap<>();
    private final Map<Integer, List<Object>> mFailMap = new HashMap<>();
    private final Map<Integer, AssistantResultChecker> mFailCheckers = new HashMap<>();
    private IAssistantWorker mNextWorker;
    private OnSuccessListener mSuccessListener;
    private OnFailListener mFailListener;
    private OnTimeoutListener mTimeoutListener;

    public AssistantPropertyWorker addWrite(int id, Object... values) {
        List<Object> writeValues = mWriteMap.computeIfAbsent(id, key -> new ArrayList<>());
        writeValues.addAll(Arrays.asList(values));
        return this;
    }

    public AssistantPropertyWorker addSuccess(int id, Object... values) {
        List<Object> writeValues = mSuccessMap.computeIfAbsent(id, key -> new ArrayList<>());
        writeValues.addAll(Arrays.asList(values));
        return this;
    }

    public AssistantPropertyWorker addSuccess(int id, AssistantResultChecker.ResultValidator validator, Object... values) {
        AssistantResultChecker checker = mSuccessCheckers.computeIfAbsent(id, key -> new AssistantResultChecker());
        checker.id = id;
        checker.values.addAll(Arrays.asList(values));
        checker.validator = validator;
        return this;
    }

    public AssistantPropertyWorker addFail(int id, Object... values) {
        List<Object> writeValues = mFailMap.computeIfAbsent(id, key -> new ArrayList<>());
        writeValues.addAll(Arrays.asList(values));
        return this;
    }

    public AssistantPropertyWorker addFail(int id, AssistantResultChecker.ResultValidator validator, Object... values) {
        AssistantResultChecker checker = mFailCheckers.computeIfAbsent(id, key -> new AssistantResultChecker());
        checker.id = id;
        checker.values.addAll(Arrays.asList(values));
        checker.validator = validator;
        return this;
    }

    public AssistantPropertyWorker addNext(IAssistantWorker worker) {
        mNextWorker = worker;
        return this;
    }

    public AssistantPropertyWorker ifSuccess(OnSuccessListener listener) {
        mSuccessListener = listener;
        return this;
    }

    public AssistantPropertyWorker ifFail(OnFailListener listener) {
        mFailListener = listener;
        return this;
    }

    public AssistantPropertyWorker ifTimeout(OnTimeoutListener listener) {
        mTimeoutListener = listener;
        return this;
    }

    @Override
    public void onPropertyChanged(int id, Object value) {
        //Log.d(TAG, "onPropertyChanged: id=" + id + " value=" + value);
        if (ifResultMatch(id, value, mSuccessMap)) {
            removeValue(id, value, mSuccessMap);
            if (mSuccessMap.isEmpty()) {
                notifySuccessListener(mSuccessListener);
                if (mNextWorker != null) {
                    mNextWorker.startWork();
                }
            }
            return;
        }

        if (ifResultMatch(id, value, mFailMap)) {
            notifyFailListener(id, value, mFailListener);
            return;
        }

        if (ifMatchCheckerValue(id, value, mSuccessCheckers)) {
            removeChecker(id, value, mSuccessCheckers);
            if (mSuccessCheckers.isEmpty()) {
                notifySuccessListener(mSuccessListener);
                if (mNextWorker != null) {
                    mNextWorker.startWork();
                }
            }
            return;
        }

        if (ifMatchCheckerValue(id, value, mFailCheckers)) {
            notifyFailListener(id, value, mFailListener);
            return;
        }
    }

    @Override
    public void onTimeout() {
        notifyTimeoutListener(mTimeoutListener);
    }

    @Override
    public void startWork() {
        Log.d(TAG, "startWork: " + this);
        FakePropertyManager.getInstance().addListener(this);
        mWriteMap.forEach((id, values) -> {
            for (Object value : values) {
                FakePropertyManager.getInstance().setProperty(id, value);
            }
        });
        AssistantWorkerManager.getInstance().startWorkerTimeout(this);
    }

    @Override
    public void tearDown() {
        Log.d(TAG, "tearDown: " + this);
        AssistantWorkerManager.getInstance().removeWorker(this);
        FakePropertyManager.getInstance().removeListener(this);
    }

    private void notifySuccessListener(OnSuccessListener listener) {
        if (listener != null) {
            listener.onSuccess(this);
        }
        tearDown();
    }

    private void notifyFailListener(int id, Object value, OnFailListener listener) {
        if (listener != null) {
            listener.onFail(id, value, this);
        }
        tearDown();
    }

    private void notifyTimeoutListener(OnTimeoutListener listener) {
        if (listener != null) {
            listener.onTimeout(this);
        }
        tearDown();
    }

    public interface OnSuccessListener {
        void onSuccess(IAssistantWorker worker);
    }

    public interface OnFailListener {
        void onFail(int id, Object value, IAssistantWorker worker);
    }

    public interface OnTimeoutListener {
        void onTimeout(IAssistantWorker worker);
    }

    private boolean ifMatchCheckerValue(int id, Object value, Map<Integer, AssistantResultChecker> map) {
        AssistantResultChecker checker = map.get(id);
        if (checker == null) return false;
        return checker.match(id, value);
    }

    private boolean removeChecker(int id, Object value, Map<Integer, AssistantResultChecker> map) {
        AssistantResultChecker checker = map.get(id);
        if (checker == null) return false;
        boolean removed = checker.removeValue(value);
        //Log.d(TAG, "removeCheckerValue: id=" + id + " value=" + value);
        if (checker.isSuccess()) {
            //Log.d(TAG, "removeChecker: id=" + id);
            map.remove(id);
        }
        return removed;
    }

    private boolean ifResultMatch(int id, Object value, Map<Integer, List<Object>> map) {
        //Log.d(TAG, "ifResultMatch: id=" + id + " value=" + value + " map=" + toString(map));
        List<Object> wanted = map.get(id);
        if (wanted == null || wanted.isEmpty()) return false;
        for (Object o : wanted) {
            if (AssistantUtils.match(value, o)) {
                return true;
            }
        }
        return false;
    }

    private boolean removeValue(int id, Object value, Map<Integer, List<Object>> map) {
        List<Object> wanted = map.get(id);
        if (wanted == null || wanted.isEmpty()) return false;
        boolean removed = wanted.remove(value);
        if (wanted.isEmpty()) {
            map.remove(id);
        }
        return removed;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!mWriteMap.isEmpty()) {
            sb.append("write=").append(toString(mWriteMap));
        }
        if (!mSuccessMap.isEmpty()) {
            sb.append("success=").append(toString(mSuccessMap));
        }
        if (!mFailMap.isEmpty()) {
            sb.append("fail=").append(toString(mFailMap));
        }
        return sb.toString();
    }

    private String toString(Map<Integer, List<Object>> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((key, values) -> {
            sb.append("[").append(key).append("-->");
            for (int i = 0; i < values.size(); i++) {
                sb.append(values.get(i));
                if (i != values.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
        });
        return sb.toString();
    }
}
