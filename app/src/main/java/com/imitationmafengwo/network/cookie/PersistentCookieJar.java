package com.imitationmafengwo.network.cookie;

import android.content.SharedPreferences;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.imitationmafengwo.MyApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.internal.http.HttpDate;

public class PersistentCookieJar implements CookieJar {

    private SharedPrefsCookiePersistent persistent;

    private PersistentCookieJar(SharedPrefsCookiePersistent persistent) {
        this.persistent = persistent;
        this.loadPreVersionCookie();
    }

    private static PersistentCookieJar instance;

    public synchronized static PersistentCookieJar shareInstance() {
        if (instance == null) {
            instance = new PersistentCookieJar(new SharedPrefsCookiePersistent(MyApplication.getApplication()));
        }
        return instance;
    }

    @Override
    synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        persistent.saveAll(cookies);
        syncCookieToWebkit(cookies);
    }

    private Cookie preVersionCookie;

    public void loadPreVersionCookie() {

        SharedPreferences p = MyApplication.getApplication().getPreferences();
        // .SFCommunity=6390D4C0A03C8CB643E724E766D7E4977974E99EC79933FA71FDD52A99ACF45F1BDEE091F0AF4E7F4718FC9095840D23055B021D172E6C226A8DD12C7744138474B7BEE2468E360542320F06F208D14B
        String rawCookies = p.getString("cookie", "");
        if (rawCookies.length() > 0) {
            String rawpath = p.getString("rawpath", "");
            String rawexpires = p.getString("rawexpires", "");
            String rawdomain = p.getString("rawdomain", "");
            String[] kv = rawCookies.split("=");

            Cookie.Builder builder = new Cookie.Builder();
            builder = builder.domain(rawdomain);
            builder = builder.path(rawpath);
            if (kv.length == 2) {
                builder = builder.name(kv[0]).value(kv[1]);
            }
            Date d = HttpDate.parse(rawexpires);
            if (d != null) {
                builder = builder.expiresAt(d.getTime());
            }
            preVersionCookie = builder.build();
            doSyncCookie(preVersionCookie);

            // 移除Cookie
            SharedPreferences.Editor e = p.edit();
            e.remove("cookie");
            e.commit();
//            L.e("------->>> sf cookie: %s", this.preVersionCookie.toString());
        } else {
//            L.e("------->>> sf cookie: is null....");
        }
    }

    public void syncCookieToWebkit(List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            doSyncCookie(cookie);
        }
    }

    private void doSyncCookie(Cookie cookie) {
        try {
            CookieSyncManager.createInstance(MyApplication.getApplication());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();
            cookieManager.setCookie("sfacg.com", cookie.toString());
            CookieSyncManager.getInstance().sync();  //强制立即同步cookie
        } catch (Exception e) {

        }
    }

    @Override
    synchronized public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = this.persistent.loadAll();
//        Cookie c = this.getPreVersionCookie();
        if (this.preVersionCookie != null) {
            HashMap<String, Cookie> map = new HashMap<>();
            for (Cookie c : cookies) {
                map.put(c.name(), c);
            }
            if (!map.containsKey(this.preVersionCookie.name())) {
                cookies.add(this.preVersionCookie);
                persistent.saveAll(cookies);
            }
        }
        List<Cookie> removedCookies = new ArrayList<>();
        List<Cookie> validCookies = new ArrayList<>();

        for (Iterator<Cookie> it = cookies.iterator(); it.hasNext(); ) {
            Cookie currentCookie = it.next();

            if (isCookieExpired(currentCookie)) {
                removedCookies.add(currentCookie);
            } else if (currentCookie.matches(url)) {
                validCookies.add(currentCookie);
            }
        }
        persistent.removeAll(removedCookies);
        return validCookies;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    synchronized public void clear() {
        persistent.clear();
    }
}
