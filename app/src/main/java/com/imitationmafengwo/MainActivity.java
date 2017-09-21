package com.imitationmafengwo;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.imitationmafengwo.base.BaseFragment;
import com.imitationmafengwo.base.BaseFragmentActivity;
import com.imitationmafengwo.base.PageType;
import com.imitationmafengwo.databinding.ActivityMainBinding;
import com.imitationmafengwo.message.Message;
import com.imitationmafengwo.message.MessageCallback;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Stu on 2017/9/8.
 */
public class MainActivity extends BaseFragmentActivity implements
        MessageCallback ,View.OnClickListener{
    private static MainActivity mainActivity;
    private HomePageViewModel viewModel;
    private ActivityMainBinding binding;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new HomePageViewModel();
        binding.setViewModel(viewModel);
        viewModel.setPuctionBack(true);
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
        this.binding.mainTabContainer1.setOnClickListener(this);
        this.binding.mainTabContainer2.setOnClickListener(this);
        this.binding.mainTabContainer3.setOnClickListener(this);
        this.binding.mainTabContainer4.setOnClickListener(this);
        this.binding.mainTabContainer5.setOnClickListener(this);
        onTabSelected(PageType.HOMEPAGE_FRAGMENT_INDEX);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tab_container1: {
                selectedTab(PageType.HOMEPAGE_FRAGMENT_INDEX);
            }
            break;
            case R.id.main_tab_container2: {
                selectedTab(PageType.DESTINATION_FRAGMENT_INDEX);
            }
            break;
            case R.id.main_tab_container3: {
                selectedTab(PageType.HOTEL_FRAGMENT_INDEX);
            }
            break;
            case R.id.main_tab_container4: {
                selectedTab(PageType.SHOPPING_MALL_FRAGMENT_INDEX);
            }
            break;
            case R.id.main_tab_container5: {
                selectedTab(PageType.SETTING_FRAGMENT_INDEX);
            }
            break;
        }
    }

    public void selectedTab(int index) {
        onTabSelected(index);
    }

    public void onTabSelected(int index) {
        switch (index) {
            case PageType.HOMEPAGE_FRAGMENT_INDEX:
                switchToPage(PageType.HOMEPAGE_FRAGMENT_INDEX, null, false);
                break;
            case PageType.DESTINATION_FRAGMENT_INDEX:
                switchToPage(PageType.DESTINATION_FRAGMENT_INDEX, null, false);
                break;
            case PageType.HOTEL_FRAGMENT_INDEX:
                switchToPage(PageType.HOTEL_FRAGMENT_INDEX, null, false);
                break;
            case PageType.SHOPPING_MALL_FRAGMENT_INDEX:
                switchToPage(PageType.SHOPPING_MALL_FRAGMENT_INDEX, null, false);
                break;
            case PageType.SETTING_FRAGMENT_INDEX:
                switchToPage(PageType.SETTING_FRAGMENT_INDEX, null, false);
                break;
            default:
                break;
        }
    }

    private void switchToPage(int pageType, JSONObject h5Params, boolean needRefresh) {
        switch (pageType) {
            case PageType.HOMEPAGE_FRAGMENT_INDEX:
                switchToPage(FragmentFactory.HomePageType.FRAGMENT_TYPE_HOMEPAGE, null);
                break;
            case PageType.DESTINATION_FRAGMENT_INDEX:
                switchToPage(FragmentFactory.HomePageType.FRAGMENT_TYPE_DESTINATION, null);
                break;
            case PageType.HOTEL_FRAGMENT_INDEX:
                switchToPage(FragmentFactory.HomePageType.FRAGMENT_TYPE_HOTEL, null);
                break;
            case PageType.SHOPPING_MALL_FRAGMENT_INDEX:
                switchToPage(FragmentFactory.HomePageType.FRAGMENT_TYPE_SHOPPINGMALL, null);
                break;
            case PageType.SETTING_FRAGMENT_INDEX:
                switchToPage(FragmentFactory.HomePageType.FRAGMENT_TYPE_MINE, null);
                break;

            default:
                break;
        }
    }

    /**
     * 选中切换其他Page
     */
    private void switchToPage(int pageType, Object obj) {

//        L.d("Main#******************************Main#MainActivity pageType =  " + pageType);
        Bundle bundle = new Bundle();
        if (obj != null && obj instanceof String) {
            bundle.putString(ARGS_URL, (String) obj);
        }
        try {
            replaceFragment(pageType, bundle);
        } catch (IllegalStateException e) {
//            L.e("java.lang.IllegalStateException");
        }
    }

    private int mIndex = PageType.HOMEPAGE_FRAGMENT_INDEX;

    private void init() {
        int openTab = 0;
        if (openTab == 0) {
            mIndex = PageType.HOMEPAGE_FRAGMENT_INDEX;
        }
        if (openTab == 1) {
            mIndex = PageType.DESTINATION_FRAGMENT_INDEX;
        }
        if(openTab == 2){
            mIndex = PageType.HOTEL_FRAGMENT_INDEX;
        }
        if (openTab == 3) {
            mIndex = PageType.SHOPPING_MALL_FRAGMENT_INDEX;
        }
        if (openTab == 4) {
            mIndex = PageType.SETTING_FRAGMENT_INDEX;
        }

    }

    public static final String ARGS_URL = "url";

    private @FragmentFactory.HomePageType int mCurrentFragmentType = FragmentFactory.HomePageType.FRAGMENT_TYPE_HOMEPAGE;


    /**
     * 将指定fragment类型对应到Fragment替换调当前Fragment，并清除BackStack
     *
     * @param fragmentType
     * @param args
     */
    public Fragment replaceFragment(int fragmentType, Bundle args) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fT = fragmentManager.beginTransaction();

        BaseFragment fragment = getFragmentFactory().getFragment(fragmentType, true);
        if (args != null) {
            if (fragment instanceof BaseFragment) {
                fragment.setBundleArguments(args);
            } else {
                fragment.setArguments(args);
            }
        }

        boolean useAnim = true;
        if (useAnim) {
            fT.setCustomAnimations(R.anim.open_alpha_in, R.anim.open_alpha_out,
                    R.anim.close_alpha_in, R.anim.close_alpha_out);
        }

        if (mCurrentFragmentType != fragment.getFragmentType()) {
            BaseFragment oldFragment = getFragmentFactory().getFragment(mCurrentFragmentType, true);

            if (oldFragment != null)
                fT.hide(oldFragment);
            if (!fragment.isAdded()) {
                fT.add(R.id.container, fragment, String.valueOf(fragmentType));
            } else {
                fT.show(fragment);
            }
        }


        mCurrentFragmentType = fragmentType;
        fT.commitAllowingStateLoss();

        return fragment;
    }

    public BaseFragment findFragment(int fragmentType) {
        return getFragmentFactory().getFragmentFromCache(fragmentType);
    }

    public BaseFragment getCurrentFragment() {
        return findFragment(mCurrentFragmentType);
    }

    public int getCurrentFragmentType() {
        return mCurrentFragmentType;
    }
}
