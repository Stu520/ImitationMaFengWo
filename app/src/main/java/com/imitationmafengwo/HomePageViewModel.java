package com.imitationmafengwo;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.imitationmafengwo.base.BaseViewModel;

/**
 * Created by Stu on 2017/9/10.
 */

public class HomePageViewModel extends BaseViewModel {
    public final ObservableField<String> firstTitle = new ObservableField<>("首页");
    public final ObservableField<String> secondTitle = new ObservableField<>("目的地");
    public final ObservableField<String> thirdTitle = new ObservableField<>("酒店");
    public final ObservableField<String> fourthTitle = new ObservableField<>("旅行商城");
    public final ObservableField<String> fifthTitle = new ObservableField<>("我的");

    public final ObservableBoolean puctionBack = new ObservableBoolean(false);

    public HomePageViewModel() {
    }

    public void setPuctionBack(boolean isPuctionBack){
        puctionBack.set(isPuctionBack);
        if (isPuctionBack){
            secondTitle.set("添加");
            thirdTitle.set("修改");
        }else {
            secondTitle.set("目的地");
            thirdTitle.set("酒店");
        }
    }
}
