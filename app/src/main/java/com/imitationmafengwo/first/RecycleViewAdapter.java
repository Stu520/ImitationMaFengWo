package com.imitationmafengwo.first;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imitationmafengwo.R;
import com.imitationmafengwo.databinding.ItemRvBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stu on 2017/9/19.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ItemViewHolder> {

    public final ObservableList<FristItemViewModel> list = new ObservableArrayList<>();
    private Context context;

    public RecycleViewAdapter(Context context) {
        this.context = context;
    }

    public void setList(ObservableList<FristItemViewModel> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setList(FristItemViewModel itemViewModel){
        this.list.add(itemViewModel);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ItemRvBinding binding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
        public void bind(FristItemViewModel itemViewModel){
            binding.setViewModel(itemViewModel);
        }
    }
}
