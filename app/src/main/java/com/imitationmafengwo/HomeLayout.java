package com.imitationmafengwo;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imitationmafengwo.api.ApiResult;
import com.imitationmafengwo.base.PageType;
import com.imitationmafengwo.utils.log.L;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class HomeLayout extends AbsHomeLayout implements View.OnClickListener {
    public static final String ARGS_LAUNCH_PAGE_TYPE = "launchPageType";
    public static final String ARGS_H5_PARAMS = "h5Params";
    public static final String ARGS_URL = "url";

    public RelativeLayout mainTabLayout;
    public RelativeLayout tabContainer1;
    public RelativeLayout tabContainer2;
    public RelativeLayout tabContainer3;
    public RelativeLayout tabContainer4;
    public RelativeLayout tabContainer5;

    private int mIndex = PageType.HOMEPAGE_FRAGMENT_INDEX;

    private Context mContext = null;
    public TextView tvHasNewMessage;//“我的” 红点

    public HomeLayout(Context context) {
        this(context, null);
        mContext = context;
        init();
    }

    public HomeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

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

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.activity_main, this);

        mainTabLayout = (RelativeLayout) findViewById(R.id.main_tab_layout);
        tabContainer1 = (RelativeLayout) findViewById(R.id.main_tab_container1);
        tabContainer2 = (RelativeLayout) findViewById(R.id.main_tab_container2);
        tabContainer3 = (RelativeLayout) findViewById(R.id.main_tab_container3);
        tabContainer4 = (RelativeLayout) findViewById(R.id.main_tab_container4);
        tabContainer5 = (RelativeLayout) findViewById(R.id.main_tab_container5);
        tvHasNewMessage = (TextView) findViewById(R.id.tvHasNewMessage);
        tabContainer1.setOnClickListener(this);
        tabContainer2.setOnClickListener(this);
        tabContainer3.setOnClickListener(this);
        tabContainer4.setOnClickListener(this);
        tabContainer5.setOnClickListener(this);

        initBottomRadioGroup();
    }

    public void setCurrentFragment(Bundle args) {
        if (args == null || args.isEmpty()) {
            return;
        }
        switchToPage(args.getInt(ARGS_LAUNCH_PAGE_TYPE, FragmentFactory.HomePageType.FRAGMENT_TYPE_DESTINATION), null);
    }


    /**
     * 初始化底部栏
     */
    private void initBottomRadioGroup() {

        Observable.create(f -> {
            onTabSelected(mIndex);
//            setTabSelected(mIndex);
            f.onComplete();
        }).subscribeOn(AndroidSchedulers.mainThread()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(ff -> {
        }, e -> e.printStackTrace(), () -> {
        });
    }

    /**
     * 选中切换其他Page
     */
    private void switchToPage(int pageType, Object obj) {

        L.d("Main#******************************Main#MainActivity pageType =  " + pageType);
        Bundle bundle = new Bundle();
        if (obj != null && obj instanceof String) {
            bundle.putString(ARGS_URL, (String) obj);
        }
        try {
            replaceFragment(pageType, bundle);
        } catch (IllegalStateException e) {
            L.e("java.lang.IllegalStateException");
        }
    }

    private void onNotifyCountSuccess(ApiResult f) {
        if (f.isSuccess()) {
            JSONObject data = (JSONObject) f.getData();
            if (data != null) {
                initNotifyCountData(data);
            }
        }
    }

    private void initNotifyCountData(JSONObject data) {
        long newCommentNum = data.optLong("newCommentNum");
        long newAtNum = data.optLong("newAtNum");
        long newFavNum = data.optLong("newFavNum");
        long newFansNum = data.optLong("newFansNum");
        long newPmNum = data.optLong("newPmNum");
        if (newCommentNum > 0 || newFavNum > 0 || newPmNum > 0 || newAtNum > 0 || newFansNum > 0) {
            tvHasNewMessage.setVisibility(VISIBLE);
        } else {
            tvHasNewMessage.setVisibility(GONE);
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

    public void selectedTab(int index) {
        onTabSelected(index);
    }

    public void setTabIndex(int index) {
        mIndex = index;
    }

    public int getTabIndex() {
        return mIndex;
    }



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
}
