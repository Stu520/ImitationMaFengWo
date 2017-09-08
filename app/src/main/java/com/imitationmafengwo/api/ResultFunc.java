
package com.imitationmafengwo.api;

import android.util.Log;

import org.json.JSONObject;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

/**
 * Created by Stu on 7/19/16.
 */
public class ResultFunc implements Function<Result<String>, ApiResult> {

    @Override
    public ApiResult apply(Result<String> result) {
        if (result.isError()) {
//            L.d("=====> error: " + Log.getStackTraceString(result.error()));
            return ApiResult.error(result.error());
        }
        ApiResult res = new ApiResult();

        Response<String> response = result.response();
        okhttp3.Response rawResponse = response.raw();

        res.setRawResponse(rawResponse);
        res.setCode(response.code());
        res.setHeaders(response.headers());
        res.setType(0);

        if (response.isSuccessful()) {
            res.setSuccess(true);
            String body = response.body();
            try {
                if (body == null) {
                    res.setHttpCode(response.code());
                } else {
                    JSONObject j = new JSONObject(body);
                    JSONObject status = j.getJSONObject("status");
                    int httpCode = status.getInt("httpCode");
                    int errorCode = status.getInt("errorCode");
                    String msg = status.getString("msg");
                    Object data = j.get("data");

                    res.setHttpCode(httpCode);
                    res.setErrorCode(errorCode);
                    res.setMsg(msg);
                    res.setData(data);
                }

            } catch (Exception e) {
//                L.d("ResultFunc Exception: " + Log.getStackTraceString(e));
            }

        } else {
            res.setSuccess(false);
            ResponseBody errorBody = response.errorBody();
            try {
                String body = errorBody.string();
                if (body != null) {
                    JSONObject j = new JSONObject(body);
                    JSONObject status = j.getJSONObject("status");
                    int httpCode = status.getInt("httpCode");
                    int errorCode = status.getInt("errorCode");
                    String msg = status.getString("msg");
                    Object data = j.get("data");

                    res.setHttpCode(httpCode);
                    res.setErrorCode(errorCode);
                    res.setMsg(msg);
                    res.setData(data);
                    if (errorCode == 502) { // 用户没登陆
//                        DebugService.shareInstance().log("502 用户没登陆, AccountService.shareInstance().apiLostSessions(rawResponse);.");
//                        AccountService.shareInstance().apiLostSessions(rawResponse);
                    }

                } else {
//                    L.d("=====>  errorBody == null");
                }
            } catch (Exception e) {
//                L.d("ResultFunc Exception: " + Log.getStackTraceString(e));
                if(response.code() == 500){
                    res.setHttpCode(500);
                    res.setErrorCode(500);
                    res.setMsg("服务器开小差了! :( ");
                }
            }
        }
        if(res.getErrorCode() == 502){
//            DebugService.shareInstance().stack();
//            // 需要登录才能访问该资源
//            DebugService.shareInstance().submit();
        }
        return res;
    }
}
