package com.example.ben.recipebook.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ben.recipebook.R;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class StoreCupboardItemAdapter extends RecyclerView.Adapter<StoreCupboardItemViewHolder> {

    private List<String> items = Collections.emptyList();

    @Inject
    public StoreCupboardItemAdapter() {
        super();
    }

    public void setItems(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(String item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    @Override
    public StoreCupboardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.template_store_cupboard_item, parent, false);

        return new StoreCupboardItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StoreCupboardItemViewHolder holder, int position) {
        String text = items.get(position);

        holder.itemName.setText(text);
        int color = position % 2 == 0 ? R.color.light_blue_50 : R.color.white;
        holder.itemName.setBackgroundResource(color);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<String> getItems(){
        return items;
    }
}
