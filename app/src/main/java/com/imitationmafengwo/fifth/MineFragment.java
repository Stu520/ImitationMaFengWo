package com.imitationmafengwo.fifth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imitationmafengwo.R;
import com.imitationmafengwo.base.BaseFragment;

/**
 * Created by Stu on 2017/9/8.
 */

public class MineFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mfw_activity_fifth,container,false);
    }
}
