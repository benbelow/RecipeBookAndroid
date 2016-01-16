package com.example.ben.recipebook.android;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ben.recipebook.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StoreCupboardItemViewHolder extends RecyclerView.ViewHolder {

    public String item;

    @Bind(R.id.item_name)
    TextView itemName;

    public StoreCupboardItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
