package com.imitationmafengwo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.imitationmafengwo.MyApplication;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Stu on 2016/11/16.
 */

public class T {
//    /**
//     * 本地化字符（根据简繁开关判断）
//     *
//     * @param string
//     * @return
//     */
//    public static String s(String string) {
//        return AppService.localeString(string);
//    }

    /**
     * 根据字符资源 id 取字符
     *
     * @param resId
     * @return
     */
    public static String rString(int resId) {
        return MyApplication.getApplication().getResources().getString(resId);
    }

    public static String rString(int resId, Object... args) {
        return MyApplication.getApplication().getResources().getString(resId, args);
    }

    private static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static int rColor(int resId) {
        return getColor(MyApplication.getApplication(), resId);
    }

    public static Drawable rDrawable(int resId) {
        return MyApplication.getApplication().getResources().getDrawable(resId);
    }

    public static Bitmap decodeResource(int resId) {
        return BitmapFactory.decodeResource(MyApplication.getApplication().getResources(), resId);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static long longValue(String str) {
        return longValue(str, 0);
    }

    public static long longValue(String str, long def) {
        if (isBlank(str)) {
            return def;
        }
        long v = def;
        try {
            v = Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    public static int intValue(String str) {
        return intValue(str, 0);
    }

    public static int intValue(String str, int def) {
        if (isBlank(str)) {
            return def;
        }
        int v = def;
        try {
            v = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    public static double doubleValue(String str) {
        return doubleValue(str, 0);
    }

    public static double doubleValue(String str, double def) {
        if (isBlank(str)) {
            return def;
        }
        double v = def;
        try {
            v = Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    public static String getReadableFileSize(long fileSize){
        if (fileSize < 1024) {//Bytes/Byte
            if (fileSize > 1) {
                return String.format(Locale.getDefault(),"%d Bytes",fileSize);
            } else {//==1 Byte
                return String.format(Locale.getDefault(), "%d Bytes",fileSize);
            }
        }
        if ((1024 * 1024) > (fileSize) && (fileSize) > 1024) {//K
            return String.format(Locale.getDefault(), "%d K",fileSize/1024);
        }

        if ((1024 * 1024 * 1024) > fileSize && fileSize > (1024 * 1024)) {//M
            return String.format(Locale.getDefault(), "%d.%d M",fileSize / (1024 * 1024), fileSize % (1024 * 1024) / (1024 * 102));
        }
        if (fileSize > (1024 * 1024 * 1024)) {//G
            return String.format(Locale.getDefault(), "%d.%d G", fileSize / (1024 * 1024 * 1024), fileSize % (1024 * 1024 * 1024) / (1024 * 1024 * 102));
        }
        return null;
    }

    public static void showSystemUI(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            view.setSystemUiVisibility(0);
        }
    }

    public static void hideSystemUI(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            view.setSystemUiVisibility(option);
        }
    }

    public static void runOnMainThread(Runnable runnable){
        Observable.create(f->{
            runnable.run();
            f.onComplete();
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(f->{},Throwable::printStackTrace);
    }

    public static void runOnIOThread(Runnable runnable){
        Observable.create(f->{
            runnable.run();
            f.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(f->{},Throwable::printStackTrace);
    }


}
