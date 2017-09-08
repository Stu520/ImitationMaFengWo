package com.imitationmafengwo;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.SparseArray;

import com.imitationmafengwo.base.BaseFragment;
import com.imitationmafengwo.fifth.MineFragment;
import com.imitationmafengwo.first.HomePageFragment;
import com.imitationmafengwo.fourth.ShoppingMallFragment;
import com.imitationmafengwo.second.DestinationFragment;
import com.imitationmafengwo.third.HotelFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by Stu on 2017/9/8.
 */
public class FragmentFactory {
    @IntDef({
            HomePageType.FRAGMENT_TYPE_HOMEPAGE,
            HomePageType.FRAGMENT_TYPE_DESTINATION,
            HomePageType.FRAGMENT_TYPE_HOTEL,
            HomePageType.FRAGMENT_TYPE_SHOPPINGMALL,
            HomePageType.FRAGMENT_TYPE_MINE,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface HomePageType {
        int FRAGMENT_TYPE_HOMEPAGE = 1004; //主页
        int FRAGMENT_TYPE_DESTINATION = 1005;//目的地
        int FRAGMENT_TYPE_HOTEL = 1006;//酒店
        int FRAGMENT_TYPE_SHOPPINGMALL = 1007;//旅行商城
        int FRAGMENT_TYPE_MINE = 1008;//我的
    }

    private SparseArray<BaseFragment> mFragmentCache = new SparseArray<BaseFragment>();

    private Context mContext;

    public FragmentFactory(Context context) {
        mContext = context;
    }

    public BaseFragment getFragment(@HomePageType int type, boolean useCache) {
        BaseFragment fragment = null;

        if (useCache && (fragment = mFragmentCache.get(type)) != null) {
//            L.d("Hit the Fragment Cache, Fragment: %d, Type: %d", fragment.hashCode(), type);
            return fragment;
        }
//        L.d("FragmentFactory create fragment, type: " + type + " useCache: " + useCache);
        switch (type) {
            case HomePageType.FRAGMENT_TYPE_HOMEPAGE:
                fragment = new HomePageFragment();
                break;
            case HomePageType.FRAGMENT_TYPE_DESTINATION:
                fragment = new DestinationFragment();
                break;
            case HomePageType.FRAGMENT_TYPE_HOTEL:
                fragment = new HotelFragment();
                break;
            case HomePageType.FRAGMENT_TYPE_SHOPPINGMALL:
                fragment = new ShoppingMallFragment();
                break;
            case HomePageType.FRAGMENT_TYPE_MINE:
                fragment = new MineFragment();
                break;
            default:
                throw new RuntimeException(String.format("Can not find fragment with type: %d", type));
        }

        if (fragment != null) {
            fragment.setFragmentType(type);

            if (useCache) {
//                L.d("Set the Fragment Cache, Fragment: %d, Type: %d", fragment.hashCode(), type);
                mFragmentCache.put(type, fragment);
            }
        }
        return fragment;
    }

    public BaseFragment getFragmentFromCache(int type) {
        BaseFragment fragment = mFragmentCache.get(type);
        return fragment;
    }

    public void removeFragment(int type) {
        mFragmentCache.remove(type);
    }

    public void clearCache() {
        mFragmentCache.clear();
    }
}
