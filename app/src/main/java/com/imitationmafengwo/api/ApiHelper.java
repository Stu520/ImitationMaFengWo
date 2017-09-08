package com.imitationmafengwo.api;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.imitationmafengwo.network.cookie.PersistentCookieJar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.adapter.rxjava2.Result;

/**
 * Created by Stu on 7/19/16.
 */
public class ApiHelper {

//    public static Disposable wrap(Observable<ApiResult> o, Consumer s) {
//        return o.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(s);
//    }

    public static Observable<ApiResult> wrap(Observable<Result<String>> o) {
        return o.map(new ResultFunc());
    }

    public static Observable<ApiResult> headWrap(Observable<Result<Void>> o) {
        return o.map(new HeadResultFunc());
    }


    //--------------------------------------------------------------
    public static Map<String, String> getHeaders(String url){
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept-Charset", "UTF-8");
        headers.put("Authorization", ApiHelper.getAuthorization());
        headers.put("Accept", ApiHelper.getAccept());
        headers.put("User-Agent", ApiHelper.getUserAgent());
        if(url != null){
            String cookie = ApiHelper.getCookie(url);
            if(cookie != null){
                headers.put("Cookie", cookie);
            }
        }
        return headers;
    }

    // User-Agent boluobao/2.0.04(android;25)/homePage
    private static String userAgent = null;
    public static String getUserAgent(){
//        if(userAgent == null){
//            String curVersion = PackageUtil.getVersionName(SfReaderApplication.getApplication());
//            String channelName = PackageUtil.getChannelValue(SfReaderApplication.getApplication());
//            userAgent = "boluobao" + "/" + curVersion + "(android;" + android.os.Build.VERSION.SDK_INT + ")" + "/" + channelName;
//        }
        return userAgent;
    }

    public static String getAccept(){
        return "application/vnd.sfacg.api+json;version=1";
    }

    // Authorization
    private static String authorization = null;
    public static String getAuthorization(){
        if(authorization == null){
//            String authorSign = AuthConfig.shareInstance().getAuthKey();
            String authorSign = "";
            authorization = "Basic " + authorSign;
        }
        return authorization;
    }

    public static @Nullable
    String getCookie(String url){
        if(TextUtils.isEmpty(url)){
            return null;
        }
        PersistentCookieJar cookieJar = PersistentCookieJar.shareInstance();
        List<Cookie> cookies = cookieJar.loadForRequest(HttpUrl.parse(url));
        if (!cookies.isEmpty()) {
            return cookieHeader(cookies);
        }

        return null;
    }

    private static String cookieHeader(List<Cookie> cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        return cookieHeader.toString();
    }

    public static HttpLoggingInterceptor.Logger httpLoggingInterceptorLogger = (message)->{
//        L.i(message);
//        if(DebugService.shareInstance().isOn()){
//            DebugService.shareInstance().log(message);
//        }
    };


}
