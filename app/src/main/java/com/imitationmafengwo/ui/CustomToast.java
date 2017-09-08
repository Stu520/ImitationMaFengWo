package com.imitationmafengwo.ui;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imitationmafengwo.MainActivity;
import com.imitationmafengwo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stu on 14-2-20.
 */
public class CustomToast {
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;
    public static final int NO_ICON = 0;

    private static List<AnimationsToastInfo> sPendingToastInfos = new ArrayList<AnimationsToastInfo>();
    private static final Object sPendingLock = new Object();
    private static Toast mToast = null;

    public static void showToast(AnimationsToastInfo info, Context context) {
        showToast(info, context, null);
    }

    public static void showToast(AnimationsToastInfo info, Context context, SpannableStringBuilder stringBuilder) {
        if (info == null) {
            return;
        }

        if (info.showAfterUserGuide && MainActivity.getMainActivity() == null) {
            synchronized (sPendingLock) {
                sPendingToastInfos.add(info);
            }
        } else {
            try {
                showImmediately(info, context, stringBuilder);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    public static void showPendingToast(Context context) {
        synchronized (sPendingLock) {
            for (AnimationsToastInfo info : sPendingToastInfos) {
                showImmediately(info, context, null);
            }
            sPendingToastInfos.clear();
        }
    }

    private static void showImmediately(AnimationsToastInfo info, Context context, SpannableStringBuilder stringBuilder) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout;
        if (mToast == null) {
            mToast = new Toast(context);

            if (info.layoutId == 0) { //默认布局id
                layout = (LinearLayout) inflater.inflate(R.layout.toast_animation, null);
            } else {
                layout = (LinearLayout) inflater.inflate(info.layoutId, null);
            }

            mToast.setView(layout);
        } else {
            layout = (LinearLayout) mToast.getView();
        }

        prepareImageView(context, layout, info);

        TextView titleView = prepareTitle(context, layout, info);

        prepareContent(context, layout, titleView, info, stringBuilder);

        mToast.setDuration(info.duration);
        mToast.show();
    }

    public static void showToast(AnimationsToastInfo info, Context context, SpannableStringBuilder stringBuilder, ViewGroup layoutView) {
        prepareImageView(context, layoutView, info);
        TextView titleView = prepareTitle(context, layoutView, info);
        prepareContent(context, layoutView, titleView, info, stringBuilder);
        Toast toast = new Toast(context);
        toast.setDuration(info.duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layoutView);
        toast.show();
    }

    /**
     * return imageView object for this toast item_fire_shoucang_layout
     *
     * @param context
     * @param layout
     * @param info
     * @return
     */
    private static ImageView prepareImageView(Context context, ViewGroup layout, AnimationsToastInfo info) {
        ImageView imageView = (ImageView) layout.findViewById(R.id.image);
        if (imageView != null) {
            if (info.iconDrawableId == NO_ICON) {
                imageView.setVisibility(View.GONE);
            } else {
                imageView.setBackgroundDrawable(context.getResources().getDrawable(info.iconDrawableId));
            }
        }

        return imageView;
    }

    private static TextView prepareTitle(Context context, ViewGroup layout, AnimationsToastInfo info) {
        TextView title1 = (TextView) layout.findViewById(R.id.title1);
        if (!TextUtils.isEmpty(info.title)) {
            title1.setText(info.title);

        } else {
            title1.setVisibility(View.GONE);
        }

        return title1;
    }

    private static TextView prepareContent(Context context, ViewGroup layout, TextView titleView, AnimationsToastInfo info, SpannableStringBuilder stringBuilder) {
        TextView contentView = (TextView) layout.findViewById(R.id.title2);
        if (!TextUtils.isEmpty(info.content)) {
            if (stringBuilder != null) {
                contentView.setText(stringBuilder);
            } else {
                contentView.setText(info.content);
            }

        } else {
            contentView.setVisibility(View.GONE);
            titleView.setSingleLine(false); //假如text2为空，那么tittle1则被允许换行
        }

        return contentView;
    }

}
