package com.imitationmafengwo.utils.log;



public class LogManager {

    public static final int LOG_LEVEL_NO = 0x00;
    public static final int LOG_LEVEL_TRACE = 0x01;
    public static final int LOG_LEVEL_DEBUG = 0x02;
    public static final int LOG_LEVEL_INFO = 0x04;
    public static final int LOG_LEVEL_WARN = 0x08;
    public static final int LOG_LEVEL_ERROR = 0x10;
    public static final int LOG_LEVEL_FATAL = 0x20;
    public static final int LOG_LEVEL_STAT = 0x40;
    public static final int LOG_LEVEL_ALL = 0xFF;

    /**
     * 当前日志上传级别
     * 打包测试包时为LOG_LEVEL_NO，即所有级别都不会上传，
     * 打包release包时为LOG_LEVEL_ERROR|LOG_LEVEL_FATAL，即LOG_LEVEL_ERROR和LOG_LEVEL_FATAL级别都会打印
     */
    private int uploadLevel = LOG_LEVEL_ERROR | LOG_LEVEL_FATAL;

    private static LogManager mLogManager;

    public synchronized static LogManager getInstance() {
        if (mLogManager == null) {
            mLogManager = new LogManager();
        }

        return mLogManager;
    }

    LogManager() {
    }

    /**
     * 指定日志级别是否达到打印级别
     *
     * @param level
     * @return
     */
    static boolean canPrint(int level) {
        return (level) > 0;
    }

    /**
     * 指定日志级别是否达到上传级别
     *
     * @param level
     * @return
     */
    static boolean canUpload(int level) {
        return (level & getInstance().uploadLevel) > 0;
    }

    public static boolean canSave(int level) {
        //            return canUpload(level);
        return (level) > 0;
    }

    /**
     * 是否处于调试级别
     *
     * @return
     */
    static boolean isDebugMode() {
        return true;
    }

}
