package com.imitationmafengwo.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.imitationmafengwo.FragmentFactory;
import com.imitationmafengwo.MyApplication;
import com.imitationmafengwo.R;
import com.imitationmafengwo.message.MessagePump;
import com.imitationmafengwo.ui.DialogFactory;
import com.imitationmafengwo.ui.SpinningDialog;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Stu on 7/16/14.
 */
public abstract class BaseFragmentActivity extends BaseActivity {
    private boolean postLock = true;
    protected View topStateBar;
    private SpinningDialog mSpinningDialog;

    protected MessagePump mMessagePump;

    protected void registerMessages() {
    }

    protected void unRegisterMessages() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if (L.isDebugMode()) {
        //    FragmentManager.enableDebugLogging(true);
        //}
        super.onCreate(savedInstanceState);
        try{
//            PushAgent.getInstance(this).onAppStart();
        }catch (OutOfMemoryError error){}
        mMessagePump = MyApplication.getApplication().getMessagePump();
        registerMessages();
        //让Application持有该子类实例
        MyApplication.getApplication().addActivity(this);

        //判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onDestroy() {
        DialogFactory.getInstance().dismissDialogNow();
        super.onDestroy();
        if (mSpinningDialog != null && mSpinningDialog.isShowing()) {
            mSpinningDialog.dismissDialog();
        }
        MyApplication.getApplication().removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onMobResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        onMobPause();
        if (isFinishing()) unRegisterMessages();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void hideTopStateBar() {
        try {
            topStateBar = findViewById(R.id.mystatebar);
        } catch (Exception e) {
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT && topStateBar != null) {
            topStateBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1){//非默认值
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res != null && res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * 显示加载对话框
     *
     * @param strResId
     * @param isSmallStyle
     */
    public void showWaitDialog(int strResId, boolean isSmallStyle) {
        if (mSpinningDialog == null) {
            mSpinningDialog = new SpinningDialog(this, this.getString(strResId), isSmallStyle);
        }else{
            mSpinningDialog.setMessage(this.getString(strResId));
        }

        if (!mSpinningDialog.isShowing()) {
            mSpinningDialog.showDialog();
        }
    }

    public void showWaitDialog(String msg, boolean isSmallStyle) {
        if (mSpinningDialog == null) {
            mSpinningDialog = new SpinningDialog(this, msg, isSmallStyle);
        }else{
            mSpinningDialog.setMessage(msg);
        }

        if (!mSpinningDialog.isShowing()) {
            mSpinningDialog.showDialog();
        }
    }

    /**
     * 显示加载对话框
     *
     * @param strResId
     * @param isSmallStyle
     */
    public void showWaitDialog(int strResId, boolean isSmallStyle, int bgRes) {
        Observable.create(f -> {
            if (mSpinningDialog == null) {
                mSpinningDialog = new SpinningDialog(this, this.getString(strResId), isSmallStyle, bgRes);
            }else{
                mSpinningDialog.setMessage(this.getString(strResId));
            }

            if (!mSpinningDialog.isShowing()) {
                mSpinningDialog.showDialog();
            }
            f.onComplete();
        }).subscribeOn(AndroidSchedulers.mainThread()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(f -> {
        }, e ->e.printStackTrace(), () -> {
        });
    }

    public void dismissWaitDialog() {
        Observable.create(f -> {
            if (mSpinningDialog != null && mSpinningDialog.isShowing()) {
                mSpinningDialog.dismissDialog();
            }
            f.onComplete();
        }).subscribeOn(AndroidSchedulers.mainThread()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(f -> {
        }, e -> e.printStackTrace(), () -> {
        });
    }

    public boolean isPostLock() {
        return postLock;
    }

    public void setPostLock(boolean postLock) {
        this.postLock = postLock;
    }
    private FragmentFactory mFragmentFactory;

    public FragmentFactory getFragmentFactory() {
        if (mFragmentFactory == null) {
            mFragmentFactory = new FragmentFactory(this);
        }
        return mFragmentFactory;
    }
    /**
     * 此函数用于写友盟统计代码
     */
    abstract protected void onMobResume();

    /**
     * 此函数用于写友盟统计代码
     */
    abstract protected void onMobPause();

}
