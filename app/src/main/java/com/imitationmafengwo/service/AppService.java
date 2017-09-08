package com.imitationmafengwo.service;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.imitationmafengwo.MyApplication;
import com.imitationmafengwo.utils.log.L;

import java.util.Locale;

/**
 * Created by Stu on 2017/9/8.
 */

public class AppService {
    private Context context;

    private static AppService instance;

    public static synchronized void init(Context ctx){
        AppService.shareInstance(ctx);
    }

    private static synchronized AppService shareInstance(Context ctx){
        if(instance == null){
            instance = new AppService(ctx);
        }
        return instance;
    }

    public static synchronized AppService shareInstance(){
        if(instance == null){
            instance = new AppService(MyApplication.getApplication());
        }
        return instance;
    }

    private AppService(Context context){
        this.context = context;
    }
//    public static String localeString(String str){
//        if(TextUtils.isEmpty(str)){
//            return str;
//        }
//        if(AppService.shareInstance().isTC()){
//            return s2t(str);
//        }else{
//            return str;
//        }
//
//    }
    private static final String isTCKey = "is.tc";
    private int istc = -1;

    public boolean isTC(){ // 是否繁体
        if(istc == -1){
//            String isTCStr = SettingService.shareInstance().getValue(isTCKey);
            String isTCStr = null;
            L.e("--> isTCStr ="+isTCStr);
            if(isTCStr == null){
                boolean tc = false;
                String c= Locale.getDefault().getCountry();
                if(c != null){
                    if(c.equalsIgnoreCase("HK") || c.equalsIgnoreCase("SG") || c.equalsIgnoreCase("TW")|| c.equalsIgnoreCase("MO")){
                        tc = true;
                    }
                }
                L.e("--> isTCStr = %s",String.valueOf(tc?1:0));
//                SettingService.shareInstance().saveString(isTCKey,tc?"1":"0");
                istc = tc?1:0;
            }else{
                try{
                    Integer i = Integer.parseInt(isTCStr);
                    if(i!=null && (i==0 || i==1)){
                        istc = i;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(istc == -1){

                    L.e("--> isTCStr == -1 set istc = %s",0);
//                    SettingService.shareInstance().saveString(isTCKey,"0");
                    istc = 0;
                }
            }
        }
        return istc == 1;
    }

    public void switchTC(boolean tc){
        switchTC(tc, true);
    }

    public void switchTC(boolean tc, boolean rebuildUI){
        L.e("--> tc = %s",tc);
        int i = tc?1:0;
        if(istc == i){
            return;
        }
        istc = i;
//        SettingService.shareInstance().saveString(isTCKey,tc?"1":"0");
        switchLocale();
        if(rebuildUI){
//            MyApplication.getApplication().rebuildAllUI();
        }
    }

    private void switchLocale(){
        Resources resources = MyApplication.getApplication().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        config.locale =  isTC()? Locale.TAIWAN:Locale.PRC;
        resources.updateConfiguration(config, dm);
    }

    /**
     * Simplified Chinese to Traditional Chinese 簡體到繁體
     * @param str
     * @return
     */
//    public static String s2t(String str){
//        if(AppService.shareInstance().getConverter() == null){
//            return str;
//        }else {
//            return AppService.shareInstance().getConverter().s2t(str);
//        }
//    }

    private int tc; // 1 繁体 0 简体
//    private OpenCCConverter converter;
//    private boolean intOpenCCConverter = false;
//    private OpenCCConverter getConverter(){
//        if(converter == null && !intOpenCCConverter){
//            try{
//                converter = new OpenCCConverter();
//                converter.init(MyApplication.getApplication());
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            intOpenCCConverter = true;
//        }
//        return converter;
//
//    }

}
