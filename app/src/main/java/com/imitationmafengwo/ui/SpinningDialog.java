package com.imitationmafengwo.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.imitationmafengwo.R;
import com.imitationmafengwo.utils.log.L;

/**
 * Created by Stu on 2017/9/8.
 */
public class SpinningDialog extends Dialog {
    private TextView mMsgTextView;

    public SpinningDialog(Context context, int theme) {
        super(context, theme);
    }

    public SpinningDialog(Activity context, String msg) {
        this(context, msg, false);
    }

    public SpinningDialog(Activity context, String msg, boolean smallDialog) {
        super(context, android.R.style.Theme_NoTitleBar);
        getWindow().setBackgroundDrawableResource(R.color.dialog_page_background);
        int layoutId = smallDialog ? R.layout.small_spinning_dialog : R.layout.spinning_dialog;
        setContentView(layoutId);
        mMsgTextView = (TextView) findViewById(R.id.tvMsg);
        mMsgTextView.setText(msg);
    }

    public SpinningDialog(Activity context, String msg, boolean smallDialog, int bgRes) {
        super(context, android.R.style.Theme_NoTitleBar);
        getWindow().setBackgroundDrawableResource(bgRes > 0? bgRes:R.color.dialog_page_background);
        int layoutId = smallDialog ? R.layout.small_spinning_dialog : R.layout.spinning_dialog;
        setContentView(layoutId);
        mMsgTextView = (TextView) findViewById(R.id.tvMsg);
        mMsgTextView.setText(msg);
    }

    public void setTag(Object tag) {
        mMsgTextView.setTag(tag);
    }

    public Object getTag() {
        return mMsgTextView.getTag();
    }

    public SpinningDialog(Activity context) {
        this(context, null);
    }

    public void setMessage(String msg) {
        mMsgTextView.setText(msg);
    }

    public boolean showDialog() {
        if (!isShowing()) {
            try {
                show();

            } catch (Exception e) {
                L.w(e);
            }
            return true;
        }

        return false;
    }

    public boolean dismissDialog() {
        if (isShowing()) {
            try {
                dismiss();

            } catch (Exception e) {
                L.w(e);
            }

            return true;
        }

        return false;
    }
}
