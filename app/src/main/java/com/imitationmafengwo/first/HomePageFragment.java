package com.imitationmafengwo.first;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.imitationmafengwo.R;
import com.imitationmafengwo.base.BaseFragment;

/**
 * Created by Stu on 2017/9/8.
 */

public class HomePageFragment extends BaseFragment {

    private View rootView;
    private RecyclerView rv;
    private FragmentActivity activity;
    private RecycleViewAdapter recycleViewAdapter;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mfw_activity_first, container, false);
        return rootView;
    }

}
