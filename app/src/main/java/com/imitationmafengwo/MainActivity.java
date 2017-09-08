package com.imitationmafengwo;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.imitationmafengwo.base.BaseFragmentActivity;
import com.imitationmafengwo.message.Message;
import com.imitationmafengwo.message.MessageCallback;

/**
 * Created by Stu on 2017/9/8.
 */
public class MainActivity extends BaseFragmentActivity implements
        MessageCallback {
    private static MainActivity mainActivity;
    private HomeLayout mHomeLayout;
    public static MainActivity getMainActivity() {
        return mainActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);
        hideTopStateBar();
        ViewGroup container = (ViewGroup) getContentView();

        mHomeLayout = new HomeLayout(this);
        container.addView(mHomeLayout);
    }
    public View getContentView() {
        return findViewById(R.id.container);
    }

    @Override
    protected void onMobResume() {

    }

    @Override
    protected void onMobPause() {

    }

    @Override
    public void onReceiveMessage(Message message) {

    }

    public void initApplicationData() {
        //应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //设置图片缓存大小为maxMemory的1/3
        int cacheSize = maxMemory / 3;
//        mBitmapDrawableCache = new BitmapDrawableCache(cacheSize);
    }
    protected void hideTopStateBar() {
        try {
            topStateBar = findViewById(R.id.mystatebar);
        } catch (Exception e) {
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT && topStateBar != null) {
            topStateBar.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
