package com.imitationmafengwo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.imitationmafengwo.message.Message;
import com.imitationmafengwo.message.MessagePump;
import com.imitationmafengwo.service.AppService;
import com.imitationmafengwo.ui.AnimationsToastInfo;
import com.imitationmafengwo.ui.CustomToast;
import com.imitationmafengwo.utils.SharePrefConstant;
import com.imitationmafengwo.utils.SharePreferenceWrapper;
import com.imitationmafengwo.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class MyApplication extends Application {
    private static MyApplication sInstance = null;

    /******************************************************/

    // 裁剪照片拉起摄像头时，可能会kill进程，为了恢复裁剪现场以及尽量减小对现有逻辑的影响，设置mRecreateFlag
    // 注意：在拉起摄像头是被杀后，恢复现场时会先恢复BaseAccountActivity
//	public boolean mRecreateFlag = false;



    private List<String> sdKarts;

    public List<String> getSdkarts() {
        return sdKarts;
    }

    //begin
    //end

    public enum ToastType {
        SUCCESS, ERROR
    }

    @Deprecated
    public static void toastError(String error) {
//		toast(getApplication().getResources().getString(R.string.net_error_tip));// + error);
    }

    @Deprecated
    public static void toastError() {
//		toast(getApplication().getResources().getString(R.string.net_error_tip));// + error);
    }

    private SharedPreferences mSharePref = null;

    public SharedPreferences getPreferences() {
        if (mSharePref == null) {
            int mode = MODE_PRIVATE;
            return mSharePref = new SharePreferenceWrapper(
                    getSharedPreferences(
                            SharePrefConstant.SHARED_PREFERENCE_NAME, mode));

        }
        return mSharePref;
    }

    public SharedPreferences getScreenshotPreferences() {
        if (mSharePref == null) {
            int mode = MODE_PRIVATE;
            return mSharePref = new SharePreferenceWrapper(getSharedPreferences("ss_s_ss_f_ss", mode));

        }
        return mSharePref;
    }


    /**
     * 记录列表上次刷新时间
     */
    public static void putToLastRefreshTime(String key, String value) {
        SharedPreferences preferences = MyApplication.getApplication().getPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取列表的上次刷新时间
     */
    public static String getLastRefreshTime(String key) {
        return MyApplication.getApplication().getPreferences().
                getString(key, StringUtils.getCurTimeStr());
    }


    public static synchronized MyApplication getApplication() {
        return sInstance;
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onTerminate() {
//        AppService.shareInstance().executeOnTerminateApplication();
        super.onTerminate();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
//		L.e("===>>> AuthConfig.init");
//        AuthConfig.init(this,com.sfacg.BuildConfig.apiEnv);
//        if(BuildConfig.publishType == 2){
//            // 友盟实时日志
//            //MobclickAgent.setDebugMode(true);
////            if (LeakCanary.isInAnalyzerProcess(this)) {
////                // This process is dedicated to LeakCanary for heap analysis.
////                // You should not init your app in this process.
////                return;
////            }
//            // 内存泄露
//            //LeakCanary.install(this);
//            if(android.os.Build.VERSION.SDK_INT > 15){
//            }
//        }

//		L.e("===>>> getApplicationContext()");


//		Config.DEBUG = true;
//		Config.REDIRECT_URL = "http://sns.whalecloud.com";
        // image pluns init
        initFresco();
        // greenDAO Database
//        DBService.shareInstance();
//        AccountService.shareInstance();

//	 	System.setProperty("http.keepAlive", "false");
//        NetReceiver.ehList.add(this);  //屏蔽网络不通的主动监听
        //设置该CrashHandler为程序的默认处理器
//        UnCeHandler catchExcep = new UnCeHandler(this);
//        Thread.setDefaultUncaughtExceptionHandler(catchExcep);

//        int with = UIUtils.getDefaultDisplayMetrics(getApplicationContext()).widthPixels;
//        int height = UIUtils.getDefaultDisplayMetrics(getApplicationContext()).heightPixels;
//        int realHeight = UIUtils.getRealDisplayMetrics(getApplicationContext()).heightPixels;
//        screenWith = Math.min(with, height);
//        screenHeight = Math.max(with, height);
//        screenRealHeight = Math.max(with, realHeight);

//        MobclickAgent.openActivityDurationTrack(false);
        AppService.init(this);
//        AppService.shareInstance().executeOnCreateApplication();

        try{
//            initUmPush();
        }catch (Exception e){
            e.printStackTrace();
        }
//        L.e("===>>> initUmPush");
    }


    private void initFresco() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig
//                .newBuilder(getApplicationContext())
//                .setBitmapMemoryCacheParamsSupplier(new LollipopBitmapMemoryCacheParamsSupplier(activityManager))
//                .setDownsampleEnabled(true)
//                .build();
//        Fresco.initialize(getApplicationContext(), imagePipelineConfig);

        //Fresco.initialize(this);
//        if(BuildConfig.publishType == 2){
            //FLog.setMinimumLoggingLevel(FLog.VERBOSE);
//        }
    }



    public String deviceToken;

//    public static int screenRealHeight = 0;//真实的高度，包含了虚拟键的高度


    public boolean isHasNetwork() {
        try {
            ConnectivityManager manager = (ConnectivityManager) getApplication().
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) {
                return false;
            }
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            return !(networkinfo == null || !networkinfo.isAvailable() ||
                    !networkinfo.isConnectedOrConnecting());

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // 手机网络类型
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication
                .getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!StringUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    private ArrayList<Activity> list = new ArrayList<Activity>();

    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     */
    public void removeActivity(Activity a) {
        list.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象
     */
    public void addActivity(Activity a) {
        list.add(a);
    }


    public boolean existMainActivity() {
        for (Activity activity : list) {
            if (null != activity && activity instanceof MainActivity) {
                return true;
            }
        }
        return false;
    }

    public void finishAllButMainActivity() {
        for (Activity activity : list) {
            if (null != activity && !(activity instanceof MainActivity)) {
                activity.finish();
            }
        }
    }

    public static int screenWith = 0;
    public static int screenHeight = 0;
//    public static int screenRealHeight = 0;//真实的高度，包含了虚拟键的高度

    public void showToastMessage(String title) {
        if (TextUtils.isEmpty(title))
            return;
        showToastMessage(title, null, CustomToast.NO_ICON,
                CustomToast.LENGTH_SHORT);
    }

    public void showToastMessage(int titleRes) {
        showToastMessage(getString(titleRes), null, CustomToast.NO_ICON,
                CustomToast.LENGTH_SHORT);
    }

    public void showToastMessage(String title, int iconDrawableId) {
        showToastMessage(title, null, iconDrawableId, CustomToast.LENGTH_SHORT);
    }

    public void showToastMessage(String title, String content,
                                 int iconDrawableId, int duration) {
        AnimationsToastInfo info = new AnimationsToastInfo(title, content,
                duration, iconDrawableId, 0);
        // 如果非特殊情况，主线程初始化,Toast在MainActivity初始化
        getMessagePump().broadcastMessage(Message.Type.SHOW_ANIMATIONS_TOAST,
                info, Message.PRIORITY_NORMAL);

    }
    private MessagePump mMessagePump;

    public MessagePump getMessagePump() {
        if (mMessagePump == null) {
            mMessagePump = new MessagePump();
        }
        return mMessagePump;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
//        L.e("--------------->>>>>>>   memory level=" + level);
    }
}

