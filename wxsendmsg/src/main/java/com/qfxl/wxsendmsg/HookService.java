package com.qfxl.wxsendmsg;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class HookService extends AccessibilityService {

    private final String TAG = "qfxl";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                String className = event.getClassName().toString();
                switch (className) {
                    case WeChatAccessUtil.WECHAT_CLASS_LAUNCHUI:
                        Log.i(TAG, "微信首页启动");
                        try {
                            WeChatAccessUtil.search(HookService.this, "陶敏");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case WeChatAccessUtil.WECHAT_CLASS_CHATUI:
                        Log.i(TAG, "微信聊天页面启动");
                        break;
                    default:
                }
            default:
        }


    }

    @Override
    public void onInterrupt() {

    }
}
