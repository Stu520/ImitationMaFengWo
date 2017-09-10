package com.imitationmafengwo;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.imitationmafengwo.base.BaseFragmentActivity;
import com.imitationmafengwo.databinding.ActivityMainBinding;
import com.imitationmafengwo.message.Message;
import com.imitationmafengwo.message.MessageCallback;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Stu on 2017/9/8.
 */
public class MainActivity extends BaseFragmentActivity implements
        MessageCallback {
    private static MainActivity mainActivity;
    private HomeLayout mHomeLayout;
    private HomePageViewModel viewModel;
    private ActivityMainBinding binding;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mHomeLayout = new HomeLayout(this);
        viewModel = new HomePageViewModel();
        binding.setViewModel(viewModel);
        viewModel.setPuctionBack(false);
        mainActivity = this;
        viewModel.loadSignal().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(f->{
                    switch (f.getCode()){
                        case 1:
                            if (viewModel.puctionBack.get()){

                            }else {

                            }
                            break;
                        case 2:
                            if (viewModel.puctionBack.get()){

                            }else {

                            }
                            break;
                        case 3:
                            if (viewModel.puctionBack.get()){

                            }else {

                            }
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                    }

                },Throwable::printStackTrace,()->{});
        hideTopStateBar();
        ViewGroup container = (ViewGroup) getContentView();
        container.addView(mHomeLayout);
    }


    public FrameLayout getContentView() {
        return binding.container;
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
        if (mHomeLayout!=null)mHomeLayout.setBinding(binding);
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
