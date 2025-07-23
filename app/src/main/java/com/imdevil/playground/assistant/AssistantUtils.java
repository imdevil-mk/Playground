package com.imdevil.playground.assistant;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AssistantUtils {
    private static final String TAG = "Utils";

    public static int optInt(String json, String valueName, int defValue) {
        int value = defValue;
        try {
            JSONObject jsonObject = new JSONObject(json);
            value = jsonObject.optInt(valueName, defValue);
        } catch (JSONException e) {
            Log.d(TAG, "optInt: " + json + " error with valueName=" + valueName);
        }
        return value;
    }

    public static String optString(String json, String valueName) {
        return optString(json, valueName, "");
    }

    public static String optString(String json, String valueName, String defValue) {
        String value = defValue;
        try {
            JSONObject jsonObject = new JSONObject(json);
            value = jsonObject.optString(valueName);
        } catch (JSONException e) {
            Log.d(TAG, "optString: " + json + " error with valueName=" + valueName);
        }
        return value;
    }

    public static boolean match(Object value, Object wanted) {
        boolean match = false;
        if (value instanceof Integer && wanted instanceof Integer && (int) value == (int) wanted) {
            match = true;
        } else if (value instanceof Float && wanted instanceof Float && (float) value == (float) wanted) {
            match = true;
        } else if (value instanceof Double && wanted instanceof Double && (double) value == (double) wanted) {
            match = true;
        } else if (value instanceof String && wanted instanceof String && TextUtils.equals((String) value, (String) wanted)) {
            match = true;
        }
        return match;
    }
}
