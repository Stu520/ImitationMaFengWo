package com.imitationmafengwo.first;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imitationmafengwo.R;
import com.imitationmafengwo.base.BaseFragment;

/**
 * Created by Stu on 2017/9/8.
 */

public class HomePageFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mfw_activity_first,container,false);
    }
}
