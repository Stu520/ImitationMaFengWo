package com.imitationmafengwo.api;

import com.imitationmafengwo.network.cookie.PersistentCookieJar;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Api 封装。
 * Created by Stu on 7/18/16.
 * 一些例子：
 * 1. Android Retrofit 2.0 使用-补充篇 http://wuxiaolong.me/2016/06/18/retrofits/
 * 2.
 */
public class ApiServiceGenerator {

    private static ApiServiceGenerator apiServiceGenerator;

    public synchronized static ApiServiceGenerator shareInstance() {
        if (apiServiceGenerator == null) {
            apiServiceGenerator = new ApiServiceGenerator();
        }
        return apiServiceGenerator;
    }

    public synchronized static void reset() {
        apiServiceGenerator = null;
    }

    private String apiBaseUrl;
    private OkHttpClient.Builder httpClient;
    //private Retrofit.Builder builder;
    private Retrofit.Builder rxBuilder;

    private ApiServiceGenerator() {
//        apiBaseUrl = AuthConfig.shareInstance().getBaseHost();

        PersistentCookieJar cookieJar = PersistentCookieJar.shareInstance();
        httpClient = new OkHttpClient.Builder().cookieJar(cookieJar);
        httpClient.retryOnConnectionFailure(true);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept-Charset", "UTF-8")
                        .header("Authorization", ApiHelper.getAuthorization())
                        .header("Accept", ApiHelper.getAccept())
                        .header("User-Agent", ApiHelper.getUserAgent())
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(ApiHelper.httpLoggingInterceptorLogger);
//        if(DebugService.shareInstance().isOn()){
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        }else{
//            logging.setLevel(SfEnv.apiLogLevel());
//        }
        httpClient.addInterceptor(logging);

        //builder = new Retrofit.Builder().baseUrl(apiBaseUrl).addConverterFactory(GsonConverterFactory.create());
        rxBuilder = new Retrofit.Builder().baseUrl(apiBaseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }


    public static <S> S createService(Class<S> serviceClass) {
        final ApiServiceGenerator apiServiceGenerator = ApiServiceGenerator.shareInstance();

        OkHttpClient client = apiServiceGenerator.httpClient.build();
        Retrofit retrofit = apiServiceGenerator.rxBuilder.client(client).build();
        return retrofit.create(serviceClass);
    }

}


