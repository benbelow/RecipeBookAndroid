package com.example.ben.recipebook.android.recipeList;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ben.recipebook.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeCardViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.recipe_card)
    CardView card;

    @Bind(R.id.recipe_card_title)
    TextView title;

    @Bind(R.id.recipe_card_image)
    ImageView image;

    @Bind(R.id.recipe_card_description)
    TextView description;

    public RecipeCardViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
