package com.example.ben.recipebook.android;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ben.recipebook.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StoreCupboardItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener{

    private final ClickListener listener;
    public String item;

    @Bind(R.id.item_name)
    TextView itemName;

    @Bind(R.id.selected_overlay)
    View selectedOverlay;

    public StoreCupboardItemViewHolder(View itemView, ClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.listener = listener;

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClicked(getPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (listener != null) {
            return listener.onItemLongClicked(getPosition());
        }

        return false;
    }

}
