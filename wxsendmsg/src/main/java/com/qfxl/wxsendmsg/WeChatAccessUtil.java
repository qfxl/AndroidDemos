package com.qfxl.wxsendmsg;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

class WeChatAccessUtil {
    private static final String TAG = "qfxl";
    /**
     * 微信包名
     */
    private static final String WX_PACKAGE = "com.tencent.mm";
    /**
     * 微信布局id前缀
     */
    private static final String BASELAYOUT_ID = "com.tencent.mm:id/";
    /**
     * 微信首页
     */
    static final String WECHAT_CLASS_LAUNCHUI = "com.tencent.mm.ui.LauncherUI";
    /**
     * 微信聊天页面
     */
    static final String WECHAT_CLASS_CHATUI = "com.tencent.mm.ui.chatting.ChattingUI";
    /**
     * 输入框
     */
    private static final String EDIT_TEXT = "android.widget.EditText";
    /**
     * textView
     */
    private static final String TEXT_VIEW = "android.widget.TextView";
    /**
     * 搜索id
     */
    private static final String SEARCH_ID = BASELAYOUT_ID + "jb";
    /**
     * 搜素输入框
     */
    private static final String SEARCH_INPUT_ID = BASELAYOUT_ID + "l3";
    /**
     * 搜索结果item
     */
    private static final String SEARCH_INPUT_LIST_ITEM_ID = BASELAYOUT_ID + "qj";
    /**
     * 聊天界面的输入框
     */
    private static final String CHAT_INPUT_ID = BASELAYOUT_ID + "ami";
    /**
     * 聊天界面发送按钮
     */
    private static final String SEND_ID = BASELAYOUT_ID + "amp";

    /**
     * 打开微信
     *
     * @param activity
     */
    static void openWeChat(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(WX_PACKAGE, WECHAT_CLASS_LAUNCHUI);
        activity.startActivity(intent);
    }

    /**
     * 搜索
     *
     * @param service
     * @param name
     */
    static void search(AccessibilityService service, String name) throws InterruptedException {
        List<AccessibilityNodeInfo> viewList = findNodesByViewId(service, SEARCH_ID);
        if (viewList != null && viewList.size() > 0) {
            for (AccessibilityNodeInfo accessibilityNodeInfo : viewList) {
                //微信7.0.4版本特殊处理
                AccessibilityNodeInfo nodeInfo = accessibilityNodeInfo.getParent();
                if (TextUtils.isEmpty(nodeInfo.getContentDescription())) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Thread.sleep(1000);
                    fillInput(service, SEARCH_INPUT_ID, name);
                    Thread.sleep(1000);
                    findViewAndPerformClickParentByText(service, name);
                    Thread.sleep(1000);
                    findViewIdAndPerformClick(service, CHAT_INPUT_ID);
                    Thread.sleep(1000);
                    fillInput(service, CHAT_INPUT_ID, "你好，我是你爸爸。");
                    Thread.sleep(1000);
                    findViewIdAndPerformClick(service, SEND_ID);
                    break;
                }
            }
        }
    }

    /**
     * 填充EditText
     *
     * @param service
     * @param viewId
     * @param content
     */
    private static void fillInput(AccessibilityService service, String viewId, String content) {
        List<AccessibilityNodeInfo> viewList = findNodesByViewId(service, viewId);
        if (viewList != null && viewList.size() > 0) {
            for (AccessibilityNodeInfo accessibilityNodeInfo : viewList) {
                if (accessibilityNodeInfo.getClassName().equals(EDIT_TEXT) && accessibilityNodeInfo.isEnabled()) {
                    ClipData clip = ClipData.newPlainText("label", content);
                    ClipboardManager clipboardManager = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(clip);
                    accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                }
            }
        }
    }

    /**
     * 查找控件并点击其父控件
     *
     * @param service
     * @param text
     */
    private static void findViewAndPerformClickParentByText(AccessibilityService service, String text) {
        List<AccessibilityNodeInfo> viewList = findNodesByText(service, text);
        if (viewList != null && viewList.size() > 0) {
            for (int i = 0; i < viewList.size(); i++) {
                //微信7.0.4版本特殊处理，7.0.4只能从父控件点击，然后这个通过当前页面文案来查找控件，要排除输入框的内容
                AccessibilityNodeInfo node = viewList.get(i);
                AccessibilityNodeInfo parentNode = node.getParent();
                if (node.getText().toString().equals(text) && node.getClassName().equals(TEXT_VIEW)) {
                    parentNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }
    }

    /**
     * 查找控件并点击其父控件
     *
     * @param viewId
     */
    private static void findViewAndPerformClickParent(AccessibilityService service, String viewId) {
        List<AccessibilityNodeInfo> viewList = findNodesByViewId(service, viewId);
        if (viewList != null && viewList.size() > 0) {
            Log.i(TAG, "viewListSize = " + viewList.size());
            Log.i(TAG, "viewList = " + viewList.toString());
            for (int i = 0; i < viewList.size(); i++) {
                //微信7.0.4版本特殊处理
                AccessibilityNodeInfo nodeInfo = viewList.get(i).getParent();
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.i(TAG, "clickParent");
            }
        }
    }

    /**
     * 查找节点并模拟点击
     *
     * @param viewId
     */
    private static void findViewIdAndPerformClick(AccessibilityService service, String viewId) {
        List<AccessibilityNodeInfo> viewList = findNodesByViewId(service, viewId);
        if (viewList != null && viewList.size() > 0) {
            for (AccessibilityNodeInfo accessibilityNodeInfo : viewList) {
                accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    /**
     * 通过viewId查找所有节点
     *
     * @param service
     * @param viewId
     * @return
     */
    private static List<AccessibilityNodeInfo> findNodesByViewId(AccessibilityService service, String viewId) {
        return service.getRootInActiveWindow() != null ? service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId(viewId) : null;
    }

    /**
     * 通过文本查找所有节点
     *
     * @param text
     * @return
     */
    private static List<AccessibilityNodeInfo> findNodesByText(AccessibilityService service, String text) {
        return service.getRootInActiveWindow() != null ? service.getRootInActiveWindow().findAccessibilityNodeInfosByText(text) : null;
    }
}
