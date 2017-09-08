package com.imitationmafengwo.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class DialogFactory {
    private MessageBox mMessageBox;
    private static DialogFactory sDialogFactory = null;
    private final static String MSG_BOX_FLAG_EXIT = "EXIT";
    private final static String MSG_BOX_FLAG_ASK_REMOVE_APP = "REMOVEAPP";

    public static DialogFactory getInstance() {
        if (sDialogFactory == null) {
            sDialogFactory = new DialogFactory();
        }
        return sDialogFactory;
    }


    /**
     * 这只能是ui线程调用
     */
    public void dismissDialogNow() {
        try{
            if (mMessageBox != null) mMessageBox.dismissDialog();
            mMessageBox = null;
        }catch (Exception e){
//            L.e(Log.getStackTraceString(e));
        }
    }

    public void dismissDialog() {
        Observable.create(f -> {
            if (mMessageBox != null) mMessageBox.dismissDialog();
            mMessageBox = null;
            f.onComplete();
        }).subscribeOn(AndroidSchedulers.mainThread()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(f -> {
        }, e -> e.printStackTrace(), () -> {
        });
    }

    // 显示下载失败提示对话框
    public void showDownloadFailMessageBox(final String msg, final Object tag, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setData(tag);
        mMessageBox.setTitle("温馨提示");
        mMessageBox.setButton1("取消");
        mMessageBox.setButton2("重试");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.show(true, false);
    }

    // 显示取消下载提示对话框
    public void showAskSdMessageBox(final String msg, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setTitle("温馨提示");
        mMessageBox.setButton1("手机");
        mMessageBox.setButton2("SD卡");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.setCheckBoxText("不再提醒");
        mMessageBox.setCheckBoxChecked(true);
        mMessageBox.show(true, true);
    }

    // 显示取消下载提示对话框
    public void showBuyVipMessageBox(final String msg, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setTitle("温馨提示");
        mMessageBox.setButton1("取消");
        mMessageBox.setButton2("去充值");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.show(true, false);
    }

    // 显示取消下载提示对话框
    public void showCancelDownloadMessageBox(final String msg, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setTitle("温馨提示");
        mMessageBox.setButton1("取消");
        mMessageBox.setButton2("确定");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.show(true, false);
    }


    // 显示删除提示对话框
    public void showClearCachMessageBox(final String msg, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setTitle("温馨提示");
        mMessageBox.setButton1("不清除");
        mMessageBox.setButton2("清除");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.show(true, false);
    }


    // 显示删除提示对话框
    public void showDelMessageBox(final String msg, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setTitle("温馨提示");
        mMessageBox.setButton1("取消");
        mMessageBox.setButton2("确定");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.show(true, false);
    }

    // 显示删除提示对话框
    public void showDelMessageBox(final String msg, final Object tag, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setData(tag);
        mMessageBox.setTitle("温馨提示");
        mMessageBox.setButton1("取消");
        mMessageBox.setButton2("确定");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.show(true, false);
    }

    public void showDownloadMessageBox(final String msg, final Object tag, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setData(tag);
        mMessageBox.setTitle("流量提醒");
        mMessageBox.setButton1("我要下载");
        mMessageBox.setButton2("不下载了");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.show(true, false);
    }

    // 显示提示对话框
    public void showTipMessageBox(final String msg, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setTitle("温馨提示");
        mMessageBox.setButton1("确定");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.show(false, false);
    }

    // 显示更新提示对话框
    public void showVersionPromptMessageBox(final String msg, final Object data, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setTitle("版本升级");
        mMessageBox.setButton1("暂不下载");
        mMessageBox.setButton2("立即下载");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.setData(data);
        mMessageBox.show(true, false);
    }


    // 显示更新提示对话框
    public void showVersionPromptMessageBox(final String msg, final String apkUrl, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setTitle("版本升级");
        mMessageBox.setButton1("暂不下载");
        mMessageBox.setButton2("立即下载");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.setData(apkUrl);
        mMessageBox.show(true, false);
    }

    // 显示提示对话框
    public void showStoryMessageBox(final String msg, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setTitle("喜欢本书就收入火袋吧");
        mMessageBox.setButton1("取消");
        mMessageBox.setButton2("去登录");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.show(true, false);
    }

    // 显示不能安装的对话框
    public void showCannotInstallMessageBox(MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox == null) {
            mMessageBox = new MessageBox(context);
        }

        mMessageBox.setTitle("版本升级");
        mMessageBox.setButton1("取消");
        mMessageBox.setButton2("确定");
        mMessageBox.setMessage("当前安装包与现有安装包签名冲突,请卸载现有安装包后再安装.");
        mMessageBox.setFlag(MSG_BOX_FLAG_ASK_REMOVE_APP);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.show(true, false);
    }


    // 显示提示对话框
    public void showMessageBox(final String msg, final String checkTxt, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setTitle("温馨提示");
        mMessageBox.setButton1("取消");
        mMessageBox.setButton2("确定");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        if(!TextUtils.isEmpty(checkTxt)){
            mMessageBox.setCheckBoxText(checkTxt);
            mMessageBox.show(true, true);
        }else {
            mMessageBox.show(true, false);
        }
    }


    // 显示删除提示对话框
    public void showTurnMessageBox(final String msg, final Object tag, MessageBox.OnMessageBoxButtonClickedListener listener, Context context) {
        if (mMessageBox != null) mMessageBox.cancel();
        mMessageBox = new MessageBox(context);
        mMessageBox.setData(tag);
        mMessageBox.setTitle("温馨提示");
        mMessageBox.setButton1("不切换");
        mMessageBox.setButton2("切换");
        mMessageBox.setCancelable(true);
        mMessageBox.setOnMessageBoxButtonClickedListener(listener);
        mMessageBox.setMessage(msg);
        mMessageBox.show(true, false);
    }


}
