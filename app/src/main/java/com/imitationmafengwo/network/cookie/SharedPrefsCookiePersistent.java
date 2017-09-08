package com.imitationmafengwo.network.cookie;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;

public class SharedPrefsCookiePersistent {

    private final SharedPreferences sharedPreferences;

    public SharedPrefsCookiePersistent(Context context) {
        this(context.getSharedPreferences("SFCookiePersistence", Context.MODE_PRIVATE));
    }

    public SharedPrefsCookiePersistent(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public List<Cookie> loadAll() {
        List<Cookie> cookies = new ArrayList<>();

        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            String serializedCookie = (String) entry.getValue();
            Cookie cookie = new SerializableCookie().decode(serializedCookie);
            cookies.add(cookie);
        }
        return cookies;
    }

    public void saveAll(Collection<Cookie> cookies) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Cookie cookie : cookies) {
            if (cookie.persistent()) {
                editor.putString(createCookieKey(cookie), new SerializableCookie().encode(cookie));
            }
        }
        editor.apply();
    }

    public void removeAll(Collection<Cookie> cookies) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Cookie cookie : cookies) {
            editor.remove(createCookieKey(cookie));
        }
        editor.apply();
    }

    private static String createCookieKey(Cookie cookie) {
        return (cookie.secure() ? "https" : "http") + "://" + cookie.domain() + cookie.path() + "|" + cookie.name();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
