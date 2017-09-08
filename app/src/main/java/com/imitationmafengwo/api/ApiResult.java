package com.imitationmafengwo.api;

import com.imitationmafengwo.R;
import com.imitationmafengwo.utils.T;

import java.util.Date;

import okhttp3.Headers;
import okhttp3.Response;

public class ApiResult {

    private boolean success;
    private int type;

    //
    private Response rawResponse;
    private Headers headers;
    private int code;

    // body content
    // status
    private int httpCode;
    private int errorCode;
    private String msg;
    // data
    private Object data;

    public static ApiResult error(Throwable throwable) {
        String msg = throwable.getLocalizedMessage();
        if (throwable instanceof java.net.UnknownHostException) {
            msg = T.rString(R.string.error_view_network_error_click_to_refresh);
        }
        if(throwable instanceof java.net.SocketTimeoutException){
            msg = T.rString(R.string.api_request_timeout);
        }
        ApiResult apiResult = new ApiResult();
        apiResult.type = 1;
        apiResult.success = false;
        apiResult.msg = msg;
        return apiResult;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Response getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(Response rawResponse) {
        this.rawResponse = rawResponse;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public Date getServerTime() {
        if (this.headers == null) {
            return null;
        }
        return this.headers.getDate("Date");
    }
}
