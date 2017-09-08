package com.imitationmafengwo.utils.log;

import android.os.Environment;
import android.util.Log;
import android.webkit.ConsoleMessage.MessageLevel;

import com.imitationmafengwo.MyApplication;
import com.imitationmafengwo.utils.DateUtil;
import com.imitationmafengwo.utils.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: xiejm
 * Date: 7/25/13
 * Time: 6:32 PM
 */
public class L {
    private L() {
    }

    private static L instance;
    private static synchronized L shareInstance(){
        if(instance == null){
            instance = new L();
        }
        return instance;
    }

    // webview打log的各种等级
    private static final int WEB_LEVEL_DEBUG = MessageLevel.DEBUG.ordinal();
    private static final int WEB_LEVEL_LOG = MessageLevel.LOG.ordinal();
    private static final int WEB_LEVEL_TIP = MessageLevel.TIP.ordinal();
    private static final int WEB_LEVEL_WARNING = MessageLevel.WARNING.ordinal();
    private static final int WEB_LEVEL_ERROR = MessageLevel.ERROR.ordinal();

    private static final Random mStatRandom = new Random();
    private static String rank = "";
    public static String path = FileUtil.getLogPath();
    private static File file = new File(path + "log.mafengwo.txt");
    /**
     * 日志采用随机数
     */

    /**
     * 上传到友盟的自定义错误，该方法仅在测试版本生效，即是版本号是奇数的时候
     */
//    public static void reportError(String errMsg){
//        int packVersion = PackageUtil.getVersionCodeInt(MyApplication.getApplication());
//        if (packVersion%2 == 0 || TextUtils.isEmpty(errMsg)) return;
//        MobclickAgent.reportError(MyApplication.getApplication(),
//                MyApplication.getFeedBackContent() + errMsg);
//    }

    public static void stack(){
        stack(null);
    }
    public static void stack(String stackMsg){
        i(Log.getStackTraceString(new Throwable(stackMsg==null?"stack dump":stackMsg)));
    }

    private static final int DEFAULT_MESSAGE_LENGTH = 3000;

    interface DivisionCallback{
        void call(String divisionMessage);
    }

    private void synchronizedDivisionMessage(String message, DivisionCallback callback){
        if(message.length() > DEFAULT_MESSAGE_LENGTH){
            synchronized (this){
                int chunkCount = message.length() / DEFAULT_MESSAGE_LENGTH;     // integer division
                for (int i = 0; i <= chunkCount; i++) {
                    int max = DEFAULT_MESSAGE_LENGTH * (i + 1);
                    if (max >= message.length()) {
                        callback.call(message.substring(DEFAULT_MESSAGE_LENGTH * i));
                    } else {
                        callback.call(message.substring(DEFAULT_MESSAGE_LENGTH * i, max));
                    }
                }
            }
        }else{
            callback.call(message);
        }
    }

    private static void divisionMessage(String message, DivisionCallback callback){
        L.shareInstance().synchronizedDivisionMessage(message,callback);
    }

    public static void d(String message, Object... args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_DEBUG);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_DEBUG);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                divisionMessage(message,(divisionMessage) ->Log.d(getTag(), divisionMessage));
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_DEBUG, getTag(), message);
            }
        }
    }

    public static void i(String message, Object... args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_INFO);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_INFO);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                divisionMessage(message,(divisionMessage) ->Log.i(getTag(), divisionMessage));
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_INFO, getTag(), message);
            }
        }

    }

    public static void w(String message, Object... args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_WARN);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_WARN);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                divisionMessage(message,(divisionMessage) ->Log.w(getTag(), divisionMessage));
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_WARN, getTag(), message);
            }
        }
    }

    public static void w(Throwable e) {
        if (LogManager.canPrint(LogManager.LOG_LEVEL_WARN)) {
            e.printStackTrace();
        }
        if (LogManager.canSave(LogManager.LOG_LEVEL_WARN)) {
            save(LogManager.LOG_LEVEL_WARN, getTag(), e.toString());
        }
    }

    public static void e(String message, Object... args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_ERROR);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_ERROR);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                divisionMessage(message,(divisionMessage) ->Log.e(getTag(), divisionMessage));
                //Log.e(getTag(), message);
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_ERROR, getTag(), message);
            }
        }
    }

    public static void e(Throwable e) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_ERROR);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_ERROR);
        if (canPrint || canSave) {
            String message = e.toString();
            if (canPrint) {
                divisionMessage(message,(divisionMessage) ->Log.e(getTag(), divisionMessage));
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_ERROR, getTag(), message);
            }
        }
    }

    public static void v(String message, Object... args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_TRACE);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_TRACE);
//        boolean canSave = MyApplication.getApplication().getPreferences().getBoolean
//                ("writeLogToLocal", false);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                divisionMessage(message,(divisionMessage) ->Log.v(getTag(), divisionMessage));
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_TRACE, getTag(), message);
            }
        }
    }

    public static void t(String message, Object... args) {
        v(message, args);
    }

    public static void f(String message, Object... args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_FATAL);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_FATAL);
//        boolean canSave = MyApplication.getApplication().getPreferences().getBoolean
//                ("writeLogToLocal", false);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                divisionMessage(message,(divisionMessage) ->Log.e(getTag(), divisionMessage));
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_FATAL, getTag(), message);
            }
        }
    }

    public static void s(String message, Object... args) {
        /*
         * 1. debug模式下全部统计
         * 2. release模式下统计概率1%
         */
        if (!LogManager.isDebugMode() && mStatRandom.nextInt(100) != 0) {
            return;
        }
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_STAT);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_STAT);
//        boolean canSave = MyApplication.getApplication().getPreferences().getBoolean
//                ("writeLogToLocal", false);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                divisionMessage(message,(divisionMessage) ->Log.i(getTag(), divisionMessage));
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_STAT, getTag(), message);
            }
        }
    }

    private static String formatMessage(String message, Object... args) {
        if (message == null) {
            return "";
        }
        if (args != null && args.length > 0) {
            try {
                return String.format(message, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    /**
     * 是否处于调试级别
     *
     * @return
     */
    public static boolean isDebugMode() {
        return LogManager.isDebugMode();
    }

    /**
     * 获取native日志tag
     *
     * @return
     */
    private static String getTag() {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[8];

        String className = stackTrace.getClassName();
        StringBuilder sb = new StringBuilder("[MaFengWo]");
        sb.append(className.substring(className.lastIndexOf('.') + 1)).append(".");
        sb.append(stackTrace.getMethodName()).append("#").append(stackTrace.getLineNumber());
        return sb.toString();
    }

    /**
     * 打印到std
     *
     * @param level
     * @param tag
     * @param message
     */
    private static void print(int level, String tag, String message) {
        if (message == null) {
            message = message + "";
        }
        switch (level) {
            case Log.VERBOSE:
                Log.v(tag, message);
                break;

            case Log.DEBUG:
                Log.d(tag, message);
                break;

            case Log.INFO:
                Log.i(tag, message);
                break;

            case Log.WARN:
                Log.w(tag, message);
                break;

            case Log.ERROR:
                Log.e(tag, message);
                break;

        }
    }

    /**
     * 获取level对应的文字描述
     *
     * @param level
     * @return
     */
    public static String getLevelString(int level) {
        switch (level) {
            case LogManager.LOG_LEVEL_TRACE:
                return "TRACE";

            case LogManager.LOG_LEVEL_DEBUG:
                return "DEBUG";

            case LogManager.LOG_LEVEL_INFO:
                return "INFO";

            case LogManager.LOG_LEVEL_WARN:
                return "WARN";

            case LogManager.LOG_LEVEL_ERROR:
                return "ERROR";

            case LogManager.LOG_LEVEL_FATAL:
                return "FATAL";

            default:
                return "";
        }
    }

    /**
     * 保存到发送队列
     *
     * @param level
     * @param tag
     * @param message
     */
    private static void save(int level, String tag, String message) {
        boolean canSave = MyApplication.getApplication().getPreferences().getBoolean("writeLogToLocal", true);
        if (!canSave) return;
        rank = getLevelString(level);
        if (isSDAva()) {
            if (!file.exists()) {
                FileUtil.makeFilePath(path);
            }
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                out.write(rank + " " + DateUtil.getDateStringNow() + " " + tag + " " + message + "\r\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void point(String path, String tag, String msg) {
        if (isSDAva()) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("",
                    Locale.SIMPLIFIED_CHINESE);
            dateFormat.applyPattern("yyyy");
            path = path + dateFormat.format(date) + "/";
            dateFormat.applyPattern("MM");
            path += dateFormat.format(date) + "/";
            dateFormat.applyPattern("dd");
            path += dateFormat.format(date) + ".log";
            dateFormat.applyPattern("[yyyy-MM-dd HH:mm:ss]");
            String time = dateFormat.format(date);
            File file = new File(path);
            if (!file.exists())
                createDipPath(path);
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                out.write(DateUtil.getDateStringNow() + " " + tag + " " + msg + "\r\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据文件路径 递归创建文件
     *
     * @param file
     */
    public static void createDipPath(String file) {
        String parentFile = file.substring(0, file.lastIndexOf("/"));
        File file1 = new File(file);
        File parent = new File(parentFile);
        if (!file1.exists()) {
            parent.mkdirs();
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isSDAva() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                || Environment.getExternalStorageDirectory().exists();
    }

    /**
     * 将h5日志级别转换为Android日志级别
     *
     * @param webLevel
     * @return
     */
    private static int webLevel2LogLevel(int webLevel) {
        if (webLevel == WEB_LEVEL_LOG) {
            return LogManager.LOG_LEVEL_INFO;
        } else if (webLevel == WEB_LEVEL_WARNING) {
            return LogManager.LOG_LEVEL_WARN;
        } else if (webLevel == WEB_LEVEL_ERROR) {
            return LogManager.LOG_LEVEL_ERROR;
        } else if (webLevel == WEB_LEVEL_TIP || webLevel == WEB_LEVEL_DEBUG) {
            return LogManager.LOG_LEVEL_DEBUG;
        } else {
            return 0;
        }
    }

    public static boolean canUploadServer(int level) {
        return LogManager.canUpload(level);
    }
}
