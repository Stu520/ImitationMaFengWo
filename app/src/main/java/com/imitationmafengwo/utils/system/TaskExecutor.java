package com.imitationmafengwo.utils.system;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 简单线程操作，立即执行的线程，和common那个TaskExecutor不同，common那个可以控制线程池的数量并超出线程数量后要等待60秒才能加入新线程
 * User: Stu
 * Date: 8/16/12
 * Time: 11:46 AM
 */

// an asynchronous task executor(thread pool)
public class TaskExecutor {
    private static ExecutorService sThreadPoolExecutor = null;
    private static ScheduledThreadPoolExecutor sScheduledThreadPoolExecutor = null;
    private static Handler sMainHandler = null;

    public static void executeTask(Runnable task) {
        ensureThreadPoolExecutor();
        sThreadPoolExecutor.execute(task);
    }

    public static <T> Future<T> submitTask(Callable<T> task) {
        ensureThreadPoolExecutor();
        return sThreadPoolExecutor.submit(task);
    }

    public static ScheduledFuture<?> scheduleTask(long delay, Runnable task) {
        ensureScheduledThreadPoolExecutor();
        return sScheduledThreadPoolExecutor.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

//    public static void scheduleTaskAtFixedRateIgnoringTaskRunningTime(long initialDelay, long period, Runnable task) {
//        ensureScheduledThreadPoolExecutor();
//        sScheduledThreadPoolExecutor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
//    }
//
//    public static void scheduleTaskAtFixedRateIncludingTaskRunningTime(long initialDelay, long period, Runnable task) {
//        ensureScheduledThreadPoolExecutor();
//        sScheduledThreadPoolExecutor.scheduleWithFixedDelay(task, initialDelay, period, TimeUnit.MILLISECONDS);
//    }

    public static void scheduleTaskOnUiThread(long delay, Runnable task) {
        ensureMainHandler();
        sMainHandler.postDelayed(task, delay);
    }

    public static void runTaskOnUiThread(Runnable task) {
        ensureMainHandler();
        sMainHandler.post(task);
    }

    private synchronized static void ensureMainHandler() {
        if (sMainHandler == null)
            sMainHandler = new Handler(Looper.getMainLooper());
    }

    private synchronized static void ensureThreadPoolExecutor() {
        if (sThreadPoolExecutor == null) {
            sThreadPoolExecutor = Executors.newFixedThreadPool(20);
//            sThreadPoolExecutor = new ThreadPoolExecutor(5, 5,
//                    60L, TimeUnit.SECONDS,
//                    new LinkedBlockingQueue<Runnable>(),
//                    Executors.defaultThreadFactory());
        }
    }

    private synchronized static void ensureScheduledThreadPoolExecutor() {
        if (sScheduledThreadPoolExecutor == null) {
            sScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(5);
        }
    }

    public static void shutdown() {
        if (sThreadPoolExecutor != null) {
            sThreadPoolExecutor.shutdown();
            sThreadPoolExecutor = null;
        }

        if (sScheduledThreadPoolExecutor != null) {
            sScheduledThreadPoolExecutor.shutdown();
            sScheduledThreadPoolExecutor = null;
        }
    }
}
