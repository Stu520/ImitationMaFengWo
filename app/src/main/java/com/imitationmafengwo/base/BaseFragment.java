package com.imitationmafengwo.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.imitationmafengwo.FragmentFactory;
import com.imitationmafengwo.MyApplication;
import com.imitationmafengwo.R;
import com.imitationmafengwo.message.MessagePump;
import com.imitationmafengwo.ui.SpinningDialog;
import com.imitationmafengwo.utils.system.TaskExecutor;

import java.lang.reflect.Field;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Stu on 2017/9/8.
 */
public class BaseFragment extends SupportFragment implements BaseFragmentInterface {
    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_LOADMORE = 2;
    public static final int STATE_NOMORE = 3;
    public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
    protected int mState = STATE_NONE;

    public static final String ARGS_URL = "url";
    private @FragmentFactory.HomePageType int mFragmentType = FragmentFactory.HomePageType.FRAGMENT_TYPE_HOMEPAGE;
    private Bundle mArgs = new Bundle();
    protected View mRootView;
    protected boolean isFirstLoadData = true;

    public static final String ARGS_TAB_INDEX = "args_tab_index";
    public static final String ARGS_H5_PARAMS = "h5Params";

    private SpinningDialog mSpinningDialog;

    protected MyApplication mApp;
    protected MessagePump mMessagePump;

    public @FragmentFactory.HomePageType int getFragmentType() {
        return mFragmentType;
    }

    public void setFragmentType(int fragmentType) {
        this.mFragmentType = fragmentType;
    }

    public View findViewById(int id) {
        return mRootView == null ? null : mRootView.findViewById(id);
    }

    private String getName() {
        return ((Object) this).getClass().getSimpleName();
    }

//    protected BaseFragmentActivity getBaseActivity() {
//        return (BaseFragmentActivity) getActivity();
//    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
//        L.i("BaseFragment# onInflate " + getName());
        super.onInflate(activity, attrs, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//        L.i("BaseFragment# onActivityCreated " + getName());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        L.i("BaseFragment# onActivityResult " + getName());
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Activity activity) {
//        L.i("BaseFragment# onAttach " + getName());
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        L.i("BaseFragment# onCreate " + getName());
        mApp = MyApplication.getApplication();
        mMessagePump = mApp.getMessagePump();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        L.i("BaseFragment# onCreateView " + getName());
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }


    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onDestroy() {
//        L.i("BaseFragment# onDestroy " + getName());
        //让dialog的销毁与activity的生命周期一致，避免出现 java.lang.IllegalArgumentException:
        // View=com.android.internal.policy.impl.PhoneWindow$DecorView not attached to window manager 错误
        dismissWaitDialog();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
//        L.i("BaseFragment# onDestroyView " + getName());
        super.onDestroyView();
        //对于不在缓存区的Fragment才调用反注册。否则。如果Fragment在缓存区，destroy的时候就反注册，将收不到Message，
        //而onCreateView的时候mRootView非空，不会重新创建，可能导致状态错乱
        Activity act = getActivity();
//        if (act instanceof BaseFragmentActivity) {
//            if (!((BaseFragmentActivity) act).isFragmentInCache(this)) {
//                unRegisterMessagePump();
//            }
//        }
        onDestroyFrame();
    }

    public void onDestroyFrame() {
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDetach() {
//        L.i("BaseFragment# onDetach " + getName());
        super.onDetach();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
//        L.i("BaseFragment# onHiddenChanged " + getName() + " hidden " + hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onLowMemory() {
//        L.i("BaseFragment# onLowMemory " + getName());
        super.onLowMemory();
    }

    @Override
    public void onPause() {
//        L.i("BaseFragment# onPause " + getName());
        super.onPause();
        /**
         * 防止
         * java.lang.IllegalArgumentException: View=com.android.internal.policy.impl.PhoneWindow$DecorView{435154c8 V.E..... R.....ID 0,0-1080,1920} not attached to window manager
         */
        dismissWaitDialog();//
    }

    @Override
    public void onResume() {
//        L.i("BaseFragment# onResume " + getName());
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        L.i("BaseFragment# onSaveInstanceState " + getName());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
//        L.i("BaseFragment# onStart " + getName());
        super.onStart();
    }

    @Override
    public void onStop() {
//        L.i("BaseFragment# onStop " + getName());
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        L.i("BaseFragment# onViewCreated " + getName());
        super.onViewCreated(view, savedInstanceState);
        registerMessagePump();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
//        L.i("BaseFragment# onConfigurationChanged " + getName() + " newConfig: " + newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        L.i("BaseFragment# onContextItemSelected " + getName());
        return super.onContextItemSelected(item);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        L.i("BaseFragment# onCreateAnimation " + getName());
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//        L.i("BaseFragment# onCreateContextMenu " + getName());
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        L.i("BaseFragment# onCreateOptionsMenu " + getName());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyOptionsMenu() {
//        L.i("BaseFragment# onDestroyOptionsMenu " + getName());
        super.onDestroyOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        L.i("BaseFragment# onOptionsItemSelected " + getName());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
//        L.i("BaseFragment# onOptionsMenuClosed " + getName());
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        L.i("BaseFragment# onPrepareOptionsMenu " + getName());
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
//        L.i("BaseFragment# onPrepareOptionsMenu " + getName());
        super.onViewStateRestored(savedInstanceState);
    }

    public void setBundleArguments(Bundle args) {
        mArgs = args;
    }

    public Bundle getBundleArguments() {
        return mArgs;
    }

    public void onBackPressed() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }


    }

    /**
     * 显示加载对话框
     *
     * @param strResId
     * @param isSmallStyle
     */
    public void showWaitDialog(final int strResId, final boolean isSmallStyle) {
        Observable.create(f -> {
            Activity activity = getActivity();
            if (activity == null || !MyApplication.getApplication().isHasNetwork()) return;

            if (mSpinningDialog == null) {
                mSpinningDialog = new SpinningDialog(activity, activity.getString(strResId), isSmallStyle);
            }

            if (!mSpinningDialog.isShowing()) {
                mSpinningDialog.showDialog();
            }
            f.onComplete();
        }).subscribeOn(AndroidSchedulers.mainThread()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(f -> {
        }, e ->e.printStackTrace(), () -> {
        });
    }

    /**
     * 显示加载对话框
     *
     * @param strResId
     * @param isSmallStyle
     */
    public void showWaitDialog(final int strResId, final boolean isSmallStyle, final int bgRes) {
        Observable.create(f -> {
            Activity activity = getActivity();
            if (activity == null) return;

            if (mSpinningDialog == null) {
                mSpinningDialog = new SpinningDialog(activity, activity.getString(strResId), isSmallStyle, bgRes);
            }

            if (!mSpinningDialog.isShowing()) {
                mSpinningDialog.showDialog();
            }
            f.onComplete();
        }).subscribeOn(AndroidSchedulers.mainThread()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(f -> {
        }, e -> e.printStackTrace(), () -> {
        });
    }

    public void dismissWaitDialog() {
        Observable.create(f -> {
            if (mSpinningDialog != null && mSpinningDialog.isShowing()) {
                mSpinningDialog.dismissDialog();
            }
            f.onComplete();
        }).subscribeOn(AndroidSchedulers.mainThread()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(f -> {
        }, e -> e.printStackTrace(), () -> {
        });
    }

    public void scrollToTop() {

    }

    /**
     * 处理返回事件
     *
     * @return true表示在fragment内部已经完成了对返回事件的处理，外部不需要再处理;
     * false表示外部需要对返回事件继续处理
     */
    public boolean goBack() {
        return false;
    }

    protected void registerMessagePump() {

    }

    protected void unRegisterMessagePump() {

    }

    public void runOnUiThread(Runnable action) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            TaskExecutor.runTaskOnUiThread(action);
        } else {
            action.run();
        }

    }


    public void showErrorToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            mApp.showToastMessage(R.string.operate_fail_tips);
        } else {
            mApp.showToastMessage(msg);
        }
    }


    /**
     * 在BaseFragment里面取更多菜单的按钮判断展示还是隐藏
     * FIXME 需要所有子界面在命名以及排版上都统一，否则会出现获取不到这个按钮或者标题部分偏右的UI问题
     **/
    private void toggleMoreBtn(int size) {
        if (mRootView != null) {
//            View btnMore = mRootView.findViewById(R.id.btnMore);
//            if (btnMore != null) {
//                btnMore.setVisibility(size > 0 ? View.VISIBLE : View.GONE);
//            }
        }
    }

}
