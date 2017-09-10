package com.imitationmafengwo.base;


import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;

import com.imitationmafengwo.message.SignalMessage;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Stu on 8/3/16.
 */
public class BaseViewModel extends BaseObservable {
    public long id;
    private boolean postLock = true;
    public final ObservableBoolean isRefreshing = new ObservableBoolean();
    private PublishSubject<SignalMessage> loadSubject = PublishSubject.create();

    public Observable<SignalMessage> loadSignal() {
        return loadSubject;
    }

    public void sendSignal(int code, int arg1, int arg2) {
        this.loadSubject.onNext(new SignalMessage(code, arg1, arg2));
    }

    public void sendSignal(int code, int arg1, Object obj) {
        this.loadSubject.onNext(new SignalMessage(code, arg1, obj));
    }

    public void sendSignal(int code, String msg, Object obj) {
        this.loadSubject.onNext(new SignalMessage(code, msg, obj));
    }

    public void sendSignal(int code, String msg) {
        this.loadSubject.onNext(new SignalMessage(code, msg));
    }

    public void sendSignal(int code) {
        this.loadSubject.onNext(new SignalMessage(code));
    }

    public void sendSignal(SignalMessage signalMessage) {
        this.loadSubject.onNext(signalMessage);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void close() {

    }

    public boolean isPostLock() {
        return postLock;
    }

    public void setPostLock(boolean postLock) {
        this.postLock = postLock;
    }
}
