package com.imdevil.playground.assistant;

import android.text.TextUtils;
import android.util.Log;

import com.imdevil.assistant.IAssistantHandler;
import com.imdevil.assistant.JsonRuleConfig;

public class AssistantSeatHandler implements IAssistantHandler {
    private static final String TAG = "AssistantSeatHandler";

    @JsonRuleConfig({
            @JsonRuleConfig.Rule(
                    name = "舒享模式",
                    fields = "object=座椅;feature=舒享模式;action=*"
                    // {"object":"座椅","feature":"舒享模式","action":"打开"}
                    // '{\"object\":\"座椅\",\"feature\":\"舒享模式\",\"action\":\"打开\"}'
            ),
            @JsonRuleConfig.Rule(
                    name = "舒享模式",
                    fields = "object=座椅;function=舒享模式;action=*"
            )
    })
    public void handleComfortMode(String json) {
        Log.d(TAG, "handleComfortMode: " + json);

        String action = AssistantUtils.optString(json, "action");
        int req;
        if (TextUtils.equals(action, "打开")) {
            req = 1;
        } else if (TextUtils.equals(action, "关闭")) {
            req = 0;
        } else {
            return;
        }
        FakePropertyManager.getInstance().addListener(new FakePropertyManager.OnPropertyListener() {
            @Override
            public void onPropertyChanged(int id, Object value) {
                if (id == 2001 && (int) value == req) {
                    Log.d(TAG, "handleComfortMode onPropertyChanged: ");
                    FakePropertyManager.getInstance().removeListener(this);
                }
            }
        });
        FakePropertyManager.getInstance().setProperty(1001, 1);
    }

    @JsonRuleConfig({
            @JsonRuleConfig.Rule(
                    name = "关闭隐身模式",
                    fields = "object=整车;feature=隐身模式;action=*;value=#;"
                    // '{\"object\":\"整车\",\"feature\":\"隐身模式\",\"action\":\"关闭\"}'
                    // adb shell am startservice -a assistant.cmd.tts --es "json" '{\"object\":\"整车\",\"feature\":\"隐身模式\",\"action\":\"关闭\"}'
            )
    })
    public void handleStealthMode(String json) {
        String action = AssistantUtils.optString(json, "action");
        Log.d(TAG, "handleStealthMode: " + action + "隐身模式");
        int req;
        if (TextUtils.equals(action, "打开")) {
            req = 1;
        } else if (TextUtils.equals(action, "关闭")) {
            req = 0;
        } else {
            return;
        }

        AssistantPropertyWorker worker = new AssistantPropertyWorker()
                .addWrite(2001, 1)
                .addWrite(2002, 2)
                .addWrite(2003, 3)
                .addWrite(2100, 1)
                .addSuccess(12001, 1)
                .addSuccess(12002, 2)
                .addSuccess(12003, 3)
                .addFail(22001, 1)
                .addFail(22001, 2)
                .addFail(22002, 2)
                .addFail(22003, 3)
                .ifSuccess(w -> {
                    Log.d(TAG, "handleStealthMode: success" + req);
                })
                .ifFail((id, value, w) -> {
                    Log.d(TAG, "handleStealthMode: fail" + req);
                })
                .ifTimeout(w -> {
                    Log.d(TAG, "handleStealthMode: timeout");
                });
        worker.startWork();
    }

    @JsonRuleConfig({
            @JsonRuleConfig.Rule(
                    name = "打开隐身模式",
                    fields = "object=整车;feature=隐身模式;action=打开;value=#;",
                    priority = 100
                    //adb shell am startservice -a assistant.cmd.tts --es "json" '{\"object\":\"整车\",\"feature\":\"隐身模式\",\"action\":\"打开\",\"value\":\"0\"}'
            )
    })
    public void handleOpenStealthMode(String json) {
        String action = AssistantUtils.optString(json, "action");
        int arg = AssistantUtils.optInt(json, "value", 1);
        Log.d(TAG, "handleOpenStealthMode: " + action + "隐身模式");
        int req;
        if (TextUtils.equals(action, "打开")) {
            req = 1;
        } else if (TextUtils.equals(action, "关闭")) {
            req = 0;
        } else {
            return;
        }

        AssistantPropertyWorker worker = new AssistantPropertyWorker()
                .addWrite(2001, 1)
                .addWrite(2002, 2)
                .addWrite(2003, 3, 2, 1)
                .addWrite(2100, 1)
                .addSuccess(12001, AssistantResultChecker.INT_VALIDATOR, 1)
                .addSuccess(12002, AssistantResultChecker.INT_VALIDATOR, 2)
                .addSuccess(12003, AssistantResultChecker.INT_VALIDATOR, 3, 2, 1)
                .addSuccess(12004, AssistantResultChecker.INT_MIN_VALIDATOR, 30)
                .addFail(22001, AssistantResultChecker.INT_VALIDATOR, 1)
                .addFail(22002, AssistantResultChecker.INT_VALIDATOR, 2)
                .addFail(22003, AssistantResultChecker.INT_VALIDATOR, 3)
                .ifSuccess(w -> {
                    Log.d(TAG, "handleOpenStealthMode: success " + req);
                })
                .ifFail((id, value, w) -> {
                    Log.d(TAG, "handleOpenStealthMode: fail " + req);
                })
                .ifTimeout(w -> {
                    Log.d(TAG, "handleOpenStealthMode: timeout");
                });
        worker.startWork();

        if (arg == 1) {
            FakePropertyManager.getInstance().emitFakeProperty(12001, 1.1, 100);
            FakePropertyManager.getInstance().emitFakeProperty(12001, 1, 110);
            FakePropertyManager.getInstance().emitFakeProperty(12002, 2, 120);
            FakePropertyManager.getInstance().emitFakeProperty(12003, 1, 130);
            FakePropertyManager.getInstance().emitFakeProperty(12003, 2, 130);
            FakePropertyManager.getInstance().emitFakeProperty(12003, 3, 130);
            FakePropertyManager.getInstance().emitFakeProperty(12004, 3, 140);
        } else {
            FakePropertyManager.getInstance().emitFakeProperty(12001, 1.1, 100);
            FakePropertyManager.getInstance().emitFakeProperty(12001, 1, 110);
            FakePropertyManager.getInstance().emitFakeProperty(12002, 2, 120);
            FakePropertyManager.getInstance().emitFakeProperty(12001, 1, 130);
            FakePropertyManager.getInstance().emitFakeProperty(22002, 2, 140);
        }
    }

    @JsonRuleConfig({
            @JsonRuleConfig.Rule(
                    name = "打开宠物模式",
                    fields = "object=整车;feature=宠物模式;action=打开,关闭;value=#;",
                    priority = 100
            )
    })
    public void handlePetMode(String json) {
        System.out.println("handlePetMode: 打开宠物模式");
    }


    @JsonRuleConfig({
            @JsonRuleConfig.Rule(
                    name = "氛围灯切换到常量模式",
                    fields = "object=整车;feature=氛围灯;mode=*;action=#"
            )
    })
    public void handleAmbientMode(String json) {
        System.out.println("handleAmbientMode: 氛围灯模式");
    }

    @JsonRuleConfig({
            @JsonRuleConfig.Rule(
                    name = "氛围灯亮度最大",
                    fields = "object=整车;feature=氛围灯;function=亮度;action=#;value=#;mode=#;"
            )
    })
    public void handleAmbientBrightness(String json) {
        System.out.println("handleAmbientMode: 氛围灯亮度");
    }

    private void speakTts(String tts) {
        Log.d(TAG, "speakTts: " + tts);
    }
}
