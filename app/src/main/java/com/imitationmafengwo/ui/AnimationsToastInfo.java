package com.imitationmafengwo.ui;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Stu on 14-3-4.
 */
public class AnimationsToastInfo implements Parcelable {

    public String title;
    public String content;
    public int duration;
    public int iconDrawableId;
    public int position;
    public int layoutId;

    /**
     * mark this toast show should after userguide
     */
    public boolean showAfterUserGuide = false;

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIconDrawableId() {
        return iconDrawableId;
    }

    public void setIconDrawable(int iconDrawableId) {
        this.iconDrawableId = iconDrawableId;
    }

    public AnimationsToastInfo(String title, String content, int duration, int iconDrawableId, int layoutId) {
        this.title = title;
        this.content = content;
        this.duration = duration;
        this.iconDrawableId = iconDrawableId;
        this.layoutId = layoutId;
    }

    public AnimationsToastInfo() {
    }

    public static final Creator<AnimationsToastInfo> CREATOR =
            new Creator<AnimationsToastInfo>() {
                @Override
                public AnimationsToastInfo createFromParcel(Parcel source) {
                    AnimationsToastInfo r = new AnimationsToastInfo();
                    r.title = source.readString();
                    r.content = source.readString();
                    r.duration = source.readInt();
                    r.iconDrawableId = source.readInt();
                    r.layoutId = source.readInt();
                    return r;
                }

                @Override
                public AnimationsToastInfo[] newArray(int size) {
                    return new AnimationsToastInfo[size];
                }
            };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeInt(duration);
        dest.writeInt(iconDrawableId);
        dest.writeInt(layoutId);
    }
}