package com.example.ben.recipebook.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ben.recipebook.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class StoreCupboardItemAdapter extends SelectableAdapter<StoreCupboardItemViewHolder>{

    private List<String> items = Collections.emptyList();
    private StoreCupboardItemViewHolder.ClickListener clickListener;

    public StoreCupboardItemAdapter(StoreCupboardItemViewHolder.ClickListener clickListener) {
        super();
        this.clickListener = clickListener;
    }

    public void setItems(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(String item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        for(int i = position; i < items.size() + 1; i++){
            notifyItemChanged(i);
        }
    }

    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            items.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
        for(int i = positionStart + itemCount; i < items.size() + 1; i++){
            notifyItemChanged(i);
        }
    }

    @Override
    public StoreCupboardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.template_store_cupboard_item, parent, false);

        return new StoreCupboardItemViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(StoreCupboardItemViewHolder holder, int position) {
        String text = items.get(position);

        holder.itemName.setText(text);
        int color = position % 2 == 0 ? R.color.light_blue_50 : R.color.white;
        holder.itemName.setBackgroundResource(color);

        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<String> getItems(){
        return items;
    }
}
