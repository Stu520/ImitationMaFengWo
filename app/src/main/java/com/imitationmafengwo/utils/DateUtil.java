package com.imitationmafengwo.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String getDateString() {
        String dateStr = new SimpleDateFormat("yyyyMMddHHmmssSSS")
                .format(new Date());
        return dateStr;
    }

    public static String getDateStringDate() {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return dateStr;
    }

    public static String getDateStringDate(Date date) {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return dateStr;
    }

    public static String getDateStringFromInt(String time) {
        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
        time = formatter.format(date);
        //System.out.println(time);
        return time;
    }

    public static String getDateStringHour() {
        String dateStr = new SimpleDateFormat("HH:mm").format(new Date());
        return dateStr;
    }

    public static String getDateStringNow() {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .format(new Date());
        return dateStr;
    }

    public static Date parseExpiresFromDateString(String strDate) {
        Date date = null;
        try {//Fri, 20-May-2016 06:56:16 GMT
            DateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss z", Locale.US);
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }


    public static String parseTFromDateToDate(String strDate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        String dateStr = "";
        if (!TextUtils.isEmpty(strDate)){
            try{
                date = format.parse(strDate);
                if (date != null) dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
            }catch (ParseException e){
                e.printStackTrace();
            }
        }
        return dateStr;
    }

    public static String parseTFromDateToTime(Date date) {
        String dateStr = "";
        if (date != null) dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return dateStr;

    }

    public static String parseTFromDateToTime2(Date date) {
        String dateStr = "";
        if (date != null) dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        return dateStr;

    }

    public static String parseTFromDateToTime(String strDate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        String dateStr = "";
        if (!TextUtils.isEmpty(strDate)){
            try {
                date = format.parse(strDate);
                if (date != null) dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dateStr;

    }

    public static String parseTFromDateToTime2(String strDate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        String dateStr = "";
        if (!TextUtils.isEmpty(strDate)){
            try {
                date = format.parse(strDate);
                if (date != null) dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dateStr;

    }

    public static Date getTodayOffset(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date dateValue(String v, String fm, Date def) {
        if (v == null || v.length() == 0)
            return def;
        try {
            return new SimpleDateFormat(fm).parse(v.trim());
        } catch (Exception e) {
            return def;
        }
    }

    public static Date dateValue(String v, Date def) {
        return dateValue(v, "yyyy-MM-dd", def);
    }

    public static Date datetimeValue(String v, Date def) {
        return dateValue(v, "yyyy-MM-dd HH:mm:ss", def);
    }

    public static String dataString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
    }

    /**
     * api 返回的时间字符串要补上时区 GTM+8
     *
     * @param strDate
     * @return
     */
    public static Date parseApiTimeString(String strDate) {
        if (strDate == null) {
            return null;
        }
        String time = strDate;
        if (time.trim().length() == 19) {
            time = time + "+0800";
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String toIOS8601(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ");
        return format.format(date);
    }

    public static Date fromIOS8601(String str) {
        Date date = null;
        if(str != null){
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ");
                date = format.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static String toIOS8601() {
        return toIOS8601(new Date());
    }

}
