package com.imitationmafengwo.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.imitationmafengwo.R;
import com.imitationmafengwo.base.BaseActivity;
import com.imitationmafengwo.bean.Description;
import com.imitationmafengwo.databinding.MfwActivityDetailBinding;

/**
 * Created by Stu on 2017/9/20.
 */

public class DescriptionDetailActivity extends BaseActivity {


    private Description description;
    private DetailViewModel viewModel;
    private MfwActivityDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        binding = DataBindingUtil.setContentView(this, R.layout.mfw_activity_detail);
        viewModel = new DetailViewModel();
        viewModel.setItem(description);
        binding.setViewModel(viewModel);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        description = (Description) intent.getSerializableExtra("description");
    }
}
