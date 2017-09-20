package com.imitationmafengwo.third;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imitationmafengwo.R;
import com.imitationmafengwo.base.BaseFragment;
import com.imitationmafengwo.bean.Description;
import com.imitationmafengwo.first.FristItemViewModel;
import com.imitationmafengwo.first.RecycleViewAdapter;

/**
 * Created by Stu on 2017/9/8.
 */

public class HotelFragment extends BaseFragment {

    private View rootView;
    private RecyclerView rv;
    private FragmentActivity activity;
    private RecycleViewAdapter recycleViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mfw_activity_third, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recycleViewAdapter = new RecycleViewAdapter(activity);
        ObservableList<FristItemViewModel> list = new ObservableArrayList<>();
        for (int i=0;i<10;i++){
            list.add(rebuildViewModel());
        }
        recycleViewAdapter.setList(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(recycleViewAdapter);
        return rootView;
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
        descriptions.setDescription("椅子");
        descriptions.setCp("张丽");
        descriptions.setPrice("100RMB");
        descriptions.setTel("13285697561");
        descriptions.setUpdateTime("2017-9-10");
        num++;
        return descriptions;
    }
}
