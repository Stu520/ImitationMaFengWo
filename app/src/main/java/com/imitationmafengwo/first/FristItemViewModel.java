package com.imitationmafengwo.first;

import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import com.imitationmafengwo.activity.DescriptionDetailActivity;
import com.imitationmafengwo.base.BaseViewModel;
import com.imitationmafengwo.bean.Description;

/**
 * Created by Stu on 2017/9/19.
 */

public class FristItemViewModel extends BaseViewModel {
    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableField<String> size = new ObservableField<>();
    public final ObservableField<String> price = new ObservableField<>();
    public final ObservableField<String> moq = new ObservableField<>();
    public final ObservableField<String> updateTime = new ObservableField<>();
    private Description descriptions;

    public void setDescriptions(Description descriptions) {
        if (descriptions != null) {
            this.descriptions = descriptions;
            description.set(descriptions.getDescription());
            size.set(descriptions.getSize());
            price.set(descriptions.getPrice());
            moq.set(descriptions.getMOQ());
            updateTime.set(descriptions.getUpdateTime());
        }
    }

    public View.OnClickListener onClickListener = v ->{
        if (descriptions != null ){
            Intent intent = new Intent();
            intent.setClass(v.getContext(),DescriptionDetailActivity.class);
            intent.putExtra("description",descriptions);
            v.getContext().startActivity(intent);
        }
    };
}
