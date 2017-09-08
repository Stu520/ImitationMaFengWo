package com.imitationmafengwo.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imitationmafengwo.R;


/**
 * Created with IntelliJ IDEA.
 * User: xandy
 * Date: 8/30/12
 * Time: 11:50 AM
 */
public class MessageBox extends Dialog {
    private TextView mTitleView;
    private TextView mMsgView;
    private TextView mMsgViewSecond;
    private Button mButton1;
    private Button mButton2;
    private CheckBox mCheckBox; // default to unchecked and hidden
    private LinearLayout mCheckBoxWarpper;
    private View mLeftSpacer;
    private View mRightSpacer;
    private Object data;

    public MessageBox(Context context) {
        this(context, null, null, null);
    }

    private MessageBox(Context context, String title, String btn1Text, String btn2Text) {
        super(context, android.R.style.Theme_NoTitleBar);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x99000000));
        setContentView(R.layout.message_box);
        mCheckBox = (CheckBox) findViewById(R.id.checkbox);
        mTitleView = (TextView) findViewById(R.id.tvTitle);
        mRightSpacer = findViewById(R.id.rightSpacer);
        mLeftSpacer = findViewById(R.id.leftSpacer);

        if (title != null)
            mTitleView.setText(title);

        final View.OnClickListener onClickListener = new MessageBoxBtnClickListener();
        mButton1 = (Button) findViewById(R.id.messageBoxBtn1);
        mButton1.setOnClickListener(onClickListener);
        mButton1.setTag(this);

        if (btn1Text != null)
            mButton1.setText(btn1Text);

        mButton2 = (Button) findViewById(R.id.messageBoxBtn2);
        mButton2.setOnClickListener(onClickListener);
        mButton2.setTag(this);

        if (btn2Text != null)
            mButton2.setText(btn2Text);

        mMsgView = (TextView) findViewById(R.id.tvMsg);
        mMsgViewSecond = (TextView) findViewById(R.id.tvMsg2);
        mCheckBoxWarpper = (LinearLayout) findViewById(R.id.checkboxwrapper);
    }


    private OnMessageBoxButtonClickedListener mOnMessageBoxButtonClickedListener = null;

    public void setOnMessageBoxButtonClickedListener(OnMessageBoxButtonClickedListener listener) {
        mOnMessageBoxButtonClickedListener = listener;
    }

    private OnMessageBoxButtonClickedListener getOnMessageBoxButtonClickedListener() {
        MessageBox.OnMessageBoxButtonClickedListener listener = mOnMessageBoxButtonClickedListener;
//         Object obj = getTag();
//         if(obj != null && obj instanceof MessageBox.OnMessageBoxButtonClickedListener){
//             listener = (OnMessageBoxButtonClickedListener) obj;
//         }
        return listener;
    }

    private void perormButton1Click() {
        MessageBox.OnMessageBoxButtonClickedListener listener = getOnMessageBoxButtonClickedListener();

        if (listener != null) {
            listener.onCancel(isChechBoxVisible() && isCheckBoxChecked());
        }

        dismissDialog();
    }

    private void perormButton2Click() {
        MessageBox.OnMessageBoxButtonClickedListener listener = getOnMessageBoxButtonClickedListener();

        if (listener != null) {


            listener.onConfirm(isChechBoxVisible() && isCheckBoxChecked(), this.data);
        }

        dismissDialog();
    }

    private final class MessageBoxBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mButton1 == v) {
                perormButton1Click();

            } else if (mButton2 == v) {
                perormButton2Click();
            }
        }
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public void setButton1(String text) {
        mButton1.setText((text));
    }

    public void setButton2(String text) {
        mButton2.setText((text));
    }

    public void setTitle(String text) {
        mTitleView.setText((text));
    }

    public void setMessage(String msg) {
        mMsgView.setText(Html.fromHtml((msg)), TextView.BufferType.SPANNABLE);
        //Add by chenjj4
        //mMsgView.setGravity(Gravity.CENTER);
    }

    public void setSecondMessage(String msg) {
        mMsgViewSecond.setVisibility(View.VISIBLE);
        mMsgViewSecond.setText((msg));
    }

    /**
     * 部分文字需要变色，
     *
     * @param msg         内容
     * @param startIndex  开始变色的下标
     * @param textColorId color的id
     ***/
    public void setMessage(String msg, int startIndex, int textColorId) {
        SpannableStringBuilder style = new SpannableStringBuilder(msg);
        style.setSpan(new ForegroundColorSpan(textColorId), startIndex, style.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mMsgView.setText(style);
    }

    public void setFlag(Object flag) {
        mMsgView.setTag(flag);
    }

    public Object getFlag() {
        return mMsgView.getTag();
    }

    public void setTag(Object tag) {
        mTitleView.setTag(tag);
    }

    public Object getTag() {
        return mTitleView.getTag();
    }

    public void setCheckBoxText(String text) {
        mCheckBox.setText(text);
    }

    public void setCheckBoxChecked(boolean checked) {
        mCheckBox.setChecked(checked);
    }

    public void setCheckBoxVisibility(boolean visible) {
        mCheckBoxWarpper.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public boolean isChechBoxVisible() {
        return mCheckBox.getVisibility() == View.VISIBLE;
    }

    public boolean isCheckBoxChecked() {
        return mCheckBox.isChecked();
    }

    public void setCheckBoxTag(Object tag) {
        mCheckBox.setTag(tag);
    }

    public Object getCheckBoxTag() {
        return mCheckBox.getTag();
    }

    public boolean dismissDialog() {
        if (isShowing()) {
            dismiss();
            return true;
        }

        return false;
    }

    public boolean show(boolean showSecondButton, boolean showCheckBox) {
        if (!isShowing()) {
//            if (!isShowing() && getWindow().getDecorView().getWindowToken() != null) {
            if (showSecondButton) {
                mButton2.setVisibility(View.VISIBLE);
            } else {
                mButton2.setVisibility(View.GONE);
            }

            int spacerVisibility = showSecondButton ? View.GONE : View.VISIBLE;
            mRightSpacer.setVisibility(spacerVisibility);
            mLeftSpacer.setVisibility(spacerVisibility);

            if (showCheckBox) {
                //mCheckBox.setVisibility(View.VISIBLE);
                mCheckBoxWarpper.setVisibility(View.VISIBLE);

            } else {
                //mCheckBox.setVisibility(View.GONE);
                mCheckBoxWarpper.setVisibility(View.GONE);
            }

            try {
                show();

            } catch (Exception e) {
                // WindowManager$BadTokenException will be caught and the app would not display
                // the 'Force Close' message
                // see http://stackoverflow.com/a/9950503/668963
//                L.w(e);
            }

            return true;
        }

        return false;
    }

    public interface OnMessageBoxButtonClickedListener {
        void onConfirm(boolean isChecked, Object data);

        void onCancel(boolean isChecked);
    }
}
