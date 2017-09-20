package com.imitationmafengwo.activity;

import android.databinding.ObservableField;

import com.imitationmafengwo.base.BaseViewModel;
import com.imitationmafengwo.bean.Description;

/**
 * Created by Stu on 2017/9/20.
 */

public class DetailViewModel extends BaseViewModel {
//    String description;
//    int item_no;
//    String color;
//    String size;
//    String price;//RMB,$
//    String MOQ;
//    String updateTime;//T O U
//    String supplier;//供应商
//    String cp;//联系人
//    String tel;//联系电话
//    String web;//网站
    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableField<String> item_no = new ObservableField<>();
    public final ObservableField<String> color = new ObservableField<>();
    public final ObservableField<String> size = new ObservableField<>();
    public final ObservableField<String> price = new ObservableField<>();
    public final ObservableField<String> MOQ = new ObservableField<>();
    public final ObservableField<String> updateTime = new ObservableField<>();
    public final ObservableField<String> supplier = new ObservableField<>();
    public final ObservableField<String> cp = new ObservableField<>();
    public final ObservableField<String> tel = new ObservableField<>();
    public final ObservableField<String> web = new ObservableField<>();

    private Description item;

    public void setItem(Description item) {
        this.item = item;
        if (item != null){
            description.set(item.getDescription());
            item_no.set(item.getItem_no()+"");
            color.set(item.getColor());
            size.set(item.getSize());
            price.set(item.getPrice());
            MOQ.set(item.getMOQ());
            updateTime.set(item.getUpdateTime());
            supplier.set(item.getSupplier());
            cp.set(item.getCp());
            tel.set(item.getTel());
            web.set(item.getWeb());
        }
    }
}
