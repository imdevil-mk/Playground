package com.imdevil.playground.assistant;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AssistantResultChecker {
    private static final String TAG = "AssistantResultChecker";
    public int id;
    public List<Object> values = new ArrayList<>();
    public ResultValidator validator;

    public boolean match(int id, Object value) {
        if (validator == null) {
            Log.e(TAG, "match: you have no validator for id=" + id);
            return false;
        }
        for (Object want : values) {
            if (validator == INT_MIN_VALIDATOR) {
                Log.d(TAG, "match: id=" + id + " value=" + value + " want<" + want);
            } else if (validator == INT_MAX_VALIDATOR) {
                Log.d(TAG, "match: id=" + id + " value=" + value + " want>" + want);
            } else {
                Log.d(TAG, "match: id=" + id + " value=" + value + " want=" + want);
            }
            if (validator.match(value, want)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeValue(Object value) {
        if (validator == INT_MIN_VALIDATOR || validator == INT_MAX_VALIDATOR) {
            values.clear();
            return true;
        }
        return values.remove(value);
    }

    public boolean isSuccess() {
        return values.isEmpty();
    }

    public static ResultValidator INT_VALIDATOR = (value, want) -> {
        if (!(value instanceof Integer) || !(want instanceof Integer)) return false;
        return value == want;
    };

    public static ResultValidator INT_MIN_VALIDATOR = (value, want) -> {
        if (!(value instanceof Integer) || !(want instanceof Integer)) return false;
        return (int) value < (int) want;
    };

    public static ResultValidator INT_MAX_VALIDATOR = (value, want) -> {
        if (!(value instanceof Integer) || !(want instanceof Integer)) return false;
        return (int) value > (int) want;
    };

    public static ResultValidator FLOAT_VALIDATOR = (value, want) -> {
        if (!(value instanceof Float) || !(want instanceof Float)) return false;
        return value == want;
    };


    public interface ResultValidator {
        boolean match(Object value, Object want);
    }
}
