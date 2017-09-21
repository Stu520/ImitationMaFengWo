package com.imitationmafengwo.third;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.imitationmafengwo.R;
import com.imitationmafengwo.base.BaseFragment;
import com.imitationmafengwo.bean.Description;
import com.imitationmafengwo.databinding.MfwActivityThirdBinding;
import com.imitationmafengwo.first.FristItemViewModel;
import com.imitationmafengwo.first.RecycleViewAdapter;

/**
 * Created by Stu on 2017/9/8.
 */

public class HotelFragment extends BaseFragment implements TextWatcher {

    private FragmentActivity activity;
    private RecycleViewAdapter recycleViewAdapter;
    private MfwActivityThirdBinding binding;
    private ThirdViewModel viewModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;

    public ObservableList<FristItemViewModel> list = new ObservableArrayList<>();//全部
    public ObservableList<FristItemViewModel> searchResult = new ObservableArrayList<>();//搜索结果


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        list = initNoRealData();
        binding = DataBindingUtil.inflate(inflater,R.layout.mfw_activity_third,container,false);
        viewModel = new ThirdViewModel();
        binding.setViewModel(viewModel);

        recyclerView = binding.recyclerView;
        recycleViewAdapter = new RecycleViewAdapter(activity);

        recycleViewAdapter.setList(list);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recycleViewAdapter);

        binding.searchEditText.addTextChangedListener(this);
        return binding.getRoot();
    }

    @NonNull
    private ObservableList<FristItemViewModel> initNoRealData() {
        ObservableList<FristItemViewModel> list = new ObservableArrayList<>();
        for (int i=0;i<30;i++){
            list.add(rebuildViewModel());
        }
        return list;
    }

    @NonNull
    private FristItemViewModel rebuildViewModel() {
        FristItemViewModel fristItemViewModel2 = new FristItemViewModel();
        Description descriptions = newDescription();
        fristItemViewModel2.setDescriptions(descriptions);
        return fristItemViewModel2;
    }

    private int num = 1;
    @NonNull
    private Description newDescription() {
        Description descriptions = new Description();
        descriptions.setMOQ(num+"");
        descriptions.setColor("紫色");
        if (num == 2){
            descriptions.setDescription("桌子");
        }else if (num %2 == 0){
            descriptions.setDescription("主机");
        }else if (num %3 == 0){
            descriptions.setDescription("电脑");
        }else {
            descriptions.setDescription("椅子");
        }
        descriptions.setCp("张丽");
        descriptions.setPrice("100RMB");
        descriptions.setTel("13285697561");
        descriptions.setUpdateTime("2017-9-10");
        num++;
        return descriptions;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String key = s.toString();
        if (key == null || key.isEmpty()){

        }else {
            ObservableArrayList<FristItemViewModel> tempList = new ObservableArrayList<>();
            for (int i = 0;i<list.size();i++){
                FristItemViewModel fristItemViewModel = list.get(i);
                if (fristItemViewModel != null){
                    Description descriptions = fristItemViewModel.getDescriptions();
                    if (descriptions != null){
                        String description = descriptions.getDescription();
                        if (key.indexOf(description) != -1) tempList.add(fristItemViewModel);
                    }
                }
                recycleViewAdapter.list.clear();
                recycleViewAdapter.setList(tempList);
            }
        }
    }
}
