package com.imitationmafengwo.message;

import java.util.Collection;

/**
 * Created by Stu on 8/3/16.
 */
public class SignalMessage<T> {
    private int code;
    private String msg;
    private int arg1;
    private int arg2;
    private T obj;

    public SignalMessage(int code, int arg1, int arg2, String msg, T obj) {
        this.code = code;
        this.msg = msg;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.obj = obj;
    }

    public SignalMessage(int code, int arg1, int arg2, String msg) {
        this.code = code;
        this.msg = msg;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public SignalMessage(int code, int arg1, int arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.code = code;
    }

    public SignalMessage(int code, String msg, T obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }

    public SignalMessage(int code, int arg1, T obj) {
        this.code = code;
        this.arg1 = arg1;
        this.obj = obj;
    }

    public SignalMessage(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public SignalMessage(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public int getArg1() {
        return arg1;
    }

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

    public int getArg2() {
        return arg2;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("code:[").append(code).append("]");
        sb.append("msg:[").append(msg).append("]");
        sb.append("arg1:[").append(arg1).append("]");
        sb.append("arg2:[").append(arg2).append("]");
        if(obj instanceof Collection){
            Collection c = (Collection)obj;
            sb.append("obj.size:[").append(c.size()).append("]");
        }
        sb.append("obj:[").append(obj).append("]");
        return sb.toString();
    }
}
