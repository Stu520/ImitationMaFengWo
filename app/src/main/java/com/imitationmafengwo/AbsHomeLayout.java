package com.imitationmafengwo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.imitationmafengwo.base.BaseFragment;
import com.imitationmafengwo.base.BaseFragmentActivity;


public abstract class AbsHomeLayout extends LinearLayout {

    private @FragmentFactory.HomePageType int mCurrentFragmentType = FragmentFactory.HomePageType.FRAGMENT_TYPE_HOMEPAGE;

    public AbsHomeLayout(Context context) {
        super(context);
    }

    public AbsHomeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Fragment replaceFragment(int fragmentType) {
        return replaceFragment(fragmentType, null);
    }

    /**
     * 将指定fragment类型对应到Fragment替换调当前Fragment，并清除BackStack
     *
     * @param fragmentType
     * @param args
     */
    public Fragment replaceFragment(int fragmentType, Bundle args) {
        FragmentManager fragmentManager = ((BaseFragmentActivity) getContext()).getSupportFragmentManager();
        FragmentTransaction fT = fragmentManager.beginTransaction();

        BaseFragment fragment = ((BaseFragmentActivity) getContext()).getFragmentFactory().getFragment(fragmentType, true);
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
            BaseFragment oldFragment = ((BaseFragmentActivity) getContext()).getFragmentFactory().getFragment(mCurrentFragmentType, true);

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
        return ((BaseFragmentActivity) getContext()).getFragmentFactory().getFragmentFromCache(fragmentType);
    }

    public BaseFragment getCurrentFragment() {
        return findFragment(mCurrentFragmentType);
    }

    public int getCurrentFragmentType() {
        return mCurrentFragmentType;
    }
}
